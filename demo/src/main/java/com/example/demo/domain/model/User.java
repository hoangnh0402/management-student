package com.example.demo.domain.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "users")
@Entity
@Builder
public class User {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", nullable = false)
  private Long id;
  @Column(name = "username")
  private String username;
  @Column(name = "password")
  private String password;
  @Column(name = "id_role")
  private Long idRole;
  @Column(name = "name")
  private String name;
  @Column(name = "code")
  private Long code;
  @Column(name = "id_class")
  private Long idClass;
  @Column(name = "image")
  private String image;
  @Column(name = "email")
  private String email;
  @Column(name = "is_first_login")
  private Boolean isFirstLogin;
  @Column(name = "is_active")
  private Boolean isActive;

  @Column(name = "create_user")
  private String createUser;
  @Column(name = "create_datetime")
  private LocalDateTime createDatetime;
  @Column(name = "update_user")
  private String updateUser;
  @Column(name = "update_datetime")
  private LocalDateTime updateDatetime;

  @Column(name = "otp")
  private String otp;

  @Column(name = "otp_expired_time")
  private LocalDateTime otpExpiredTime;

  @Override
  public String toString() {
    try {
      this.password = "";

      return new ObjectMapper().writeValueAsString(this);
    } catch (JsonProcessingException e) {
      return "{}";
    }
  }
}
