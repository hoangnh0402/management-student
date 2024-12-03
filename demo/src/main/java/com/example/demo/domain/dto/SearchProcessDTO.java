package com.example.demo.domain.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SearchProcessDTO {
  private Long id;
  private String fileName;
  private LocalDateTime createDatetime;
  private Integer status;
}
