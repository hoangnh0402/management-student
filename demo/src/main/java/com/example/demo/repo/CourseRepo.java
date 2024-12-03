package com.example.demo.repo;

import com.example.demo.domain.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseRepo extends JpaRepository<Course, Long> {
  @Query(value = "select * from courses", nativeQuery = true)
  List<Course> getAllCourse();

  @Query(value = "select * from courses where id = :courseId", nativeQuery = true)
  Course getCourseByCourseId(Long courseId);

  @Query(value = "select id from courses where  name_course = :courseName", nativeQuery = true)
  Long getIdByCourseName(String courseName);

  @Query(value = "select c2.name_course  from users u join classroomes c on u.id_class = c.id join courses c2 on c.id_course = c2.id where u.code = :studentCode", nativeQuery = true)
  String getCourseNameByStudentCode(Long studentCode);
}
