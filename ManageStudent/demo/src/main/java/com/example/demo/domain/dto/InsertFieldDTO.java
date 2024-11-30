package com.example.demo.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class InsertFieldDTO {
  private Long id;
  private Map<String, String> mapFields;
}
