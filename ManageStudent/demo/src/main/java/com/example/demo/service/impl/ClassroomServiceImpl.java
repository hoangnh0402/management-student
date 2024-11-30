package com.example.demo.service.impl;

import com.example.demo.domain.model.Classroom;
import com.example.demo.repo.ClassroomRepo;
import com.example.demo.service.ClassroomService;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;

@Service
public class ClassroomServiceImpl implements ClassroomService {
  private final ClassroomRepo classroomRepo;

  public ClassroomServiceImpl(ClassroomRepo classroomRepo) {
    this.classroomRepo = classroomRepo;
  }

  @Override
  public List<Classroom> getClassroomByCourseId(Long courseId) {
    Assert.notNull(courseId, "CourseId is null");
    return classroomRepo.getClassroomByCourseId(courseId);
  }
}
