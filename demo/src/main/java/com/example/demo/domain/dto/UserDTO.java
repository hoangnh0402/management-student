package com.example.demo.domain.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {

  private Long id;

  private String username;

  private String password;

  private String teacherName;

  private Long teacherCode;

  private String email;

  private Boolean isActive = true;

  private String name;

}
