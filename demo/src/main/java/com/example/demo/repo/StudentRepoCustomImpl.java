package com.example.demo.repo;

import com.example.demo.domain.dto.StudentPointDTO;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;

@Repository
public class StudentRepoCustomImpl implements StudentRepoCustom{
  @PersistenceContext
  private EntityManager entityManager;
  @Override
  public List<StudentPointDTO> getStudent(Long studentCode, Long courseId, Long classroomId) {
    List<StudentPointDTO> listResult = new ArrayList<>();
    StringBuilder strQuery = new StringBuilder();
    strQuery.append("select u.id, u.code, u.name, c.name_class , c2.name_course , u.username , u.email , u.id_class , c2.id as id_course \n" +
        "from  users u  join classroomes c on u.id_class = c.id \n" +
        "join courses c2 on c.id_course = c2.id ");
    if (studentCode != null || courseId!=null || classroomId!=null){
      strQuery.append(" where ");
    }
    if (studentCode != null){
      strQuery.append(" u.code like CONCAT('%'," );
      strQuery.append(":studentCode,");
      strQuery.append("'%')");
    }
    if (studentCode != null && courseId != null){
      strQuery.append(" and ");
      strQuery.append(" c2.id = :courseId");
    }
    if (studentCode == null && courseId != null){
      strQuery.append(" c2.id = :courseId");
    }
    if (classroomId != null){
      strQuery.append(" and ");
      strQuery.append(" c.id = :classroomId");
    }

    Query query = entityManager.createNativeQuery(strQuery.toString());
    if (studentCode!=null){
      query.setParameter("studentCode" , studentCode);
    }

    if (courseId!=null){
      query.setParameter("courseId" , courseId);
    }

    if (classroomId!=null){
      query.setParameter("classroomId", classroomId);
    }

    List<Object[]> result = query.getResultList();
    if (result!= null){
      for (Object[] item: result) {
        StudentPointDTO student = new StudentPointDTO();
        student.setStudentId(Long.parseLong(item[0].toString()));
        student.setStudentCode(Long.parseLong(item[1].toString()));
        student.setStudentName(item[2].toString());
        student.setClassroomName(item[3].toString());
        student.setCourseName(item[4].toString());
        student.setUsername(item[5].toString());
        student.setEmail(item[6].toString());
        student.setIdClass(Long.parseLong(item[7].toString()));
        student.setIdCourse(Long.parseLong(item[8].toString()));
        listResult.add(student);
      }
    }
    return listResult;
  }

  @Override
  public StudentPointDTO getStudentByStudentId(Long userId) {
    List<StudentPointDTO> studentPointDTOList = new ArrayList<>();
    StringBuilder strQuery = new StringBuilder();
    strQuery.append("select u.id, u.code, u.name, c.name_class , c2.name_course   , u.image \n" +
        "from  users u   join classroomes c on u.id_class = c.id \n" +
        "join courses c2 on c.id_course = c2.id where u.id = :userId and u.id_role = 3");
    Query query = entityManager.createNativeQuery(strQuery.toString());
    query.setParameter("userId", userId);
    List<Object[]> result = query.getResultList();
    if (result!= null){
      for (Object[] item: result) {
        StudentPointDTO student = new StudentPointDTO();
        student.setStudentId(Long.parseLong(item[0].toString()));
        student.setStudentCode(Long.parseLong(item[1].toString()));
        student.setStudentName(item[2].toString());
        student.setClassroomName(item[3].toString());
        student.setCourseName(item[4].toString());
        student.setImage(item[5] != null ? item[5].toString() : null);
        studentPointDTOList.add(student);
      }
    }
    return studentPointDTOList.get(0);
  }
}
