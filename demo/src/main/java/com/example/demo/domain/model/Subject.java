package com.example.demo.domain.model;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "subjects")
@Builder
public class Subject {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", nullable = false)
  private Long id;
  @Column(name = "id_semester")
  private Long idSemester;
  @Column(name = "subject_name")
  private String subjectName;
  @Column(name = "subject_code")
  private String subjectCode;
  @Column(name = "number_of_credits")
  private Integer numberOfCredits;
  @Column(name = "coefficient_regular")
  private Integer coefficientRegular;
  @Column(name = "coefficient_mid")
  private Integer coefficientMid;
  @Column(name = "coefficient_test")
  private Integer coefficientTest;
  @Column(name = "create_user")
  private String createUser;
  @Column(name = "create_datetime")
  private LocalDateTime createDatetime;
  @Column(name = "update_user")
  private String updateUser;
  @Column(name = "update_datetime")
  private LocalDateTime updateDatetime;

}
