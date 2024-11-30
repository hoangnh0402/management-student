package com.example.demo.domain.dto;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentPointDTO {
  private String courseName;
  private Long studentCode;
  private String studentName;
  private String classroomName;
  private Double accumulatedPoints;

  private Long studentId;

  //create student
  private String studentImage;
  private String image;
  private Long idClass;
  private Long idCourse;
  private String username;
  private String email;
}
