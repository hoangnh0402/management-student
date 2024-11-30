package com.example.demo.repo;

import com.example.demo.domain.dto.StudentPointDTO;

import java.util.List;

public interface StudentRepoCustom {
  List<StudentPointDTO> getStudent(Long studentCode, Long courseId, Long classroomId);
  StudentPointDTO getStudentByStudentId(Long studentId);
}
