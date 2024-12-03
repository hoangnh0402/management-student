package com.example.demo.service.impl;

import com.example.demo.domain.dto.*;
import com.example.demo.domain.model.Role;
import com.example.demo.domain.model.User;
import com.example.demo.repo.ClassroomRepo;
import com.example.demo.repo.CourseRepo;
import com.example.demo.repo.RoleRepo;
import com.example.demo.repo.UserRepo;
import com.example.demo.service.AuthService;
import com.example.demo.service.MailService;
import com.example.demo.service.MyUserDetailsService;
import com.example.demo.utils.FileUtil;
import com.example.demo.utils.JwtUtil;
import com.example.demo.utils.SecurityUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.time.LocalDateTime;

@Service
public class AuthServiceImpl implements AuthService {
  private final UserRepo userRepo;
  private final PasswordEncoder passwordEncoder;
  private final JwtUtil jwtUtil;
  private final MyUserDetailsService myUserDetailsService;
  private final RoleRepo roleRepo;
  private final MailService mailService;
  private final CourseRepo courseRepo;
  private final ClassroomRepo classroomRepo;

  public AuthServiceImpl(UserRepo userRepo, PasswordEncoder passwordEncoder, JwtUtil jwtUtil, MyUserDetailsService myUserDetailsService,
                         RoleRepo roleRepo, MailService mailService,
                         CourseRepo courseRepo,
                         ClassroomRepo classroomRepo) {
    this.userRepo = userRepo;
    this.passwordEncoder = passwordEncoder;
    this.jwtUtil = jwtUtil;
    this.myUserDetailsService = myUserDetailsService;
    this.roleRepo = roleRepo;
    this.mailService = mailService;
    this.courseRepo = courseRepo;
    this.classroomRepo = classroomRepo;
  }

  @Override
  public AuthenticationResponse login(AuthenticationPayload payload) {
    User user = userRepo.getUserByUsername(payload.getUsername());
    if (ObjectUtils.isEmpty(user)) {
      return null;
    }

    if (!passwordEncoder.matches(payload.getPassword(), user.getPassword())) {
      return null;
    }

    Role role = roleRepo.getRoleByRoleId(user.getIdRole());
    return new AuthenticationResponse(
        jwtUtil.generateToken(myUserDetailsService.loadUserByUsername(user.getUsername())),
        user, role != null ? role.getNameRole() : null
    );
  }

  @Override
  public AuthenticationResponse changePassword(ChangePasswordPayload payload) {
    User user = userRepo.getUserByUsername(SecurityUtil.getCurrentUserLogin().getUsername());
    if (ObjectUtils.isEmpty(user)) {
      return null;
    }

    user.setPassword(passwordEncoder.encode(payload.getPassword()));
    user.setIsFirstLogin(false);
    user = userRepo.save(user);

    Role role = roleRepo.getRoleByRoleId(user.getIdRole());
    return new AuthenticationResponse(
        jwtUtil.generateToken(myUserDetailsService.loadUserByUsername(user.getUsername())),
        user, role != null ? role.getNameRole() : null
    );
  }

  @Override
  public UserDTO forgotPasswordOTP(String userName) throws Exception {
    User user = userRepo.getUserByUsername(userName);

    if (ObjectUtils.isEmpty(user)) {
      throw new Exception("Tên đăng nhập không tồn tại.");
    }


    String otp = String.valueOf((int) ((Math.random()) * ((9999 - 1000) + 1) + 1000));

    user.setOtp(otp);
    user.setOtpExpiredTime(LocalDateTime.now().plusMinutes(1));

    // send email
    mailService.send(user.getEmail(), "OTP quên password ", "" +
        "Dưới đây là mã OTP để lấy lại mật khẩu, vui lòng không cung cấp cho bất kỳ ai : " + otp);
    UserDTO userDTO = new UserDTO();
    userDTO.setName(user.getName());
    userDTO.setEmail(user.getEmail());
    userDTO.setUsername(user.getUsername());

    userRepo.save(user);
    return userDTO;
  }

  @Override
  public UserDTO validOtp(String otp) {
    User user = userRepo.findByOtp(otp);
    if (ObjectUtils.isEmpty(user)) {
      return null;
    }

    if (LocalDateTime.now().isAfter(user.getOtpExpiredTime())) {
      return null;
    }

    UserDTO userDTO = new UserDTO();
    userDTO.setName(user.getName());
    userDTO.setEmail(user.getEmail());

    return userDTO;
  }

  @Override
  public UserDTO changePasswordWithOtp(ChangePasswordWithOTPPayload payload) {
    User user = userRepo.findByOtp(payload.getOtp());
    if (ObjectUtils.isEmpty(user)) {
      return null;
    }

    user.setPassword(passwordEncoder.encode(payload.getPassword()));
    user.setUpdateDatetime(LocalDateTime.now());

    userRepo.save(user);

    return new UserDTO();
  }

  @Override
  public StudentDTO getUserInfo() {
    User user = SecurityUtil.getCurrentUserLogin();
    return StudentDTO.builder()
        .studentId(user.getId())
        .studentCode(user.getCode())
        .image(user.getImage())
        .courseName(courseRepo.getCourseNameByStudentCode(user.getCode()))
        .classroomName(classroomRepo.getClassroomByClassroomId(user.getIdClass()).getNameClass())
        .email(user.getEmail())
        .username(user.getUsername())
        .studentName(user.getName()).build();
  }
}
