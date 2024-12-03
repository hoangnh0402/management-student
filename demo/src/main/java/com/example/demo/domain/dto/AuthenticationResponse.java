package com.example.demo.domain.dto;

import com.example.demo.domain.model.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AuthenticationResponse {

  private String jwt;

  private User information;
  private String roleName;

}
