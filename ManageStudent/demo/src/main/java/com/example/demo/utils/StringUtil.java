package com.example.demo.utils;

import java.util.Arrays;
import java.util.stream.Collectors;

public class StringUtil {

  public static String standardizeName(String name) {
    return Arrays.stream(name.trim().toLowerCase()
            .replaceAll("[0-9]", "")
            .replaceAll("\\s+", " ")
            .split(" "))
        .map(item -> item.substring(0, 1).toUpperCase() + item.substring(1))
        .collect(Collectors.joining(" "));
  }

}
