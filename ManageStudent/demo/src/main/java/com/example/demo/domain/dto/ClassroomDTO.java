package com.example.demo.domain.dto;

import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClassroomDTO {
  private String classroomCode;
  private String subjectName;
  private Long quantityStudent;
  private String teacherName;
  private Integer status;

}
