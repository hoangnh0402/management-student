package com.example.demo.controller;

import com.example.demo.domain.dto.*;
import com.example.demo.domain.model.User;
import com.example.demo.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.example.demo.common.Const.RETURN_CODE_ERROR;

@RestController
@Slf4j
@RequestMapping("/auth")
public class AuthController extends CommonController {
  private final AuthService authService;

  public AuthController(AuthService authService) {
    this.authService = authService;
  }

  @Operation(summary = "API login - admin - teacher")
  @PostMapping("/login")
  public ResponseEntity<?> login(@RequestBody @Valid AuthenticationPayload payload) {
    AuthenticationResponse response = authService.login(payload);
    if (ObjectUtils.isEmpty(response)) {
      return toExceptionResult("Username hoặc Password sai", RETURN_CODE_ERROR);
    }
    return toSuccessResult(response);
  }

  @Operation(summary = "API login -  teacher - student")
  @PostMapping("/change-password")
  public ResponseEntity<?> changePassword(@RequestBody ChangePasswordPayload payload) {
    AuthenticationResponse response = authService.changePassword(payload);

    if (ObjectUtils.isEmpty(response)) {
      return toExceptionResult("Thay đổi mật khẩu thất bại", RETURN_CODE_ERROR);
    }
    return toSuccessResult(response);
  }

  @Operation(summary = "API forgot password otp-  teacher - student")
  @PostMapping("/forgot-password/otp")
  public ResponseEntity<?> forgotPasswordOTP(@RequestParam(name = "username") String username) {
    try {
      UserDTO userDTO = authService.forgotPasswordOTP(username);
      return toSuccessResult(userDTO);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      return toExceptionResult(e.getMessage(), RETURN_CODE_ERROR);
    }
  }

  @Operation(summary = "API valid otp -  teacher - student")
  @PostMapping("/valid-otp")
  public ResponseEntity<?> validateOtp(@RequestParam(name = "otp") String otp) {
    UserDTO response = authService.validOtp(otp);

    if (ObjectUtils.isEmpty(response)) {
      return toExceptionResult("OTP không hợp lệ!", RETURN_CODE_ERROR);
    }
    return toSuccessResult(response);
  }

  @Operation(summary = "API change password with otp -  teacher - student")
  @PostMapping("/change-password/otp")
  public ResponseEntity<?> changePasswordWithOtp(@RequestBody ChangePasswordWithOTPPayload payload) {
    UserDTO response = authService.changePasswordWithOtp(payload);

    if (ObjectUtils.isEmpty(response)) {
      return toExceptionResult("OTP không hợp lệ!", RETURN_CODE_ERROR);
    }
    return toSuccessResult(response);
  }

  @Operation(summary = "API get current user info")
  @GetMapping("/me")
  public ResponseEntity<?> getInfo(){
    StudentDTO userDTO = authService.getUserInfo();
    if (ObjectUtils.isEmpty(userDTO)) {
      return toExceptionResult("Authen", "401");
    }
    return toSuccessResult(userDTO);
  }
}
