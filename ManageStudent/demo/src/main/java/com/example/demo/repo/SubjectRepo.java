package com.example.demo.repo;

import com.example.demo.domain.model.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubjectRepo extends JpaRepository<Subject, Long> {
  @Query(value = "select\n" +
      "\ts.*\n" +
      "from\n" +
      "\tstudent_in_classroom_subjects sics\n" +
      "inner join classroom_in_subjects cis on\n" +
      "\tsics.id_class_sbject = cis.id\n" +
      "inner join subjects s on\n" +
      "\tcis.id_subject = s.id\n" +
      "where s.id_semester = :idSemester and sics.id_student = :idStudent", nativeQuery = true)
  List<Subject> getIdSubjectBySemesterAndStudent(Long idSemester, Long idStudent);

  @Query(value = "select * from subjects s where s.subject_code = :subjectCode", nativeQuery = true)
  Subject getSubjectBySubjectCode(String subjectCode);

  @Query(value = "select * from subjects s where s.id = :subjectId", nativeQuery = true)
  Subject getSubjectBySubjectId(Long subjectId);


  @Query(value = "select s.* from classroom_in_subjects cis join subjects s on cis.id_subject = s.id where cis.id = :classSubjectId", nativeQuery = true)
  Subject getSubjectByClassId(Long classSubjectId);

  @Query(value = "select * from subjects s where s.subject_name like concat('%'," + ":subjectName," + "'%')", nativeQuery = true)
  List<Subject> getSubjectBySubjectName(String subjectName);

}
