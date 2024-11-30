package com.example.demo.controller;

import com.example.demo.domain.dto.StudentDTO;
import com.example.demo.domain.dto.StudentPointInClassroomDTO;
import com.example.demo.service.StudentService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import static com.example.demo.common.Const.RETURN_CODE_ERROR;

@RestController
@Slf4j
@RequestMapping("/students")
public class StudentController extends CommonController {
  private final StudentService studentService;

  public StudentController(StudentService studentService) {
    this.studentService = studentService;
  }

  @Operation(summary = "API lấy sinh viên by mã sinh viên")
  @GetMapping()
  public ResponseEntity<?> getStudentByStudentCode(@RequestParam(name = "studentCode") Long studentCode) {
    try {
      return toSuccessResult(studentService.getStudentByStudentCode(studentCode));
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      return toExceptionResult(e.getMessage(), RETURN_CODE_ERROR);
    }
  }

  @Operation(summary = "API lấy sinh viên by studentId")
  @GetMapping("/view/detail")
  public ResponseEntity<?> getStudentByStudentId(@RequestParam(name = "studentId") Long studentId) {
    try {
      return toSuccessResult(studentService.getStudentByStudentId(studentId));
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      return toExceptionResult(e.getMessage(), RETURN_CODE_ERROR);
    }
  }

  @Operation(summary = "API tìm kiếm sinh viên - admin - teacher")
  @GetMapping("/search")
  public ResponseEntity<?> searchStudent(@RequestParam(name = "studentCode", required = false) Long studentCode,
                                         @RequestParam(name = "courseId", required = false) Long courseId,
                                         @RequestParam(name = "classroomId", required = false) Long classroomId,
                                         @RequestParam(name = "pointStart", required = false) Double pointStart,
                                         @RequestParam(name = "pointEnd", required = false) Double pointEnd,
                                         @RequestParam(value = "pageIndex") Integer pageIndex,
                                         @RequestParam(value = "pageSize") Integer pageSize) {
    try {
      return toSuccessResult(studentService.searchStudent(studentCode, courseId, classroomId, pointStart, pointEnd, pageIndex, pageSize));
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      return toExceptionResult(e.getMessage(), RETURN_CODE_ERROR);
    }
  }


  @Operation(summary = "API lấy tất cả môn học của sinh viên màn xem thông tin chi tiết sinh viên - admin - teacher - student")
  @GetMapping("/detail/subject")
  public ResponseEntity<?> getSubjectInStudent(@RequestParam(name = "studentId") Long studentId,
                                               @RequestParam(value = "pageIndex") Integer pageIndex,
                                               @RequestParam(value = "pageSize") Integer pageSize) {
    try {
      return toSuccessResult(studentService.getSubjectInStudent(studentId, pageIndex, pageSize));
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      return toExceptionResult(e.getMessage(), RETURN_CODE_ERROR);
    }
  }

  @Operation(summary = "API lấy điểm tích lũy của sinh vin theo kỳ học - admin - teacher - student")
  @GetMapping("/semester/accumulated_point")
  public ResponseEntity<?> getAccumulatedPointByStudentCode(@RequestParam(name = "studentCode") Long studentCode) {
    try {
      return toSuccessResult(studentService.getAccumulatedPointByStudentCode(studentCode));
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      return toExceptionResult(e.getMessage(), RETURN_CODE_ERROR);
    }
  }

  @Operation(summary = "API tạo sinh viên - admin")

  @PostMapping()
  public ResponseEntity<?> createStudent(@ModelAttribute StudentDTO studentDTO) {
    try {
      return toSuccessResult(studentService.createStudent(studentDTO));
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      return toExceptionResult(e.getMessage(), RETURN_CODE_ERROR);
    }
  }

  @Operation(summary = "API sửa thông tin sinh viên - admin")
  @PutMapping
  public ResponseEntity<?> changeStudent(@RequestPart("studentName") String studentName,
                                         @Nullable @RequestParam(value = "studentImage", required = false) MultipartFile studentImage,
                                         @RequestPart(value = "idClass") Long idClass,
                                         @RequestPart(value = "idCourse") Long idCourse,
                                         @RequestPart(value = "username") String username,
                                         @RequestPart(value = "email") String email) {
    try {
      StudentDTO studentDTO = new StudentDTO();
      studentDTO.setStudentName(studentName);
      studentDTO.setStudentImage(studentImage);
      studentDTO.setIdClass(idClass);
      studentDTO.setUsername(username);
      studentDTO.setEmail(email);
      studentDTO.setIdCourse(idCourse);
      return toSuccessResult(studentService.changeStudent(studentDTO));
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      return toExceptionResult(e.getMessage(), RETURN_CODE_ERROR);
    }
  }

  @Operation(summary = "API sửa thông tin sinh viên - student")
  @PutMapping("/me")
  public ResponseEntity<?> changeStudentByStudent(
      @RequestPart("studentName") String studentName,
      @RequestPart("email") String email,
      @RequestPart(value = "studentImage", required = false) MultipartFile studentImage
  ) {
    try {
      StudentDTO studentDTO = new StudentDTO();
      studentDTO.setStudentName(studentName);
      studentDTO.setEmail(email);
      studentDTO.setStudentImage(studentImage);

      return toSuccessResult(studentService.changeStudentByStudent(studentDTO));
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      return toExceptionResult(e.getMessage(), RETURN_CODE_ERROR);
    }
  }


  @Operation(summary = "API lấy tất cả sinh viên, điểm trong lớp học màn nhập điểm teacher ")
  @GetMapping("/view-point-class")
  public ResponseEntity<?> viewPointInClassroom(@RequestParam(name = "classroomCode") String classroomCode,
                                                @RequestParam(value = "pageIndex") Integer pageIndex,
                                                @RequestParam(value = "pageSize") Integer pageSize) {
    try {
      return toSuccessResult(studentService.viewPointInClassroom(classroomCode, pageIndex, pageSize));
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      return toExceptionResult(e.getMessage(), RETURN_CODE_ERROR);
    }
  }

  @Operation(summary = "API gui yeu cau sua diem cho admin - teacher")
  @PutMapping("/send-request/change-point-class")
  public ResponseEntity<?> sendRequestChangePointClass(@RequestParam(name = "classroomCode") String classroomCode) {
    try {
      return toSuccessResult(studentService.sendRequestChangePointClass(classroomCode));
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      return toExceptionResult(e.getMessage(), RETURN_CODE_ERROR);
    }
  }

  @Operation(summary = "API sửa điểm sinh viên trong lớp học màn nhập điểm - teacher")
  @PutMapping("/change-point-class")
  public ResponseEntity<?> changePointClass(@RequestBody StudentPointInClassroomDTO studentPointInClassroomDTO) {
    try {
      return toSuccessResult(studentService.changePointInClassroom(studentPointInClassroomDTO));
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      return toExceptionResult(e.getMessage(), RETURN_CODE_ERROR);
    }
  }


  @Operation(summary = "API lấy các field để mapping trong màn nhập điểm - teacher")
  @GetMapping("/get-column/point")
  public ResponseEntity<?> getColumnForInputPoint() {
    try {
      return toSuccessResult(studentService.getColumnForInputPoint());
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      return toExceptionResult(e.getMessage(), RETURN_CODE_ERROR);
    }
  }

  @Operation(summary = "API lấy các field để mapping trong màn theem sinh viên")
  @GetMapping("/get-column")
  public ResponseEntity<?> getColumnForInput() {
    try {
      return toSuccessResult(studentService.getColumnForInput());
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      return toExceptionResult(e.getMessage(), RETURN_CODE_ERROR);
    }
  }

  @Operation(summary = "API man dang ky lop hoc xem cac lop hoc trong mon hoc - student")
  @GetMapping("/view-subject-class/register")
  public ResponseEntity<?> viewSubjectClassRegister(@RequestParam(name = "subjectCode") String subjectCode,
                                                    @RequestParam(value = "pageIndex") Integer pageIndex,
                                                    @RequestParam(value = "pageSize") Integer pageSize) {
    try {
      return toSuccessResult(studentService.viewSubjectClassRegister(subjectCode, pageIndex, pageSize));
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      return toExceptionResult(e.getMessage(), RETURN_CODE_ERROR);
    }
  }

  @Operation(summary = "API man dang ky lop hoc xem cac mon hoc- student")
  @GetMapping("/view-subject/register")
  public ResponseEntity<?> viewSubjectRegister(@RequestParam(value = "subjectName") String subjectName,
                                               @RequestParam(value = "pageIndex") Integer pageIndex,
                                               @RequestParam(value = "pageSize") Integer pageSize) {
    try {
      return toSuccessResult(studentService.viewSubjectRegister(subjectName, pageIndex, pageSize));
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      return toExceptionResult(e.getMessage(), RETURN_CODE_ERROR);
    }
  }

  @Operation(summary = "API man dang ky lop hoc - student")
  @PostMapping("/register-class")
  public ResponseEntity<?> registerClassSubject(@RequestParam(value = "classroomCode") String classroomCode,
                                                @RequestParam(value = "subjectId") Long subjectId) {
    try {
      return toSuccessResult(studentService.registerClassSubject(classroomCode, subjectId));
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      return toExceptionResult(e.getMessage(), RETURN_CODE_ERROR);
    }
  }

  @Operation(summary = "API man huy dang ky lop hoc - student")
  @DeleteMapping("/cancel-register-class")
  public ResponseEntity<?> cancelRegisterClassSubject(@RequestParam(value = "classroomCode") String classroomCode,
                                                      @RequestParam(value = "subjectId") Long subjectId) {
    try {
      return toSuccessResult(studentService.cancelRegisterClassSubject(classroomCode, subjectId));
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      return toExceptionResult(e.getMessage(), RETURN_CODE_ERROR);
    }
  }
}
