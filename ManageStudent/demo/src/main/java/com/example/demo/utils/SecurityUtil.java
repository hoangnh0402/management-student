package com.example.demo.utils;

import com.example.demo.domain.dto.RequestHeader;
import com.example.demo.domain.model.User;
import com.example.demo.repo.UserRepo;

public final class SecurityUtil {
  private SecurityUtil() {
  }

  public static User getCurrentUserLogin() {
    RequestHeader requestHeader = BeanUtil.getBean(RequestHeader.class);
    UserRepo userRepo = BeanUtil.getBean(UserRepo.class);
    return (User) userRepo.getUserByUsername(requestHeader.getUsername());
  }

}
