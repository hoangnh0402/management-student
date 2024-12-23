package com.example.demo.repo;

import com.example.demo.domain.model.Semester;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SemesterRepo extends JpaRepository<Semester, Long> {
  @Query(value = "select * from semesters", nativeQuery = true)
  List<Semester> getAllSemester();
}
