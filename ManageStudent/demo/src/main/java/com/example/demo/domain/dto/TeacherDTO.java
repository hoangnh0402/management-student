package com.example.demo.domain.dto;

import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TeacherDTO {
  private Long idUser;
  private String teacherName;
}
