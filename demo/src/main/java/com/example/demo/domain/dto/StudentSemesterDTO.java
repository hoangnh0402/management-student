package com.example.demo.domain.dto;

import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentSemesterDTO {
  private Integer sumCredit;
  private Long idSemester;
  private Double accumulatedPoint;
}
