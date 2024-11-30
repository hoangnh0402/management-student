package com.example.demo.controller;

import com.example.demo.domain.dto.AuthenticationResponse;
import com.example.demo.domain.dto.ClassroomDTO;
import com.example.demo.domain.dto.SubjectDTO;
import com.example.demo.domain.model.Subject;
import com.example.demo.service.SubjectService;
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
@Slf4j
@RequestMapping("/subjects")
public class SubjectController extends CommonController{
  private final SubjectService subjectService;

  public SubjectController(SubjectService subjectService) {
    this.subjectService = subjectService;
  }

  @Operation(summary = "API lấy môn học by mã môn")
  @GetMapping("/detail")
  public ResponseEntity<?> getSubjectBySubjectCode(@RequestParam(value = "subjectCode") String subjectCode){
    try {
      return toSuccessResult(subjectService.getSubjectBySubjectCode(subjectCode));
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      return toExceptionResult(e.getMessage(), RETURN_CODE_ERROR);
    }
  }

  @Operation(summary = "API lấy tất car môn học - admin")
  @GetMapping()
  public ResponseEntity<?> getAllSubject(@RequestParam(name = "subjectName" , required = false) String subjectName,
                                         @RequestParam(value = "pageIndex") Integer pageIndex,
                                         @RequestParam(value = "pageSize") Integer pageSize){
    try {
      return toSuccessResult(subjectService.getAllSubject(subjectName, pageIndex, pageSize));
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      return toExceptionResult(e.getMessage(), RETURN_CODE_ERROR);
    }
  }

  @Operation(summary = "API lấy các lớp học trong môn học - admin")
  @GetMapping("/classrooms")
  public ResponseEntity<?> getClassroomBySubjectCode(@RequestParam(value = "subjectId") Long subjectId){
    try {
      return toSuccessResult(subjectService.getClassroomBySubjectId(subjectId));
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      return toExceptionResult(e.getMessage(), RETURN_CODE_ERROR);
    }
  }

  @Operation(summary = "API lấy môn học theo subjectId")
  @GetMapping("/detail/{subjectId}")
  public ResponseEntity<?> getSubjectBySubjectId(@PathVariable Long subjectId){
    try {
      return toSuccessResult(subjectService.getSubjectBySubjectId(subjectId));
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      return toExceptionResult(e.getMessage(), RETURN_CODE_ERROR);
    }
  }

  @Operation(summary = "API tạo môn học - admin")
  @PostMapping()
  public ResponseEntity<?> createSubject(@RequestBody SubjectDTO subjectDTO){
    try {
      return toSuccessResult(subjectService.createSubject(subjectDTO));
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      return toExceptionResult(e.getMessage(), RETURN_CODE_ERROR);
    }
  }

  @Operation(summary = "API thay đổi thông tin moon học - admin")
  @PutMapping()
  public ResponseEntity<?> changeSubject(@RequestBody SubjectDTO subjectDTO, @RequestParam(value = "subjectId") Long subjectId){
    try {
      return toSuccessResult(subjectService.changeSubject(subjectDTO, subjectId));
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      return toExceptionResult(e.getMessage(), RETURN_CODE_ERROR);
    }
  }

  ///delete mon hoc
}
