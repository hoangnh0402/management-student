package com.example.demo.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ResponseMessage<T> {
  private String code;
  private Boolean success;
  private T data;
  private String description;
}
