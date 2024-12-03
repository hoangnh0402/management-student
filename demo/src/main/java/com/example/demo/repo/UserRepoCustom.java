package com.example.demo.repo;

import com.example.demo.domain.dto.TeacherDTO;

import java.util.List;

public interface UserRepoCustom {
  List<TeacherDTO> getAllTeacherInfo();
}
