package com.example.demo.repo;

import com.example.demo.domain.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepo extends JpaRepository<Role, Long> {
  @Query(value = "select * from roles r where id = :roleId", nativeQuery = true)
  Role getRoleByRoleId(Long roleId);

  Role findByNameRole(String nameRole);
}
