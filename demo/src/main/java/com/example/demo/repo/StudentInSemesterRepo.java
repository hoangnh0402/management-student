package com.example.demo.repo;

import com.example.demo.domain.model.StudentInSemester;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentInSemesterRepo extends JpaRepository<StudentInSemester, Long> {
  @Query(value = "select sum(accumulated_points) from student_semester ss join student_in_classroom_subjects sics on ss.user_id = sics.id_user \n" +
      "join classroom_in_subjects cis on cis.id = sics.id_class_sbject \n" +
      "where user_id = :userId and cis.status = 2", nativeQuery = true)
  Double getAccumulatedPointsByStudentId(Long userId);

  @Query(value = "select * from student_semester where user_id = :userId and semester_id = :semesterId", nativeQuery = true)
  StudentInSemester getStudentSemesterByUserIdAnhSemesterId(Long userId, Long semesterId);

  @Query(value = "select count(*) from student_semester ss join student_in_classroom_subjects sics on ss.user_id = sics.id_user \n" +
      "join classroom_in_subjects cis on cis.id = sics.id_class_sbject \n" +
      "where user_id = :userId and cis.status = 2 and accumulated_points != 0", nativeQuery = true)
  Integer countAccumulatedPointsByStudentId(Long userId);

  @Query(value = "with datas as (\n" +
      "\tselect\n" +
      "\t\tss.student_id, \n" +
      "\t\tSUM(ss.accumulated_points)/count(ss.student_id) as trungbinh\n" +
      "\tfrom\n" +
      "\t\tstudent_semester ss inner join students s on ss.student_id = s.id \n" +
      "\twhere\n" +
      "\t\t(\n" +
      "\t\tselect\n" +
      "\t\t\tsum(ss.accumulated_points)/count(ss.accumulated_points) \n" +
      "\t\tfrom\n" +
      "\t\t\t\t\tstudent_semester ss2) >= :pointStart\n" +
      "\t\tand (\n" +
      "\t\tselect\n" +
      "\t\t\tsum(ss.accumulated_points)/count(ss.accumulated_points) \n" +
      "\t\tfrom\n" +
      "\t\t\tstudent_semester ss2) <= :pointEnd \n" +
      "\t\tand s.id_class  = :classroomId\n" +
      "\tgroup by ss.student_id\n" +
      "\torder by ss.student_id\n" +
      ")\n" +
      "select \n" +
      "\tss.id, \n" +
      "\td.trungbinh as accumulated_points,\n" +
      "\tss.create_datetime, \n" +
      "\tss.create_user, \n" +
      "\tss.semester_id, \n" +
      "\tss.student_id, \n" +
      "\tss.update_datetime, \n" +
      "\tss.update_user \n" +
      "from student_semester ss inner join datas d \n" +
      "\ton ss.student_id = d.student_id\n" +
      "order by ss.student_id ", nativeQuery = true)
  List<StudentInSemester> getStudentInSemester(Double pointStart, Double pointEnd, Long classroomId);

  @Query(value = "with datas as (\n" +
      "\tselect\n" +
      "\t\tss.student_id, \n" +
      "\t\tSUM(ss.accumulated_points)/count(ss.student_id) as trungbinh\n" +
      "\tfrom\n" +
      "\t\tstudent_semester ss \n" +
      "\twhere\n" +
      "\t\t(\n" +
      "\t\tselect\n" +
      "\t\t\tsum(ss.accumulated_points)/count(ss.accumulated_points) \n" +
      "\t\tfrom\n" +
      "\t\t\t\t\tstudent_semester ss2) >= :pointStart\n" +
      "\t\tand (\n" +
      "\t\tselect\n" +
      "\t\t\tsum(ss.accumulated_points)/count(ss.accumulated_points) \n" +
      "\t\tfrom\n" +
      "\t\t\tstudent_semester ss2) <= :pointEnd \n" +
      "\tgroup by ss.student_id\n" +
      "\torder by ss.student_id\n" +
      ")\n" +
      "select \n" +
      "\tss.id, \n" +
      "\td.trungbinh as accumulated_points,\n" +
      "\tss.create_datetime, \n" +
      "\tss.create_user, \n" +
      "\tss.semester_id, \n" +
      "\tss.student_id, \n" +
      "\tss.update_datetime, \n" +
      "\tss.update_user \n" +
      "from student_semester ss inner join datas d \n" +
      "\ton ss.student_id = d.student_id\n" +
      "order by ss.student_id ", nativeQuery = true)
  List<StudentInSemester> getStudentInSemesterByPoint(Double pointStart, Double pointEnd);

  @Query("select s from StudentInSemester s where s.semesterId = ?1")
  StudentInSemester findBySemesterId();

  @Modifying
  @Query(value = "delete from student_semester where semester_id = :idSemester and user_id = :idStudent", nativeQuery = true)
  void deleteStudentIdAndSemesterId(Long idSemester, Long idStudent);

}
