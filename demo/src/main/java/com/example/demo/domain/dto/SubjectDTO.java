package com.example.demo.domain.dto;

import lombok.*;

import javax.persistence.Column;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubjectDTO {
  private Integer coefficientRegular;
  private Integer coefficientMid;
  private Integer coefficientTest;
  private String subjectCode;
  private String subjectName;;
  private Integer numberOfCredits;
  private Long idSemester;
  private Integer status;
}
