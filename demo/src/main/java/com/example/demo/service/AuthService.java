package com.example.demo.service;

import com.example.demo.domain.dto.*;
import com.example.demo.domain.model.User;

public interface AuthService {
  AuthenticationResponse login(AuthenticationPayload payload);
  AuthenticationResponse changePassword(ChangePasswordPayload payload);

  UserDTO forgotPasswordOTP(String userName) throws Exception;

  UserDTO validOtp(String otp);

  UserDTO changePasswordWithOtp(ChangePasswordWithOTPPayload payload);
  StudentDTO getUserInfo();
}
