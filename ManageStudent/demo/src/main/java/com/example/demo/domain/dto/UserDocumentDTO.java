package com.example.demo.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserDocumentDTO {
  private Long studentCode;
  private String studentName;
  private String filename;
  private LocalDateTime submitDate;
  private Boolean status;

  private Long userDocumentId;


}
