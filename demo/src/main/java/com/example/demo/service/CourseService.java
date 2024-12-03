package com.example.demo.service;

import com.example.demo.domain.dto.CourseDTO;
import com.example.demo.domain.model.Course;

import java.util.List;

public interface CourseService {
  List<Course> getAllCourse();
  Course createCourse(CourseDTO courseDTO);
}
