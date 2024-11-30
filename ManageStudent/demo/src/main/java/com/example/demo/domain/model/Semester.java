package com.example.demo.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "semesters")
public class Semester {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", nullable = false)
  private Long id;

  @Column(name = "name_semester")
  private String nameSemester;
  @Column(name = "create_user")
  private String createUser;
  @Column(name = "create_datetime")
  private LocalDateTime createDatetime;
  @Column(name = "update_user")
  private String updateUser;
  @Column(name = "update_datetime")
  private LocalDateTime updateDatetime;
}
