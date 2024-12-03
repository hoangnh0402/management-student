package com.example.demo.controller;

import com.example.demo.domain.dto.AuthenticationResponse;
import com.example.demo.domain.dto.StudentDTO;
import com.example.demo.domain.dto.UserDTO;
import com.example.demo.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.example.demo.common.Const.RETURN_CODE_ERROR;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController extends CommonController{
  private final UserService userService;

  public UserController(UserService userService) {
    this.userService = userService;
  }

  @Operation(summary = "API lấy tất cả thng tin giáo viên - admin")
  @GetMapping()
  ResponseEntity<?> getAllTeacher(){
    try {
      return toSuccessResult(userService.getAllTeacher());
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      return toExceptionResult(e.getMessage(), RETURN_CODE_ERROR);
    }
  }

  @GetMapping("/search - admin")
  ResponseEntity<?> search(@RequestParam(name = "teacherName") String teacherName,
                           @RequestParam(value = "pageIndex") Integer pageIndex,
                           @RequestParam(value = "pageSize") Integer pageSize) {
    try {
      return toSuccessResult(userService.search(teacherName, pageIndex, pageSize));
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      return toExceptionResult(e.getMessage(), RETURN_CODE_ERROR);
    }
  }
  @Operation(summary = "API tạo mới giáo viên - admin")
  @PostMapping
  ResponseEntity<?> createUser(@RequestBody UserDTO dto) {
    try {
      return toSuccessResult(userService.createUser(dto));
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      return toExceptionResult(e.getMessage(), RETURN_CODE_ERROR);
    }
  }

  @Operation(summary = "API cập nhật thông tin giáo viên -admin")
  @PutMapping
  ResponseEntity<?> updateUser(@RequestBody UserDTO dto) {
    try {
      return toSuccessResult(userService.updateUser(dto));
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      return toExceptionResult(e.getMessage(), RETURN_CODE_ERROR);
    }
  }

  @Operation(summary = "API sinh vien dang ky tk - student")
  @PostMapping("/create-student")
  ResponseEntity<?> createStudent(@ModelAttribute StudentDTO studentDTO) {
    try {
      return toSuccessResult(userService.registerStudent(studentDTO));
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      return toExceptionResult(e.getMessage(), RETURN_CODE_ERROR);
    }
  }


}
