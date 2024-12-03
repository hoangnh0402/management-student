package com.example.demo.repo;

import com.example.demo.domain.dto.ClassroomSubjectDTO;
import com.example.demo.domain.model.ClassroomSubject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClassroomSubjectRepo extends JpaRepository<ClassroomSubject, Long> {
  @Query(value = "select * from classroom_in_subjects where id_subject = :idSubject", nativeQuery = true)
  List<ClassroomSubject> getByIdSubject(Long idSubject);

  @Query(value = "select * from classroom_in_subjects where id_subject = :idSubject and status = 0", nativeQuery = true)
  List<ClassroomSubject> getByIdSubjectAndStatus(Long idSubject);

  @Query(value = "select * from classroom_in_subjects cis where cis.id_subject = :subjectId and cis.status = 0 limit 1", nativeQuery = true)
  ClassroomSubject getByIdAndStatus(Long subjectId);

  @Query(value = "select * from classroom_in_subjects where classroom_code = :classroomCode", nativeQuery = true)
  ClassroomSubject getClassroomSubjectByClassroomCode(String classroomCode);

  @Query(value = "select * from classroom_in_subjects where classroom_code = :classroomCode and status = 2", nativeQuery = true)
  ClassroomSubject getClassroomSubjectByClassroomCodeAndStatus(String classroomCode);

  @Query(value = "select * from classroom_in_subjects cis where cis.id = :classroomId", nativeQuery = true)
  ClassroomSubject getClassroomSubjectById(Long classroomId);

  @Query(value = "select * from classroom_in_subjects cis where cis.id = :classroomId and cis.status = 0", nativeQuery = true)
  ClassroomSubject getClassroomSubjectByIdAndStatus(Long classroomId);

  @Query(value = "\n" +
      "select\n" +
      "\tcis.id_user \n" +
      "from\n" +
      "\tclassroom_in_subjects cis\n" +
      "join student_in_classroom_subjects sics on\n" +
      "\tcis.id = sics.id_class_sbject \n" +
      "\twhere sics.id = :classroomId", nativeQuery = true)
  Long getTeacherId(Long classroomId);

}
