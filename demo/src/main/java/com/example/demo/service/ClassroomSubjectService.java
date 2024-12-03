package com.example.demo.service;

import com.example.demo.domain.dto.ClassroomSubjectDTO;
import com.example.demo.domain.dto.StudentDTO;
import com.example.demo.domain.model.ClassroomSubject;
import com.example.demo.domain.model.ProcessFileImport;
import com.example.demo.domain.model.StudentInClassroomSubject;
import com.example.demo.domain.model.User;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface ClassroomSubjectService {
  List<ClassroomSubjectDTO> getClassroomSubject() throws Exception;
  Page<ClassroomSubjectDTO> getClassroomSubjectByUser(String subjectName, Integer status, Integer pageIndex, Integer pageSize) throws Exception;
  List<ClassroomSubjectDTO> getAllClassroomSubject(Long subjectId);
  ClassroomSubjectDTO getClassroomByClassroomCode(Long subjectId, String classroomCode) throws Exception;
  ClassroomSubject createClassroomSubject(ClassroomSubjectDTO classroomSubjectDTO, Long subjectId) throws Exception;
  ClassroomSubject changeInfoClassroomSubject(ClassroomSubjectDTO classroomSubjectDTO, Long classroomId) throws Exception;

  StudentInClassroomSubject addStudentInClassroom(Long classroomId, Long subjectId, Long userId) throws Exception;

  Map<String, Integer> statisticalPoint(String classroomCode);
  Map<StudentDTO, String> statisticalPointStudent(String classroomCode);

  Page<ProcessFileImport> viewDocumentInClassroom(String classroomCode, Integer pageIndex, Integer pageSize);
  Long uploadDocumentInClassroom(String classroomCode, MultipartFile file, byte[] fileContent) throws Exception;

  ClassroomSubjectDTO getClassroomSubjectByCode(String classroomCode);

}
