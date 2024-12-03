package com.example.demo.domain.dto;

import com.example.demo.domain.model.StudentInClassroomSubject;
import lombok.*;

import javax.persistence.Column;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DetailStudentDTO {
  private String subjectName;
  private String classroomCode;
  private StudentInClassroomSubject studentInClassroomSubject;

  private Double accumulated_point;
  private Double point;
  private Double mediumPoint;
  private Long semesterId;
  private Integer numberOfCredits;
}
