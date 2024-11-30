package com.example.demo.service.impl;

import com.example.demo.domain.dto.InsertFieldDTO;
import com.example.demo.domain.dto.SearchProcessDTO;
import com.example.demo.domain.dto.StudentPointInClassroomDTO;
import com.example.demo.domain.model.ClassroomSubject;
import com.example.demo.domain.model.ProcessFileImport;
import com.example.demo.domain.model.User;
import com.example.demo.repo.*;
import com.example.demo.service.FileService;
import com.example.demo.service.StudentService;
import com.example.demo.utils.SecurityUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceUnit;
import java.awt.*;
import java.io.*;
import java.time.LocalDateTime;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

import static com.example.demo.config.DataUtil.validateColumnType;

@Service
@Slf4j
public class FileServiceImpl implements FileService {
  private final ClassroomSubjectRepo classroomSubjectRepo;
  private final ProcessFileImportRepo processFileImportRepo;
  private final StudentService studentService;
  private final CourseRepo courseRepo;
  private final ClassroomRepo classroomRepo;
  private final UserRepo userRepo;
  private final DataFormatter df = new DataFormatter();
  @PersistenceContext
  private EntityManager entityManager;
  @PersistenceUnit
  private EntityManagerFactory entityManagerFactory;
  @Value("${upload.file.path}")
  private String filePath;

  public FileServiceImpl(ProcessFileImportRepo processFileImportRepo, StudentService studentService, CourseRepo courseRepo, ClassroomRepo classroomRepo, UserRepo userRepo,
                         ClassroomSubjectRepo classroomSubjectRepo) {
    this.processFileImportRepo = processFileImportRepo;
    this.studentService = studentService;
    this.courseRepo = courseRepo;
    this.classroomRepo = classroomRepo;
    this.userRepo = userRepo;
    this.classroomSubjectRepo = classroomSubjectRepo;
  }

  @Override
  public Page<SearchProcessDTO> queryProcess(Integer pageIndex, Integer pageSize) {
    List<SearchProcessDTO> result = new ArrayList<>();
    List<ProcessFileImport> allFile = processFileImportRepo.findAllByStatusInOrderByCreateDatetimeDesc(List.of(0, 1, 2, 3, -1));
    for (ProcessFileImport processFileImport : allFile) {
      result.add(SearchProcessDTO.builder()
          .id(processFileImport.getId())
          .fileName(processFileImport.getKeyRequest())
          .createDatetime(processFileImport.getCreateDatetime())
          .status(processFileImport.getStatus())
          .build());
    }
    Pageable pageRequest = PageRequest.of(pageIndex - 1, pageSize);
    int start = (int) pageRequest.getOffset();
    int end = Math.min(start + pageRequest.getPageSize(), result.size());
    List<SearchProcessDTO> pageContent = result.subList(start, end);

    return new PageImpl<>(pageContent, pageRequest, result.size());
  }


  @Override
  public Long uploadFile(MultipartFile file, byte[] fileContent, String classroomCode) throws Exception {
    User user = SecurityUtil.getCurrentUserLogin();
    if (user == null) {
      throw new Exception(HttpStatus.UNAUTHORIZED.toString());
    }
    String[] str = Objects.requireNonNull(file.getOriginalFilename()).split("\\.");
    String filename = String.format("%s(%s).%s", Arrays.stream(str).filter(item -> !item.equals(str[str.length - 1])).collect(Collectors.joining(".")),
        UUID.randomUUID(), str[str.length - 1]);
    try {
      ClassroomSubject classroomSubject = classroomSubjectRepo.getClassroomSubjectByClassroomCode(classroomCode);
      ProcessFileImport process = ProcessFileImport.builder()
          .createDatetime(LocalDateTime.now())
          .fileContent(fileContent)
          .filePath(filePath)
          .status(0)
          .keyRequest(filename)
          .createUser(user.getUsername())
          .classroomId(classroomSubject.getId())
          .build();
      processFileImportRepo.save(process);
      FileCopyUtils.copy(fileContent, new File(filePath, filename));
      return process.getId();
    } catch (IOException e) {
      e.printStackTrace();
      throw new IOException(e.getMessage());
    }
  }

  @Override
  public boolean insertField(InsertFieldDTO insertFieldDTO, Integer typeInsert) throws Exception {
    Optional<ProcessFileImport> processFileImport = processFileImportRepo.findById(insertFieldDTO.getId());
    User user = SecurityUtil.getCurrentUserLogin();
    if (user == null) {
      throw new Exception(HttpStatus.UNAUTHORIZED.toString());
    }

    if (processFileImport.get() != null) {
      if (typeInsert == 2) {
        processFileImport.get().setMapField(new ObjectMapper().writeValueAsString(insertFieldDTO.getMapFields()));
        processFileImport.get().setSchema("manage_student");
        processFileImport.get().setTable("student_in_classroom_subjects");
        processFileImport.get().setStatus(1);
        processFileImport.get().setType(2L);
        processFileImport.get().setUpdateDatetime(LocalDateTime.now());
        processFileImport.get().setUpdateUser(user.getUsername());
        processFileImportRepo.save(processFileImport.get());
        return true;
      }
      if (typeInsert == 1) {
        processFileImport.get().setMapField(new ObjectMapper().writeValueAsString(insertFieldDTO.getMapFields()));
        processFileImport.get().setSchema("manage_student");
        processFileImport.get().setTable("users");
        processFileImport.get().setStatus(1);
        processFileImport.get().setType(1L);
        processFileImport.get().setUpdateUser(user.getUsername());
        processFileImport.get().setUpdateDatetime(LocalDateTime.now());
        processFileImportRepo.save(processFileImport.get());
        return true;
      }
    }
    return false;
  }

  @Override
  public void importFile(ProcessFileImport processFileImport) throws IOException {
    String schema = processFileImport.getSchema();
    String table = processFileImport.getTable();
    String mapFieldsStr = processFileImport.getMapField();
    String fileName = "result_" + processFileImport.getKeyRequest();
    byte[] data = processFileImport.getFileContent();
    Long type = processFileImport.getType();

    InputStream is = new BufferedInputStream(new ByteArrayInputStream(data));
    XSSFWorkbook wb = new XSSFWorkbook(is);

    boolean checkField = true;
    Sheet sheet = wb.getSheetAt(0);
    HashMap<String, String> mapFields = new ObjectMapper().readValue(mapFieldsStr, HashMap.class);
    mapFields.entrySet().removeIf(e -> !StringUtils.hasText(e.getValue()));

    HashMap<Integer, String> columnFile = new HashMap<>();
    HashSet<String> mapFieldFile = new HashSet<>(mapFields.values());
    if (type == 1) {
      StringBuilder insertQuery = new StringBuilder("insert into " + schema + "." + table);
      try {
        for (Row row : sheet) {
          Long courseId = null;
          Long classroomId = null;
          boolean checkStudent = false;
          if (checkField) {
            for (int i = 0; i < row.getLastCellNum(); i++) {
              Cell cell = row.getCell(i);
              if (mapFieldFile.contains(cell.getStringCellValue()))
                columnFile.put(i, row.getCell(i).getStringCellValue());

            }
            row.createCell(row.getLastCellNum()).setCellValue("Import result");
            row.createCell(row.getLastCellNum()).setCellValue("Description");
            checkField = false;
            continue;
          }
          StringBuilder columnQuery = new StringBuilder(" ( ");
          StringBuilder valuesQuery = new StringBuilder(" value ( ");
          boolean checkFirst = true;
          int i;
          for (i = 0; i < row.getLastCellNum(); i++) {

            String course = "";
            String classroom = "";
            Cell cell = row.getCell(i);
            String fileColumn = columnFile.get(i);
            if (Objects.nonNull(fileColumn)) {
              for (Map.Entry<String, String> itemCourse : mapFields.entrySet()) {
                if (itemCourse.getValue().equals(fileColumn)) {
                  if (itemCourse.getKey().equals("id_course")) {
                    course = itemCourse.getValue();
                    courseId = courseRepo.getIdByCourseName(df.formatCellValue(cell));
                    break;
                  }
                }
              }
              for (Map.Entry<String, String> itemClass : mapFields.entrySet()) {
                if (itemClass.getValue().equals(fileColumn)) {
                  if (itemClass.getKey().equals("id_class")) {
                    classroom = itemClass.getValue();
                    classroomId = classroomRepo.getIdByClassNameAndCourseId(courseId, df.formatCellValue(cell));
                    break;
                  }
                }
              }

              if (!fileColumn.equals(course) && !fileColumn.equals(classroom) && !checkStudent) {
                String valueStr = df.formatCellValue(cell);
                if (StringUtils.hasText(valueStr)) {
                  if (checkFirst) {
                    for (Map.Entry<String, String> item : mapFields.entrySet()) {
                      if (item.getValue().equals(fileColumn)) {
                        columnQuery.append(item.getKey());
                      }
                    }
                    valuesQuery.append("'").append(valueStr).append("'");
                    checkFirst = false;
                    continue;
                  }
                  boolean checkColumn = true;
                  for (Map.Entry<String, String> item : mapFields.entrySet()) {
                    if (item.getValue().equals(fileColumn)) {
                      columnQuery.append(", ").append(item.getKey());
                      checkColumn = false;
                      break;
                    }
                  }
                  if (checkColumn) {
                    columnQuery.append(", ").append(mapFields.get(fileColumn));
                  }

                  valuesQuery.append(", '").append(valueStr).append("'");
                }
              }
            }

          }
          columnQuery.append(" , id_class , create_user, create_datetime)");
          valuesQuery.append(", ").append(classroomId).append(" , ").append("'PROCESS' , '").append(LocalDateTime.now()).append("')");

          try {
            EntityManager entityManager = entityManagerFactory.createEntityManager();
            entityManager.getTransaction().begin();
            entityManager.createNativeQuery(insertQuery.toString() + columnQuery + valuesQuery).executeUpdate();
            entityManager.flush();
            entityManager.getTransaction().commit();
            entityManager.close();
            row.createCell(i).setCellValue("Import data success");
          } catch (Exception ex) {
            row.createCell(i++).setCellValue("Import date fail");
            row.createCell(i).setCellValue(ex.getMessage());
          }
        }
        is.close();
        String[] validateName = fileName.replaceAll(" ", "").split("\\.+\\w+$");
        log.info("File name {} ", Arrays.toString(validateName));
        Assert.isTrue(validateName.length != 0, "Filename is null");
        StringBuilder name = new StringBuilder(fileName);
        ByteArrayOutputStream boas = new ByteArrayOutputStream();
        wb.write(boas);
        wb.close();

        InputStream fis = new ByteArrayInputStream(IOUtils.toByteArray(new ByteArrayInputStream(boas.toByteArray())));
        FileCopyUtils.copy(fis.readAllBytes(), new File(filePath, String.valueOf(name)));

        processFileImport.setDiscription(boas.toByteArray());
        processFileImport.setStatus(2);
        processFileImport.setKeyResponse(name.toString());
        processFileImport.setUpdateUser("PROCESS");
        processFileImport.setUpdateDatetime(LocalDateTime.now());
        processFileImportRepo.save(processFileImport);
      } catch (Exception e) {
        processFileImport.setStatus(3);
        processFileImport.setCreateUser(e.getMessage());
        processFileImport.setUpdateUser("PROCESS");
        processFileImport.setUpdateDatetime(LocalDateTime.now());
        processFileImportRepo.save(processFileImport);
      }
    }

    if (type == 2){
      StringBuilder update = new StringBuilder("update " + schema + "." + table + "  set  ");
      Long classroomId = processFileImport.getClassroomId();
      try {
        for (Row row : sheet) {
          Long studentId = null;
          String student = null;
          String studentName = null;
          if (checkField) {
            for (int i = 0; i < row.getLastCellNum(); i++) {
              Cell cell = row.getCell(i);
              if (mapFieldFile.contains(cell.getStringCellValue()))
                columnFile.put(i, row.getCell(i).getStringCellValue());

            }
            row.createCell(row.getLastCellNum()).setCellValue("Nhập kết quả");
            row.createCell(row.getLastCellNum()).setCellValue("Sự miêu tả");
            checkField = false;
            continue;
          }
          StringBuilder columnQuery = new StringBuilder(" ");
          StringBuilder whereQuery = new StringBuilder(" ");
          boolean checkFirst = true;
          int i;
          for (i = 0; i < row.getLastCellNum(); i++) {

            Cell cell = row.getCell(i);
            String fileColumn = columnFile.get(i);
            if (Objects.nonNull(fileColumn)) {
              for (Map.Entry<String, String> itemCode : mapFields.entrySet()) {
                if (itemCode.getValue().equals(fileColumn)) {
                    if (itemCode.getKey().equals("code")){
                      studentId = userRepo.getStudentIdByStudentCode(Long.parseLong(df.formatCellValue(cell)));
                      student = itemCode.getValue();
                    }
                  if (itemCode.getKey().equals("name")){
                    studentName = itemCode.getValue();
                  }
                }
              }

              if (!fileColumn.equals(student) && !fileColumn.equals(studentName)) {
                String valueStr = df.formatCellValue(cell);
                if (StringUtils.hasText(valueStr)) {
                  if (checkFirst) {
                    for (Map.Entry<String, String> item : mapFields.entrySet()) {
                      if (item.getValue().equals(fileColumn)) {
                        columnQuery.append(item.getKey()).append(" = ").append("").append(valueStr).append("");
                      }
                    }
                    checkFirst = false;
                  } else {
                    for(Map.Entry<String, String> item : mapFields.entrySet()){
                      if (item.getValue().equals(fileColumn)) {
                        columnQuery.append(" , ").append(item.getKey()).append(" = ").append("").append(valueStr).append("");
                      }
                    }
                  }
                }
              }
            }

          }
          columnQuery.append(" ,   update_datetime = '").append(LocalDateTime.now()).append("' ,   update_user = ").append("'PROCESS'");
          whereQuery.append("  where id_user = ").append(studentId).append(" and id_class_sbject = ").append(classroomId);
          try {
            EntityManager entityManager = entityManagerFactory.createEntityManager();
            entityManager.getTransaction().begin();
            Integer rowColumnUpdate = entityManager.createNativeQuery(update.toString() + columnQuery + whereQuery).executeUpdate();
            entityManager.flush();
            entityManager.getTransaction().commit();
            entityManager.close();
            if (rowColumnUpdate == 0){
              throw new Exception("Không tồn tại bản ghi này");
            }
            row.createCell(i).setCellValue("Nhập dữ liệu thành công");
          } catch (Exception ex) {
            row.createCell(i++).setCellValue("Nhập dữ liệu không thành công");
            row.createCell(i).setCellValue(ex.getMessage());
          }
        }
        is.close();
        String[] validateName = fileName.replaceAll(" ", "").split("\\.+\\w+$");
        log.info("File name {} ", Arrays.toString(validateName));
        Assert.isTrue(validateName.length != 0, "Tên tệp là null");
        StringBuilder name = new StringBuilder(fileName);
        ByteArrayOutputStream boas = new ByteArrayOutputStream();
        wb.write(boas);
        wb.close();

        InputStream fis = new ByteArrayInputStream(IOUtils.toByteArray(new ByteArrayInputStream(boas.toByteArray())));
        FileCopyUtils.copy(fis.readAllBytes(), new File(filePath, String.valueOf(name)));

        processFileImport.setDiscription(boas.toByteArray());
        processFileImport.setStatus(2);
        processFileImport.setKeyResponse(name.toString());
        processFileImport.setUpdateUser("PROCESS");
        processFileImport.setUpdateDatetime(LocalDateTime.now());
        processFileImportRepo.save(processFileImport);
      } catch (Exception e) {
        processFileImport.setStatus(3);
        processFileImport.setCreateUser(e.getMessage());
        processFileImport.setUpdateUser("PROCESS");
        processFileImport.setUpdateDatetime(LocalDateTime.now());
        processFileImportRepo.save(processFileImport);
      }
    }
  }
}
