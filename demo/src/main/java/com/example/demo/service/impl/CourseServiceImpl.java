package com.example.demo.service.impl;

import com.example.demo.domain.dto.CourseDTO;
import com.example.demo.domain.model.Course;
import com.example.demo.domain.model.User;
import com.example.demo.repo.CourseRepo;
import com.example.demo.service.CourseService;
import com.example.demo.utils.SecurityUtil;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.management.Query;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class CourseServiceImpl implements CourseService {
  private final CourseRepo courseRepo;

  public CourseServiceImpl(CourseRepo courseRepo) {
    this.courseRepo = courseRepo;
  }

  @Override
  public List<Course> getAllCourse() {
    return courseRepo.getAllCourse();
  }

  @Override
  public Course createCourse(CourseDTO courseDTO) {
    User user = SecurityUtil.getCurrentUserLogin();
    Assert.notNull(courseDTO.getNameCourse(), "Name course is null");
    Assert.notNull(courseDTO.getId(), "Code is null");
    Course item = courseRepo.getCourseByCourseId(courseDTO.getId());
    Assert.isNull(item, "code already exists");
    Course course = Course.builder()
        .nameCourse(courseDTO.getNameCourse())
        .id(courseDTO.getId()).createDatetime(LocalDateTime.now())
        .createUser(user!=null ? user.getUsername() : null)
        .build();
    courseRepo.save(course);
    return course;
  }
}
