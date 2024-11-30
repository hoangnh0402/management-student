package com.example.demo.domain.model;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
@Table(name = "classroom_in_subjects")
@Entity
public class ClassroomSubject {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", nullable = false)
  private Long id;

  @Column(name = "id_subject")
  private Long idSubject;

  @Column(name = "quantity_student")
  private Long quantityStudent;
  @Column(name = "classroom_code")
  private String classroomCode;

  @Column(name = "id_user")
  private Long idUser;

  @Column(name = "type")
  private Integer type;

  @Column(name = "create_user")
  private String createUser;
  @Column(name = "create_datetime")
  private LocalDateTime createDatetime;
  @Column(name = "update_user")
  private String updateUser;
  @Column(name = "update_datetime")
  private LocalDateTime updateDatetime;
  @Column(name = "status")
  private Integer status;


}
