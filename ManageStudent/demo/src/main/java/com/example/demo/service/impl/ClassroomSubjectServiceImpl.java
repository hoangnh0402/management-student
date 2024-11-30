package com.example.demo.service.impl;

import com.example.demo.domain.dto.ClassroomSubjectDTO;
import com.example.demo.domain.dto.StudentDTO;
import com.example.demo.domain.dto.StudentPointInClassroomDTO;
import com.example.demo.domain.dto.UserDTO;
import com.example.demo.domain.model.*;
import com.example.demo.repo.*;
import com.example.demo.service.ClassroomSubjectService;
import com.example.demo.service.StudentService;
import com.example.demo.utils.FileUtil;
import com.example.demo.utils.SecurityUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ClassroomSubjectServiceImpl implements ClassroomSubjectService {
  private final ClassroomSubjectRepo classroomSubjectRepo;
  private final ClassroomSubjectRepoCustom classroomSubjectRepoCustom;
  private final StudentInClassroomSubjectRepo studentInClassroomSubjectRepo;
  private final StudentService studentService;
  private final UserRepo userRepo;
  private final ProcessFileImportRepo processFileImportRepo;
  private final ClassroomRepo classroomRepo;
  private final CourseRepo courseRepo;

  public ClassroomSubjectServiceImpl(ClassroomSubjectRepo classroomSubjectRepo, ClassroomSubjectRepoCustom classroomSubjectRepoCustom, StudentInClassroomSubjectRepo studentInClassroomSubjectRepo, StudentService studentService, UserRepo userRepo, ProcessFileImportRepo processFileImportRepo,
                                     ClassroomRepo classroomRepo,
                                     CourseRepo courseRepo) {
    this.classroomSubjectRepo = classroomSubjectRepo;
    this.classroomSubjectRepoCustom = classroomSubjectRepoCustom;
    this.studentInClassroomSubjectRepo = studentInClassroomSubjectRepo;
    this.studentService = studentService;
    this.userRepo = userRepo;
    this.processFileImportRepo = processFileImportRepo;
    this.classroomRepo = classroomRepo;
    this.courseRepo = courseRepo;
  }

  @Override
  public List<ClassroomSubjectDTO> getClassroomSubject() throws Exception {
    User user = SecurityUtil.getCurrentUserLogin();
    if (user == null){
      throw new Exception(HttpStatus.UNAUTHORIZED.toString());
    }
    List<ClassroomSubjectDTO> classroomSubjectDTOS = classroomSubjectRepoCustom.getAllClassroomSubject(user.getId());
    return classroomSubjectDTOS;
  }

  @Override
  public Page<ClassroomSubjectDTO> getClassroomSubjectByUser(String subjectName, Integer status, Integer pageIndex, Integer pageSize) throws Exception {
    User user = SecurityUtil.getCurrentUserLogin();
    if (user == null){
      throw new Exception(HttpStatus.UNAUTHORIZED.toString());
    }
    List<ClassroomSubjectDTO> list = classroomSubjectRepoCustom.getClassroomSubjectBySubjectNameAndStatus(subjectName, status, user.getId());
    for (ClassroomSubjectDTO item: list) {
      Long quantityStudentNow = studentInClassroomSubjectRepo.getQuantityStudent(item.getIdClassroom());
      item.setQuantityStudentNow(quantityStudentNow);
    }
    Pageable pageRequest = PageRequest.of(pageIndex - 1, pageSize);
    int start = (int) pageRequest.getOffset();
    int end = Math.min(start + pageRequest.getPageSize(), list.size());
    List<ClassroomSubjectDTO> pageContent = list.subList(start, end);

    return new PageImpl<>(pageContent, pageRequest, list.size());
  }

  @Override
  public List<ClassroomSubjectDTO> getAllClassroomSubject(Long subjectId) {
    List<ClassroomSubjectDTO> list = classroomSubjectRepoCustom.getAllClassroomSubjectDetail(subjectId, null, null);
    for (ClassroomSubjectDTO item: list) {
      Long quantityStudentNow = studentInClassroomSubjectRepo.getQuantityStudent(item.getIdClassroom());
      item.setQuantityStudentNow(quantityStudentNow);
    }
    return list;
  }

  @Override
  public ClassroomSubjectDTO getClassroomByClassroomCode(Long subjectId, String classroomCode) throws Exception {
    List<ClassroomSubjectDTO> listClassroom = classroomSubjectRepoCustom.getAllClassroomSubjectDetail(subjectId, classroomCode, null);
    for (ClassroomSubjectDTO item: listClassroom) {
      Long quantityStudentNow = studentInClassroomSubjectRepo.getQuantityStudent(item.getIdClassroom());
      item.setQuantityStudentNow(quantityStudentNow);
    }
    if (listClassroom.size() != 0){
      ClassroomSubjectDTO classroom = listClassroom.get(0);
      if (classroom == null){
        throw new Exception("Không tìm thấy thông tin lớp học");
      }
      return classroom;
    } else {
      throw new Exception("Không tìm thấy thông tin lớp học");
    }

  }

  @Override
  public ClassroomSubject createClassroomSubject(ClassroomSubjectDTO classroomSubjectDTO, Long subjectId) throws Exception {
    User user = SecurityUtil.getCurrentUserLogin();
    if (user == null){
      throw new Exception(HttpStatus.UNAUTHORIZED.toString());
    }
    if (user.getIdRole() != 1){
      throw new Exception("Tài khoản không có quyền sử dụng chức năng này");
    }
    Assert.notNull(classroomSubjectDTO.getClassroomCode(), "Mã lớp chống");
    ClassroomSubject classroomSubject = classroomSubjectRepo.getClassroomSubjectByClassroomCode(classroomSubjectDTO.getClassroomCode());
    if (classroomSubject == null){
      ClassroomSubject classroomSubject1 = ClassroomSubject.builder()
          .idSubject(subjectId)
          .classroomCode(classroomSubjectDTO.getClassroomCode())
          .idUser(classroomSubjectDTO.getIdUser())
          .quantityStudent(classroomSubjectDTO.getQuantityStudent())
          .createDatetime(LocalDateTime.now())
          .status(-1)
          .build();
      classroomSubjectRepo.save(classroomSubject1);
      return  classroomSubject1;
    } else {
      throw new Exception("Mã lớp đã tồn tại");
    }
  }

  @Override
  public ClassroomSubject changeInfoClassroomSubject(ClassroomSubjectDTO classroomSubjectDTO, Long classroomId) throws Exception {
    User user = SecurityUtil.getCurrentUserLogin();
    if (user == null){
      throw new Exception(HttpStatus.UNAUTHORIZED.toString());
    }
    ClassroomSubject classroomSubject = classroomSubjectRepo.getClassroomSubjectById(classroomId);
    if ( classroomSubject != null && classroomSubject.getIdUser() == user.getId() && user.getIdRole() == 2){
      classroomSubject.setStatus(classroomSubjectDTO.getStatus());
      classroomSubject.setUpdateUser(user.getUsername());
      classroomSubject.setUpdateDatetime(LocalDateTime.now());
      classroomSubjectRepo.save(classroomSubject);
      return classroomSubject;
    } else if (user.getIdRole() != 1){
      throw new Exception("Tài khoản không có quyền sử dụng chức năng này");
    }

    if (classroomSubject!= null && user.getIdRole() == 1){
//      if (classroomSubject.getStatus() != -1){
//        throw new Exception("Không thể thay đổi thông tin lớp học");
//      }
      Assert.notNull(classroomSubjectDTO.getClassroomCode(), "Mã lớp chống");
      classroomSubject.setQuantityStudent(classroomSubjectDTO.getQuantityStudent());
      classroomSubject.setClassroomCode(classroomSubjectDTO.getClassroomCode());
      classroomSubject.setIdUser(classroomSubjectDTO.getIdUser());
      classroomSubject.setUpdateDatetime(LocalDateTime.now());
      classroomSubject.setStatus(classroomSubjectDTO.getStatus());
      classroomSubjectRepo.save(classroomSubject);
      return classroomSubject;
    } else {
      throw new Exception("Lớp không tồn tại");
    }
  }

  @Override
  public StudentInClassroomSubject addStudentInClassroom(Long classroomId, Long subjectId, Long userId) throws Exception {
    User user = SecurityUtil.getCurrentUserLogin();
    if (user == null){
      throw new Exception(HttpStatus.UNAUTHORIZED.toString());
    }
    if (user.getIdRole() != 1){
      throw new Exception("Tài khoản không có quyền sử dụng chức năng này");
    }
    ClassroomSubject classroomSubject = classroomSubjectRepo.getClassroomSubjectByIdAndStatus(classroomId);
//    if (classroomSubject != null){
//      throw new Exception("Lớp học đã bắt đầu, không thể thêm sinh viên");
//    }
    Long result = studentInClassroomSubjectRepo.getStudentInStudentClass(userId, subjectId);
    if (result == null){
        Long quantityStudentInClass = studentInClassroomSubjectRepo.getQuantityStudent(classroomId);
      if (quantityStudentInClass < classroomSubject.getQuantityStudent()){
        StudentInClassroomSubject studentInClassroomSubject = StudentInClassroomSubject.builder()
                                                              .idClassroomInSubject(classroomId)
                                                              .idUser(userId)
                                                              .createUser(user.getUsername())
                                                              .createDatetime(LocalDateTime.now())
                                                              .createDatetime(LocalDateTime.now())
                                                              .build();
        studentInClassroomSubjectRepo.save(studentInClassroomSubject);
        return studentInClassroomSubject;
      } else {
        throw new Exception("Lớp đã đầy.");
      }
    } else {
      throw new Exception("Sinh viên đang học môn học này");
    }
  }

  @Override
  public Map<String, Integer> statisticalPoint(String classroomCode) {
    Page<StudentPointInClassroomDTO> pageStudentPoint = studentService.viewPointInClassroom(classroomCode, 1, 100);
    List<StudentPointInClassroomDTO> listStudentPoint = pageStudentPoint.getContent();
    Map<String, Integer> mapPoint = new HashMap<>();
    Integer diemF = 0;
    Integer diemA = 0;
    Integer diemB = 0;
    Integer diemC= 0;
    Integer diemD = 0;
    for (StudentPointInClassroomDTO item: listStudentPoint) {
      if (item.getPoint() == null){
        diemF++;
      }
      if (item.getPoint() >=0 && item.getPoint() <4.0){
        diemF++;
      }
      if (item.getPoint()>=4.0 && item.getPoint() <5.5){
        diemD++;
      }
      if (item.getPoint()>=5.5 && item.getPoint() <7.0){
        diemC++;
      }
      if (item.getPoint()>=7.0 && item.getPoint() <8.5){
        diemB++;
      }
      if (item.getPoint()>=8.5 && item.getPoint() <=10.0){
        diemA++;
      }
    }
    mapPoint.put("F", diemF);
    mapPoint.put("D", diemD);
    mapPoint.put("C", diemC);
    mapPoint.put("B", diemB);
    mapPoint.put("A", diemA);
    return mapPoint;
  }

  @Override
  public Map<StudentDTO, String> statisticalPointStudent(String classroomCode) {
    Page<StudentPointInClassroomDTO> pageStudentPoint = studentService.viewPointInClassroom(classroomCode, 1, 100);
    List<StudentPointInClassroomDTO> listStudentPoint = pageStudentPoint.getContent();
    Map<StudentDTO, String> mapStudent = new HashMap<>();
    for (StudentPointInClassroomDTO item: listStudentPoint) {
      StudentDTO student = new StudentDTO();
      User user = userRepo.getStudentByStudentCode(item.getStudentCode());
      Classroom classroom = classroomRepo.getClassroomByClassroomId(user.getIdClass());
      student.setStudentCode(user.getCode());
      student.setStudentName(user.getName());
      student.setClassroomName(classroom.getNameClass());
      student.setCourseName(courseRepo.getCourseByCourseId(classroom.getIdCourse()).getNameCourse());
      if (item.getPoint() == null) {
        mapStudent.put(student, "F");
      }
      if (item.getPoint() >= 0 && item.getPoint() < 4.0) {
        mapStudent.put(student, "F");
      }
      if (item.getPoint() >= 4.0 && item.getPoint() < 5.5) {
        mapStudent.put(student, "D");
      }
      if (item.getPoint() >= 5.5 && item.getPoint() < 7.0) {
        mapStudent.put(student, "C");
      }
      if (item.getPoint() >= 7.0 && item.getPoint() < 8.5) {
        mapStudent.put(student, "B");
      }
      if (item.getPoint() >= 8.5 && item.getPoint() <= 10.0) {
        mapStudent.put(student, "A");
      }
    }
    return mapStudent;
  }

  @Override
  public Page<ProcessFileImport> viewDocumentInClassroom(String classroomCode, Integer pageIndex, Integer pageSize) {
    ClassroomSubject classroomSubject = classroomSubjectRepo.getClassroomSubjectByClassroomCode(classroomCode);
    List<ProcessFileImport> listFile = processFileImportRepo.getListFileByClassroomId(classroomSubject.getId());
    Pageable pageRequest = PageRequest.of(pageIndex - 1, pageSize);
    int start = (int) pageRequest.getOffset();
    int end = Math.min(start + pageRequest.getPageSize(), listFile.size());
    List<ProcessFileImport> pageContent = listFile.subList(start, end);

    return new PageImpl<>(pageContent, pageRequest, listFile.size());
  }

  @Override
  public Long uploadDocumentInClassroom(String classroomCode, MultipartFile file, byte[] fileContent) throws Exception {
    User user = SecurityUtil.getCurrentUserLogin();
    if (user == null) {
      throw new Exception(HttpStatus.UNAUTHORIZED.toString());
    }
    try {
      ClassroomSubject classroomSubject = classroomSubjectRepo.getClassroomSubjectByClassroomCode(classroomCode);
      String filePath = FileUtil.saveDocument(file);
      ProcessFileImport process = ProcessFileImport.builder()
          .createDatetime(LocalDateTime.now())
          .filePath(filePath)
          .type(3L)
          .classroomId(classroomSubject.getId())
          .keyResponse(file.getOriginalFilename())
          .createUser(user.getUsername())
          .build();
      processFileImportRepo.save(process);
      return process.getId();
    } catch (IOException e) {
      e.printStackTrace();
      throw new IOException(e.getMessage());
    }
  }

  @Override
  public ClassroomSubjectDTO getClassroomSubjectByCode(String classroomCode) {
    ClassroomSubjectDTO classroomSubject = classroomSubjectRepoCustom.getClassroomSubject(classroomCode).get(0);
    return classroomSubject;
  }


}
