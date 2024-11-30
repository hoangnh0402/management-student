package com.example.demo.domain.model;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "roles")
@Entity
public class Role {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", nullable = false)
  private Long id;

  @Column(name = "name_role")
  private String nameRole;
  @Column(name = "create_user")
  private String createUser;
  @Column(name = "create_datetime")
  private LocalDateTime createDatetime;
  @Column(name = "update_user")
  private String updateUser;
  @Column(name = "update_datetime")
  private LocalDateTime updateDatetime;
}
