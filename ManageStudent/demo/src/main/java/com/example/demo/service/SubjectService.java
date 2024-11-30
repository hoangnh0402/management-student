package com.example.demo.service;

import com.example.demo.domain.dto.ClassroomDTO;
import com.example.demo.domain.dto.SubjectDTO;
import com.example.demo.domain.model.Subject;
import org.springframework.data.domain.Page;

import java.util.List;

public interface SubjectService {
  Subject getSubjectBySubjectCode(String subjectCode);
  Page<Subject> getAllSubject(String subjectName, Integer pageIndex, Integer pageSize);
  List<ClassroomDTO> getClassroomBySubjectId(Long subjectId);
  Subject getSubjectBySubjectId(Long subjectId) throws Exception;
  Subject createSubject(SubjectDTO subjectDTO) throws Exception;
  Subject changeSubject(SubjectDTO subjectDTO, Long subjectId) throws Exception;
}
