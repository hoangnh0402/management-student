package com.example.demo.service;

import com.example.demo.domain.model.Classroom;

import java.util.List;

public interface ClassroomService {
  List<Classroom> getClassroomByCourseId(Long courseId);
}
