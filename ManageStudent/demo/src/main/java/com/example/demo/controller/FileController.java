package com.example.demo.controller;

import com.example.demo.domain.dto.InsertFieldDTO;
import com.example.demo.domain.dto.StudentPointInClassroomDTO;
import com.example.demo.domain.model.ProcessFileImport;
import com.example.demo.repo.ProcessFileImportRepo;
import com.example.demo.service.FileService;
import com.example.demo.service.StudentService;
import com.example.demo.service.impl.UserPDFExporter;
import com.lowagie.text.DocumentException;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static com.example.demo.common.Const.RETURN_CODE_ERROR;

@RestController
@RequestMapping("/file")
@Slf4j
public class FileController extends CommonController {
  private final ProcessFileImportRepo processFileImportRepo;
  private final FileService fileService;
  private final StudentService studentService;
  private final UserPDFExporter userPDFExporter;
  @Value("${upload.file.path}")
  private String filePath;

  public FileController(FileService fileService,
                        ProcessFileImportRepo processFileImportRepo, StudentService studentService, UserPDFExporter userPDFExporter) {
    this.fileService = fileService;
    this.processFileImportRepo = processFileImportRepo;
    this.studentService = studentService;
    this.userPDFExporter = userPDFExporter;
  }

  @Operation(summary = "API lấy trạng thái các file dã upload")
  @GetMapping("/process-file")
  public ResponseEntity<?> getAllProcessFile(@RequestParam(value = "pageIndex") Integer pageIndex,
                                             @RequestParam(value = "pageSize") Integer pageSize) {
    try {
      return toSuccessResult(fileService.queryProcess(pageIndex, pageSize));
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      return toExceptionResult(e.getMessage(), RETURN_CODE_ERROR);
    }
  }

  @Operation(summary = "API download file")
  @GetMapping("/download-file")
  public ResponseEntity<?> downloadFile(@RequestParam(value = "idFile") Long idFile) {
    try {
      HttpHeaders httpHeaders = new HttpHeaders();
      httpHeaders.setContentType(MediaType.APPLICATION_OCTET_STREAM);
      InputStream inputStream;
      byte[] data;
      ProcessFileImport processFileImport = processFileImportRepo.getProcessFileById(idFile);
      Assert.notNull(processFileImport, "File download does not exits");
      data = processFileImport.getDiscription();
      String path = filePath + "/" + processFileImport.getKeyResponse();
      httpHeaders.set("Content-disposition", "attachment; filename=" + processFileImport.getKeyResponse());
      httpHeaders.setContentLength(data.length);
      inputStream = new BufferedInputStream(new ByteArrayInputStream(data));
      InputStreamResource inputStreamResource = new InputStreamResource(inputStream);
      return new ResponseEntity<>(inputStreamResource, httpHeaders, HttpStatus.OK);
    } catch (Exception ex) {
      log.error(ex.getMessage(), ex);
      return toExceptionResult(ex.getMessage(), RETURN_CODE_ERROR);
    }
  }

  @Operation(summary = "API upload file")
  @PostMapping(value = "/upload-file")
  ResponseEntity<?> uploadFile(@RequestBody MultipartFile file, @RequestParam("classroomCode") String classroomCode) {
    try {
      return toSuccessResult(fileService.uploadFile(file, file.getBytes(), classroomCode));
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      return toExceptionResult(e.getMessage(), RETURN_CODE_ERROR);
    }
  }

  @Operation(summary = "API insert field mapping")
  @PostMapping(value = "/insert-field")
  ResponseEntity<?> insertField(@RequestBody InsertFieldDTO insertFieldDTO, @RequestParam(value = "typeInsert") Integer typeInsert) {
    try {
      return toSuccessResult(fileService.insertField(insertFieldDTO, typeInsert));
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      return toExceptionResult(e.getMessage(), RETURN_CODE_ERROR);
    }
  }

  @GetMapping("/export/pdf")
  public void exportToPDF(@RequestParam(name = "classroomCode") String classroomCode, HttpServletResponse response) throws DocumentException, IOException {
    response.setContentType("application/pdf");
    DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
    String currentDateTime = dateFormatter.format(new Date());

    String headerKey = "Content-Disposition";
    String headerValue = "attachment; filename=users_" + currentDateTime + ".pdf";
    response.setHeader(headerKey, headerValue);

    Page<StudentPointInClassroomDTO> pageUsers = studentService.viewPointInClassroom(classroomCode, 1, 100);
    List<StudentPointInClassroomDTO> listUsers = pageUsers.getContent();
    userPDFExporter.export(response, listUsers);
  }

  @GetMapping("/download-document")
  public ResponseEntity<?> downloadDocument(@RequestParam("idFile") Long fileId) {
    try {
      ProcessFileImport processFileImport = processFileImportRepo.getProcessFileById(fileId);
      Assert.notNull(processFileImport, "File download does not exist");

      File file = new File(processFileImport.getFilePath());
      if (!file.exists()) {
        throw new FileNotFoundException("File not found: " + processFileImport.getFilePath());
      }

      HttpHeaders httpHeaders = new HttpHeaders();
      httpHeaders.setContentType(MediaType.APPLICATION_OCTET_STREAM);
      httpHeaders.setContentDispositionFormData("attachment", processFileImport.getKeyResponse());
      httpHeaders.setContentLength(file.length());

      InputStreamResource resource = new InputStreamResource(new FileInputStream(file));

      return ResponseEntity.ok()
          .headers(httpHeaders)
          .body(resource);
    } catch (FileNotFoundException ex) {
      log.error("File not found: " + ex.getMessage(), ex);
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("File not found");
    } catch (Exception ex) {
      log.error(ex.getMessage(), ex);
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred while downloading the file");
    }
  }

}
