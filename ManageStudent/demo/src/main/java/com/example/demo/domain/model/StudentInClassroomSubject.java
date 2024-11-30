package com.example.demo.domain.model;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "student_in_classroom_subjects")
@Entity
public class StudentInClassroomSubject {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", nullable = false)
  private Long id;

  @Column(name = "id_class_sbject")
  private Long idClassroomInSubject;
  @Column(name = "id_user")
  private Long idUser;
  @Column(name = "regular_point_one")
  private Double regularPointOne;
  @Column(name = "regular_point_two")
  private Double regularPointTwo;
  @Column(name = "midterm_point_one")
  private Double midtermPointOne;
  @Column(name = "test_point_one")
  private Double testPointOne;

  @Column(name = "create_user")
  private String createUser;
  @Column(name = "create_datetime")
  private LocalDateTime createDatetime;
  @Column(name = "update_user")
  private String updateUser;
  @Column(name = "update_datetime")
  private LocalDateTime updateDatetime;
}
