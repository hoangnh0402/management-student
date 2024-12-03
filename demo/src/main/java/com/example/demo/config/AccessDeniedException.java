package com.example.demo.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class AccessDeniedException extends RuntimeException {

  private static final long serialVersionUID = 1L;

  private HttpStatus status;
  private String message;

  public AccessDeniedException() {
    super("Access denied");
    this.status = HttpStatus.FORBIDDEN;
    this.message = "Access denied";
  }

}
