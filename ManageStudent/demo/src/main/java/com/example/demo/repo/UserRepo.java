package com.example.demo.repo;

import com.example.demo.domain.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepo extends JpaRepository<User, Long> {
  @Query(value = "select * from users u where u.username = :userName", nativeQuery = true)
  User getUserByUsername(String userName);

  @Query(value = "select * from users u where u.id = :userId", nativeQuery = true)
  User getUserByUserId(Long userId);

  @Query(value = "select * from users u where u.name like concat('%'," + ":teacherName," + "'%') and id_role = 2", nativeQuery = true)
  List<User> getUserByName(String teacherName);

  @Query(value = "select * from users where code = :studentCode and id_role = 3 limit 1", nativeQuery = true)
  User getStudentByStudentCode(Long studentCode);

  @Query(value = "select id from users where code = :studentCode and id_role = 3 limit 1", nativeQuery = true)
  Long getStudentIdByStudentCode(Long studentCode);

  @Query(value = "select * from users u where u.otp = ?1 LIMIT 1", nativeQuery = true)
  User findByOtp(String otp);
}
