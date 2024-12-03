package com.example.demo.config;

import java.util.List;

public class DataUtil {
  private static final List<String> numbericType = List.of("int", "bool", "float", "double", "bit", "num", "fix", "dec");

  public static boolean validateColumnType(String type){
    for (String number : numbericType){
      if (type.contains(number)){
        return true;
      }
    }
    return  false;
  }
}
