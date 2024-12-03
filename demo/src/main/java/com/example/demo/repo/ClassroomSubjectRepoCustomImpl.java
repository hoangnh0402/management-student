package com.example.demo.repo;

import com.example.demo.domain.dto.ClassroomSubjectDTO;
import org.springframework.stereotype.Repository;

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Repository
public class ClassroomSubjectRepoCustomImpl implements ClassroomSubjectRepoCustom{
  @PersistenceContext
  EntityManager entityManager;
  @Override
  public List<ClassroomSubjectDTO> getAllClassroomSubject(Long userId) {
    List<ClassroomSubjectDTO> list = new ArrayList<>();
    StringBuilder strQuery = new StringBuilder();
    strQuery.append("select\n" +
        "\tclassroom_code, s.subject_name \n" +
        "from\n" +
        "\tsubjects s\n" +
        "inner join classroom_in_subjects cis on\n" +
        "\ts.id = cis.id_subject\n" +
        "inner join users u on\n" +
        "\tcis.id_user = u.id\n" +
        "\twhere cis.id_user = ").append(userId);
    List<Object[]> result = entityManager.createNativeQuery(strQuery.toString()).getResultList();
    if (result!=null){
      for (Object[] item: result) {
        ClassroomSubjectDTO classroomSubjectDTO = ClassroomSubjectDTO.builder()
            .classroomCode(item[0] != null ? item[0].toString() : null)
            .subjectName(item[1] != null ? item[1].toString() : null)
            .build();
        list.add(classroomSubjectDTO);
      }
    }
    return list;
  }

  @Override
  public List<ClassroomSubjectDTO> getAllClassroomSubjectDetail(Long subjectId, String classroomCode, Long userId) {
    List<ClassroomSubjectDTO> list = new ArrayList<>();
    StringBuilder strQuery = new StringBuilder();
    strQuery.append("select\n" +
        "\tu.name, classroom_code, s.subject_name, cis.quantity_student, u.id, cis.id as classroomId , cis.status  \n" +
        "from\n" +
        "\tsubjects s\n" +
        "inner join classroom_in_subjects cis on\n" +
        "\ts.id = cis.id_subject\n" +
        "inner join users u on\n" +
        "\tcis.id_user = u.id ");

    if (userId != null){
      strQuery.append(" where u.id = :userId");
    }
    if (subjectId != null){
      strQuery.append(" where s.id = :subjectId");
    }

    if (classroomCode != null && subjectId != null ){
      strQuery.append("  and cis.classroom_code = :classroomCode");
    }
    Query query = entityManager.createNativeQuery(strQuery.toString());
    if (userId != null){
      query.setParameter("userId", userId);
    }
    if (subjectId != null){
      query.setParameter("subjectId", subjectId);
    }

    if (classroomCode != null){
      query.setParameter("classroomCode", classroomCode);
    }
    List<Object[]> result = query.getResultList();
    if (result!=null){
      for (Object[] item: result) {
        ClassroomSubjectDTO classroomSubjectDTO = ClassroomSubjectDTO.builder()
            .teacher(item[0] != null ? item[0].toString() : null)
            .classroomCode(item[1] != null ? item[1].toString() : null)
            .subjectName(item[2] != null ? item[2].toString() : null)
            .quantityStudent(item[3] != null ? Long.parseLong(item[3].toString()) : null)
            .idUser(item[4] != null ? Long.parseLong(item[4].toString()) : null)
            .idClassroom(item[5] != null ? Long.parseLong(item[5].toString()) : null)
            .status(item[6] != null ? Integer.parseInt(item[6].toString()) : null)
            .build();
        list.add(classroomSubjectDTO);
      }
    }
    return list;
  }

  @Override
  public List<ClassroomSubjectDTO> getClassroomSubject(String classroomCode) {
    List<ClassroomSubjectDTO> list = new ArrayList<>();
    StringBuilder strQuery = new StringBuilder();
    strQuery.append("select cis.classroom_code , s.subject_name from classroom_in_subjects cis join subjects s on cis.id_subject = s.id  where classroom_code = :classroomCode");
    Query query = entityManager.createNativeQuery(strQuery.toString());
    query.setParameter("classroomCode", classroomCode);
    List<Object[]> result = query.getResultList();
    if (result!=null){
      for (Object[] item: result) {
        ClassroomSubjectDTO classroomSubjectDTO = ClassroomSubjectDTO.builder()
            .classroomCode(item[0] != null ? item[0].toString() : null)
            .subjectName(item[1] != null ? item[1].toString() : null)
            .build();
        list.add(classroomSubjectDTO);
      }
    }
    return list;
  }

  @Override
  public List<ClassroomSubjectDTO> getClassroomSubjectBySubjectNameAndStatus(String subjectName, Integer status, Long userId) {
    List<ClassroomSubjectDTO> listResult = new ArrayList<>();
    StringBuilder str = new StringBuilder();
    str.append("select cis.id, cis.classroom_code , s.subject_name , cis.quantity_student , cis.status from users u join classroom_in_subjects cis on u.id = cis.id_user join subjects s on s.id = cis.id_subject ");
    if (subjectName.trim() != null || status != null || userId != null){
      str.append("  where  ");
    }
    if (subjectName.trim() != null){
      str.append(" s.subject_name like CONCAT('%'," );
      str.append(":subjectName,");
      str.append("'%')");
    }
    if (status != null && subjectName.trim() == null){
      str.append(" cis.status = :status ");
    }
    if (status != null && subjectName.trim()!= null){
      str.append(" and ");
      str.append(" cis.status = :status ");
    }
    if ((userId != null && subjectName.trim() != null) || userId!= null && status != null){
      str.append(" and ");
      str.append(" u.id = :userId ");
    } else str.append(" u.id = :userId ");

    Query query = entityManager.createNativeQuery(str.toString());
    if (subjectName!=null){
      query.setParameter("subjectName", subjectName);
    }
    if (status != null){
      query.setParameter("status", status);
    }
    if (userId!=null){
      query.setParameter("userId", userId);
    }
    List<Objects[]> result = query.getResultList();
    if (result!=null){
      for (Object[] item: result) {
        ClassroomSubjectDTO classroomSubjectDTO = ClassroomSubjectDTO.builder()
            .idClassroom(item[0] != null ? Long.parseLong(item[0].toString()) : null)
            .classroomCode(item[1] != null ? item[1].toString() : null)
            .subjectName(item[2] != null ? item[2].toString() : null)
            .quantityStudent(item[3] != null ? Long.parseLong(item[3].toString()) : null)
            .status(item[4] != null ? Integer.parseInt(item[4].toString()) : null)
            .build();
        listResult.add(classroomSubjectDTO);
      }
    }
    return listResult;
  }
}
