package com.example.demo.controller;

import com.example.demo.domain.dto.AuthenticationResponse;
import com.example.demo.service.SemesterService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static com.example.demo.common.Const.RETURN_CODE_ERROR;

@RestController
@Slf4j
@RequestMapping("/semesters")
public class SemesterController extends CommonController{
  @Autowired
  private SemesterService semesterService;
  @Operation(summary = "API lấy all semester")
  @GetMapping()
  public ResponseEntity<?> getAllSemester() {
    try {
      return toSuccessResult(semesterService.getAllSemester());
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      return toExceptionResult(e.getMessage(), RETURN_CODE_ERROR);
    }
  }

}
