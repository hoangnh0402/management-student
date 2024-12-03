package com.example.demo.domain.dto;

import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClassroomSubjectDTO {

  private Integer status;
  private String teacher;
  private String classroomCode;
  private String subjectName;
  private Long quantityStudent;
  private Long quantityStudentNow;
  private Integer numberOfCredits;
  private String subjectCode;
  private Long idUser;

  private Long idClassroom;
  private Integer checkStudent;
  private Long subjectId;


}
