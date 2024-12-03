package com.example.demo.controller;

import com.example.demo.domain.dto.AuthenticationResponse;
import com.example.demo.service.ClassroomService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static com.example.demo.common.Const.RETURN_CODE_ERROR;

@RestController
@RequestMapping("/classroom")
@Slf4j
public class ClassroomController extends CommonController {
  private final ClassroomService classroomService;

  public ClassroomController(ClassroomService classroomService) {
    this.classroomService = classroomService;
  }


  @Operation(summary = "API lấy classroom by courseId - teacher - student - admin")
  @GetMapping("/course")
  public ResponseEntity<?> getClassroomByCourseId(@RequestParam(name = "courseId") Long courseId) {
    try {
      return toSuccessResult(classroomService.getClassroomByCourseId(courseId));
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      return toExceptionResult(e.getMessage(), RETURN_CODE_ERROR);
    }
  }

}
