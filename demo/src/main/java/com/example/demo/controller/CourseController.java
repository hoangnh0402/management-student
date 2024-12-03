package com.example.demo.controller;

import com.example.demo.domain.dto.AuthenticationResponse;
import com.example.demo.domain.dto.CourseDTO;
import com.example.demo.service.CourseService;
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
@RequestMapping("/courses")
@Slf4j
public class CourseController extends CommonController {
  private final CourseService courseService;

  public CourseController(CourseService courseService) {
    this.courseService = courseService;
  }

  @Operation(summary = "API tất cả khóa")
  @GetMapping()
  public ResponseEntity<?> getAllCourse() {
    try {
      return toSuccessResult(courseService.getAllCourse());
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      return toExceptionResult(e.getMessage(), RETURN_CODE_ERROR);
    }
  }

  @Operation(summary = "API tạo khóa")
  @PostMapping()
  public ResponseEntity<?> createCourse(@RequestBody CourseDTO courseDTO) {
    try {
      return toSuccessResult(courseService.createCourse(courseDTO));
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      return toExceptionResult(e.getMessage(), RETURN_CODE_ERROR);
    }
  }
}
