package com.example.demo.repo;

import com.example.demo.domain.model.StudentInClassroomSubject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentInClassroomSubjectRepo extends JpaRepository<StudentInClassroomSubject, Long> {
  @Query(value = "select * from student_in_classroom_subjects s where id_user = :studentId", nativeQuery = true)
  List<StudentInClassroomSubject> findByStudentId(Long studentId);

  @Query(value = "select cis.id_subject  from student_in_classroom_subjects sics join classroom_in_subjects cis on sics.id_class_sbject = cis.id where sics.id_class_sbject = :idClassSubject and sics.id_user = :idStudent", nativeQuery = true)
  Long getIdSubject(Long idClassSubject, Long idStudent);

  @Query(value = "select cis.classroom_code  from student_in_classroom_subjects sics join classroom_in_subjects cis on sics.id_class_sbject = cis.id where sics.id_class_sbject = :idClassSubject and sics.id_user = :idStudent order by sics.create_datetime desc limit 1", nativeQuery = true)
  String getClassroomCode(Long idClassSubject, Long idStudent);

  @Query(value = "select\n" +
      "\tsics.*\n" +
      "from\n" +
      "\tstudent_in_classroom_subjects sics\n" +
      "inner join classroom_in_subjects cis on\n" +
      "\tcis.id = sics.id_class_sbject \n" +
      "where cis.id_subject = :idSubject", nativeQuery = true)
  List<StudentInClassroomSubject> getListByIdSubject(Long idSubject);

  @Query(value = "select sics.*  from student_in_classroom_subjects sics join classroom_in_subjects cis on sics.id_class_sbject = cis.id \n" +
      "where cis.classroom_code = :classroomCode", nativeQuery = true)
  List<StudentInClassroomSubject> getStudentInClassroomSubjectsByClassroomCode(String classroomCode);

  @Query(value = "select sics.*  from student_in_classroom_subjects sics join classroom_in_subjects cis on sics.id_class_sbject = cis.id \n" +
      "where cis.classroom_code = :classroomCode and sics.id_user = :studentId", nativeQuery = true)
  StudentInClassroomSubject getStudentInClassroomSubjectByClassCodeAndStudentId(String classroomCode, Long studentId);

  @Query(value = "select count(sics.id_student) from student_in_classroom_subjects sics where id_class_sbject = :idClass", nativeQuery = true)
  Long getQuantityStudentInClass(Long idClass);

  @Query(value = "select sics.id \n" +
      "from subjects s join classroom_in_subjects cis on s.id = cis.id_subject \n" +
      "join student_in_classroom_subjects sics on cis.id = sics.id_class_sbject \n" +
      "where sics.id_user = :userId and s.id = :subjectId and cis.status in (0, 1) limit 1", nativeQuery = true)
  Long getStudentInStudentClass(Long userId, Long subjectId);


  @Query(value = "select count(sics.id_user) from student_in_classroom_subjects sics where sics.id_class_sbject = :classroomId", nativeQuery = true)
  Long getQuantityStudent(Long classroomId);

  @Query(value = "select sics.*  from student_in_classroom_subjects sics join classroom_in_subjects cis on sics.id_class_sbject = cis.id \n" +
      "where sics.id = :classroomId and cis.status = 1", nativeQuery = true)
  StudentInClassroomSubject getClassroomSubjectByClassroomCodeAndStatus(Long classroomId);

  @Query(value = "select sics.id from classroom_in_subjects cis join student_in_classroom_subjects sics on cis.id = sics.id_class_sbject \n" +
      "where cis.classroom_code = :classroomCode and sics.id_user = :studentId and cis.status = 0 limit 1", nativeQuery = true)
  Long getStudentByIdAndClassroomCode(String classroomCode, Long studentId);

  @Modifying
  @Query(value = "delete from student_in_classroom_subjects where id_user = :studentId and id_class_sbject = :classSubjectId", nativeQuery = true)
  void deleteStudentInClassSubject(Long studentId, Long classSubjectId);

  @Query(value = "select * from student_in_classroom_subjects sics where sics.id_class_sbject = :classroomId", nativeQuery = true)
  List<StudentInClassroomSubject> getStudentPointByClassroomId(Long classroomId);

  @Query(value = "select sics.id_user  from classroom_in_subjects cis join student_in_classroom_subjects sics on cis.id = sics.id_class_sbject where cis.id  = :classroomId", nativeQuery = true)
  List<Long> getUserIdInClass(Long classroomId);
}
