package com.example.demo.repo;

import com.example.demo.domain.dto.TeacherDTO;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;

@Repository
public class UserRepoCustomImpl implements UserRepoCustom {
  @PersistenceContext
  EntityManager entityManager;

  @Override
  public List<TeacherDTO> getAllTeacherInfo() {
    List<TeacherDTO> list = new ArrayList<>();
    StringBuilder strQuery = new StringBuilder();
    strQuery.append("select u.id , u.name , u.code \n" +
        "from users u where u.id_role  = 2 and is_active = 1");
    List<Object[]> result = entityManager.createNativeQuery(strQuery.toString()).getResultList();
    if (result != null) {
      for (Object[] item : result) {
        TeacherDTO teacherDTO = TeacherDTO.builder()
            .idUser(Long.parseLong(item[0].toString()))
            .teacherName((item[1] != null ? item[1].toString() :  " ") + " - " + (item[2]!= null ? item[2].toString() : " ")).build();
        list.add(teacherDTO);
      }
    }
    return list;
  }
}
