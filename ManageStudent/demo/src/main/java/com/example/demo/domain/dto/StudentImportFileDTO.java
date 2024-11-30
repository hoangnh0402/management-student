package com.example.demo.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class StudentImportFileDTO {
  private String studentCode;
  private String studentName;
  private String studentImage;
  private String courseName;
  private String className;
}
