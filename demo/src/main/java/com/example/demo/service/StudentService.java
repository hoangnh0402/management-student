package com.example.demo.service;

import com.example.demo.domain.dto.*;
import com.example.demo.domain.model.StudentInClassroomSubject;
import com.example.demo.domain.model.User;
import org.springframework.data.domain.Page;

import java.util.LinkedHashMap;
import java.util.List;

public interface StudentService {
  StudentPointDTO getStudentByStudentCode(Long code);
  StudentPointDTO getStudentByStudentId(Long userId) throws Exception;
//  List<StudentPointDTO> getStudentByClassroom(Long courseId, Long classroomId);
//  List<StudentPointDTO> getStudentByPoint(Double pointStart, Double pointEnd);
  Page<StudentPointDTO> searchStudent(Long studentCode, Long courseId, Long classroomId, Double pointStart, Double pointEnd, Integer pageIndex, Integer pageSize) throws Exception;
  Page<DetailStudentDTO> getSubjectInStudent(Long studentId, Integer pageIndex, Integer pageSize);
  List<StudentSemesterDTO> getAccumulatedPointByStudentCode(Long studentCode);
  User createStudent(StudentDTO studentDTO) throws Exception;
  User changeStudent(StudentDTO studentDTO) throws Exception;
  User changeStudentByStudent(StudentDTO studentDTO) throws Exception;
  Page<StudentPointInClassroomDTO> viewPointInClassroom(String classroomCode, Integer pageIndex, Integer pageSize);
  StudentPointInClassroomDTO sendRequestChangePointClass(String classroomCode);
  StudentPointInClassroomDTO changePointInClassroom(StudentPointInClassroomDTO studentPointInClassroomDTO) throws Exception;
  void deleteStudentInClass(Long studentClassId);
  LinkedHashMap<String, String> getColumnForInputPoint();
  LinkedHashMap<String, String> getColumnForInput();
  Page<ClassroomSubjectDTO> viewSubjectClassRegister(String subjectCode, Integer pageIndex, Integer pageSize) throws Exception;
  Page<SubjectDTO> viewSubjectRegister(String subjectName, Integer pageIndex, Integer pageSize) throws Exception;

  StudentInClassroomSubject registerClassSubject(String classroomCode,  Long subjectId) throws Exception;
  Boolean cancelRegisterClassSubject(String classroomCode, Long subjectId) throws Exception;
}
