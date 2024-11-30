package com.example.demo.domain.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.*;
import org.springframework.lang.Nullable;
import org.springframework.web.multipart.MultipartFile;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentDTO {
  private String courseName;
  private Long studentCode;
  private String studentName;
  private String classroomName;
  private Double accumulatedPoints;

  private Long studentId;

  //create student
  private MultipartFile studentImage;
  private Long idClass;
  private Long idCourse;

  private String username;
  private String password;
  private String email;

  private String image;
  @Override
  public String toString() {
    try {
//      this.password = "";

      return new ObjectMapper().writeValueAsString(this);
    } catch (JsonProcessingException e) {
      return "{}";
    }
  }

}
