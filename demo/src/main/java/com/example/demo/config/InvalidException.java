package com.example.demo.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class InvalidException extends RuntimeException {

  private static final long serialVersionUID = 1L;

  private HttpStatus status;
  private String message;

  public InvalidException(String message) {
    super(message);
    this.status = HttpStatus.BAD_REQUEST;
    this.message = message;
  }

  public InvalidException(HttpStatus status, String message) {
    super(message);
    this.status = status;
    this.message = message;
  }

}