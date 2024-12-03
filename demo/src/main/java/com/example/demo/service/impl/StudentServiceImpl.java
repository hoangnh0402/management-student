package com.example.demo.service.impl;

import com.example.demo.domain.dto.*;
import com.example.demo.domain.model.*;
import com.example.demo.repo.*;
import com.example.demo.service.MailService;
import com.example.demo.service.StudentService;
import com.example.demo.utils.FileUtil;
import com.example.demo.utils.SecurityUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

@Transactional
@Service
public class StudentServiceImpl implements StudentService {
  private final StudentInSemesterRepo studentInSemesterRepo;
  private final ClassroomRepo classroomRepo;
  private final CourseRepo courseRepo;
  private final StudentInClassroomSubjectRepo studentInClassroomSubjectRepo;
  private final ClassroomSubjectRepo classroomSubjectRepo;
  private final SubjectRepo subjectRepo;
  private final SemesterRepo semesterRepo;
  private final StudentRepoCustom studentRepoCustom;
  private final PasswordEncoder passwordEncoder;
  private final UserRepo userRepo;
  private final MailService mailService;

  public StudentServiceImpl(StudentInSemesterRepo studentInSemesterRepo, ClassroomRepo classroomRepo, CourseRepo courseRepo, StudentInClassroomSubjectRepo studentInClassroomSubjectRepo, ClassroomSubjectRepo classroomSubjectRepo, SubjectRepo subjectRepo, SemesterRepo semesterRepo, StudentRepoCustom studentRepoCustom, PasswordEncoder passwordEncoder, UserRepo userRepo, MailService mailService) {
    this.studentInSemesterRepo = studentInSemesterRepo;
    this.classroomRepo = classroomRepo;
    this.courseRepo = courseRepo;
    this.studentInClassroomSubjectRepo = studentInClassroomSubjectRepo;
    this.classroomSubjectRepo = classroomSubjectRepo;
    this.subjectRepo = subjectRepo;
    this.semesterRepo = semesterRepo;
    this.studentRepoCustom = studentRepoCustom;
    this.passwordEncoder = passwordEncoder;
    this.userRepo = userRepo;
    this.mailService = mailService;
  }

  @Override
  public StudentPointDTO getStudentByStudentCode(Long studentCode) {
    User user = userRepo.getStudentByStudentCode(studentCode);
    Assert.notNull(user, "Student is null");
    Classroom classroom = classroomRepo.getClassroomByClassroomId(user.getIdClass());
    Course course = courseRepo.getCourseByCourseId(classroom.getIdCourse());
    Double point = studentInSemesterRepo.getAccumulatedPointsByStudentId(user.getId());
    Integer countPoint = studentInSemesterRepo.countAccumulatedPointsByStudentId(user.getId());
    return StudentPointDTO.builder()
        .studentId(user.getId())
        .studentCode(user.getCode())
        .studentName(user.getName())
        .classroomName(classroom.getNameClass())
        .courseName(course != null ? course.getNameCourse() : null)
        .accumulatedPoints(point != null ? Math.ceil(point / countPoint * 100) / 100 : null)
        .build();
  }

  @Override
  public StudentPointDTO getStudentByStudentId(Long userId) throws Exception {
    StudentPointDTO studentPointDTO = studentRepoCustom.getStudentByStudentId(userId);
    if (studentPointDTO == null){
      throw new Exception("Không có thông tin sinh viên");
    }
    return studentPointDTO;
  }

//  @Override
//  public List<StudentPointDTO> getStudentByClassroom(Long courseId, Long classroomId) {
//    Assert.notNull(classroomId, "Classroom is null");
//    Assert.notNull(courseId, "Course is null");
//    List<Student> listStudent = studentRepo.getStudentByClassroomId(classroomId);
//    Classroom classroom = classroomRepo.getClassroomByClassroomId(classroomId);
//    Course course = courseRepo.getCourseByCourseId(courseId);
//    List<StudentPointDTO> studentPointDTOList = new ArrayList<>();
//    for (Student item : listStudent) {
//      Double point = studentInSemesterRepo.getAccumulatedPointsByStudentId(item.getId());
//      StudentPointDTO studentPointDTO = StudentPointDTO.builder()
//          .studentCode(item.getStudentCode())
//          .accumulatedPoints(point)
//          .studentName(item.getStudentName())
//          .classroomName(classroom != null ? classroom.getNameClass() : null)
//          .courseName(course != null ? course.getNameCourse() : null)
//          .build();
//      studentPointDTOList.add(studentPointDTO);
//    }
//    return studentPointDTOList;
//  }

//  @Override
//  public List<StudentPointDTO> getStudentByPoint(Double pointStart, Double pointEnd) {
//    Assert.notNull(pointStart, "Point is null");
//    Assert.notNull(pointEnd, "Point is null");
//    List<StudentPointDTO> studentPointDTOList = new ArrayList<>();
//    List<StudentInSemester> studentInSemesters = studentInSemesterRepo.getStudentInSemesterByPoint(pointStart, pointEnd);
//    for (StudentInSemester studentInSemester : studentInSemesters) {
//      Student student = studentRepo.getById(studentInSemester.getStudentId());
//      Classroom classroom = classroomRepo.getClassroomByClassroomId(student.getIdClass());
//      Course course = courseRepo.getCourseByCourseId(classroom.getIdCourse());
//      boolean check = false;
//      for (StudentPointDTO item : studentPointDTOList) {
//        if (item.getStudentCode().equals(student.getStudentCode())) {
//          check = true;
//          break;
//        }
//      }
//      if (!check) {
//        StudentPointDTO studentPointDTO = StudentPointDTO.builder()
//            .studentCode(student.getStudentCode())
//            .classroomName(classroom.getNameClass())
//            .courseName(course != null ? course.getNameCourse() : null)
//            .studentName(student.getStudentName())
//            .accumulatedPoints(Math.ceil(studentInSemester.getAccumulatedPoints() * 100) / 100)
//            .build();
//        studentPointDTOList.add(studentPointDTO);
//      }
//
//    }
//    return studentPointDTOList;
//  }

  @Override
  public Page<StudentPointDTO> searchStudent(Long studentCode, Long courseId, Long classroomId, Double pointStart, Double pointEnd, Integer pageIndex, Integer pageSize) throws Exception {
    List<StudentPointDTO> listStudentPoint = studentRepoCustom.getStudent(studentCode, courseId, classroomId);
    for (int i = 0 ; i <listStudentPoint.size(); i++) {
      Double point = studentInSemesterRepo.getAccumulatedPointsByStudentId(listStudentPoint.get(i).getStudentId());
      Integer countPoint = studentInSemesterRepo.countAccumulatedPointsByStudentId(listStudentPoint.get(i).getStudentId());
      if (point != null) {
        point = Math.ceil(point / countPoint * 100) / 100;
        if (pointStart != null && pointEnd == null) {
          if (point >= pointStart) {
            listStudentPoint.get(i).setAccumulatedPoints(point);
          } else {
            listStudentPoint.remove(listStudentPoint.get(i));
            i--;
          }
        }
        if (pointStart == null && pointEnd == null) {
          listStudentPoint.get(i).setAccumulatedPoints((point));
        }
        if (pointStart == null && pointEnd != null) {
          if (point <= pointEnd) {
            listStudentPoint.get(i).setAccumulatedPoints(point);
          } else {
            listStudentPoint.remove(listStudentPoint.get(i));
            i--;
          }
        }
        if (pointStart != null && pointEnd !=null){
          if (point >= pointStart && point <= pointEnd){
            listStudentPoint.get(i).setAccumulatedPoints(point);
          } else {
            listStudentPoint.remove(listStudentPoint.get(i));
            i--;
          }
        }

      } else {
        if (pointStart != null || pointEnd != null) {
          listStudentPoint.remove(listStudentPoint.get(i));
          i--;
        }
      }

      if ( i>= 0 && listStudentPoint.get(i) != null && listStudentPoint.get(i).getStudentCode() != null){
        List<StudentSemesterDTO> studentPointDTOList = getAccumulatedPointByStudentCode(listStudentPoint.get(i).getStudentCode());
      }

    }
      Pageable pageRequest = PageRequest.of(pageIndex - 1, pageSize);
      int start = (int) pageRequest.getOffset();
      int end = Math.min(start + pageRequest.getPageSize(), listStudentPoint.size());
      List<StudentPointDTO> pageContent = listStudentPoint.subList(start, end);

      return new PageImpl<>(pageContent, pageRequest, listStudentPoint.size());

    }

    @Override
    public Page<DetailStudentDTO> getSubjectInStudent (Long studentId, Integer pageIndex, Integer pageSize){
      System.out.println("=================> Local date time" + LocalDateTime.now());
      User user = userRepo.getById(studentId);
      List<StudentInClassroomSubject> studentSubject = studentInClassroomSubjectRepo.findByStudentId(user.getId());
      List<DetailStudentDTO> detailStudentDTOList = new ArrayList<>();
      for (StudentInClassroomSubject item : studentSubject) {
        Long subjectId = studentInClassroomSubjectRepo.getIdSubject(item.getIdClassroomInSubject(), item.getIdUser());
        Subject subject = subjectRepo.findById(subjectId).get();
        String classroomCode = studentInClassroomSubjectRepo.getClassroomCode(item.getIdClassroomInSubject(), item.getIdUser());
        double mediumPoint = 0.0;
        if (item.getRegularPointOne() != null && item.getRegularPointTwo() != null && item.getMidtermPointOne() != null) {
          mediumPoint = Math.ceil(((item.getRegularPointOne() + item.getRegularPointTwo()) / 2 * subject.getCoefficientRegular()/100 + item.getMidtermPointOne() * subject.getCoefficientMid()/100) * 100) / 100;
        }
        double accumulatedPoint = 0;
        if (item.getRegularPointOne() != null && item.getRegularPointTwo() != null && item.getMidtermPointOne() != null && item.getTestPointOne() != null) {
          accumulatedPoint = Math.ceil((mediumPoint + item.getTestPointOne() * subject.getCoefficientTest()/100)  * 100) / 100;
        }
        double point = accumulatedPoint / 2.5;
        DetailStudentDTO detailStudentDTO = DetailStudentDTO.builder()
            .studentInClassroomSubject(item)
            .subjectName(subject.getSubjectName())
            .classroomCode(classroomCode)
            .mediumPoint(Math.ceil(mediumPoint))
            .accumulated_point(accumulatedPoint)
            .point(Math.ceil(point * 100) / 100)
            .semesterId(subject.getIdSemester())
            .numberOfCredits(subject.getNumberOfCredits())
            .build();
        detailStudentDTOList.add(detailStudentDTO);
      }

      Pageable pageRequest = PageRequest.of(pageIndex - 1, pageSize);
      int start = (int) pageRequest.getOffset();
      int end = Math.min(start + pageRequest.getPageSize(), detailStudentDTOList.size());
      List<DetailStudentDTO> pageContent = detailStudentDTOList.subList(start, end);

      return new PageImpl<>(pageContent, pageRequest, detailStudentDTOList.size());
    }

    @Override
    public List<StudentSemesterDTO> getAccumulatedPointByStudentCode (Long studentCode){
      User user = userRepo.getStudentByStudentCode(studentCode);
      Page<DetailStudentDTO> pageDetail = getSubjectInStudent(user.getId(), 1, 100);
      List<DetailStudentDTO> listDetail = pageDetail.getContent();
      List<Semester> semesters = semesterRepo.getAllSemester();
      List<StudentSemesterDTO> studentSemesterDTOS = new ArrayList<>();
      for (Semester semesterItem : semesters) {
        Double sum = 0.0;
        Integer sumCredits = 0;
          for (DetailStudentDTO subjectItem : listDetail){
            if (semesterItem.getId() == subjectItem.getSemesterId()){
              ClassroomSubject classroomSubject = classroomSubjectRepo.getClassroomSubjectByClassroomCodeAndStatus(subjectItem.getClassroomCode());
              if (classroomSubject != null){
                sum += subjectItem.getPoint() * subjectItem.getNumberOfCredits();
                sumCredits += subjectItem.getNumberOfCredits();
              }
            }
          }
            studentSemesterDTOS.add(StudentSemesterDTO.builder()
                .accumulatedPoint(sumCredits != 0 ? Math.ceil(sum/sumCredits *100)/100 : 0.0)
                .sumCredit(sumCredits)
                .idSemester(semesterItem.getId())
                .build());
            StudentInSemester studentInSemester = StudentInSemester.builder()
                .semesterId(semesterItem.getId())
                .accumulatedPoints(sumCredits != 0 ? Math.ceil(sum/sumCredits*100)/100 : 0.0)
                .userId(user.getId())
                .build();
            if (studentInSemesterRepo.getStudentSemesterByUserIdAnhSemesterId(user.getId(), semesterItem.getId()) != null){
              studentInSemesterRepo.deleteStudentIdAndSemesterId(semesterItem.getId(), user.getId());
            }
            studentInSemesterRepo.save(studentInSemester);

      }
      return studentSemesterDTOS;
    }

    @Override
    public User createStudent (StudentDTO studentDTO) throws Exception {
      User user = SecurityUtil.getCurrentUserLogin();
      if (user == null){
        throw new Exception(HttpStatus.UNAUTHORIZED.toString());
      }
      if (user.getIdRole() != 1){
        throw new Exception("Bạn không có quyền sử dụng chức năng này");
      }
      Assert.notNull(studentDTO.getUsername(), "Student username is null");
      Assert.notNull(studentDTO.getPassword(), "Student password is null");
      Assert.notNull(studentDTO.getStudentImage(), "Student image is null");
      Assert.notNull(studentDTO.getStudentName(), "Student name is null");
      Assert.notNull(studentDTO.getEmail(), "Student email is null");
      Assert.notNull(studentDTO.getIdClass(), "Class is null");
      Assert.notNull(studentDTO.getIdCourse(), "Course is null");
      User user1 = userRepo.getUserByUsername(studentDTO.getUsername());
      if (user1 != null) throw new Exception("Đã tồn tại username này, vui lòng đổi user name khác");
      User userNew = User.builder()
          .createDatetime(LocalDateTime.now())
          .createUser(user.getUsername())
          .password(passwordEncoder.encode(studentDTO.getPassword()))
          .username(studentDTO.getUsername())
          .idRole(3L)
          .isActive(true)
          .isFirstLogin(true)
          .image(FileUtil.saveImage(studentDTO.getStudentImage()))
          .name(studentDTO.getStudentName())
          .idClass(studentDTO.getIdClass())
          .email(studentDTO.getEmail())
          .build();

      // send email
      mailService.send(studentDTO.getEmail(), "Thông tin tài khoản", "Tài khoản của bạn đã được tạo thành công!\n" +
          "Username: " + studentDTO.getUsername() + "\n" +
          "Password: " + studentDTO.getPassword() + "\n" +
          "Vui lòng đăng nhập và thay đổi mật khẩu ngay sau khi đăng nhập thành công!");
      userRepo.save(userNew);
      userNew.setCode(studentDTO.getIdCourse() + userNew.getId());
      userRepo.save(userNew);
      return userNew;
    }

    @Override
    public User changeStudent (StudentDTO studentDTO) throws Exception {
      User user = SecurityUtil.getCurrentUserLogin();
      if (user == null){
        throw new Exception(HttpStatus.UNAUTHORIZED.toString());
      }
      Assert.notNull(studentDTO.getUsername(), "Tên đăng nhập đang null");
//      Assert.notNull(studentDTO.getStudentImage(), "Student image is null");
      Assert.notNull(studentDTO.getStudentName(), "Student name is null");
      Assert.notNull(studentDTO.getEmail(), "Student email is null");
      Assert.notNull(studentDTO.getIdClass(), "Class is null");
      Assert.notNull(studentDTO.getIdCourse(), "Course is null");

      User user1 = userRepo.getStudentByStudentCode(studentDTO.getStudentCode());

      user1.setName(studentDTO.getStudentName());
      user1.setImage(FileUtil.saveImage(studentDTO.getStudentImage()));
      user1.setIdClass(studentDTO.getIdClass());
      user1.setUpdateDatetime(LocalDateTime.now());
      user1.setUpdateUser(user.getUsername());
      user1.setEmail(studentDTO.getEmail());
      return userRepo.save(user1);
    }

  @Override
  public User changeStudentByStudent(StudentDTO studentDTO) throws Exception {
    User user = SecurityUtil.getCurrentUserLogin();
    if (user == null){
      throw new Exception(HttpStatus.UNAUTHORIZED.toString());
    }
//    Assert.notNull(studentDTO.getStudentImage(), "Student image is null");
    Assert.notNull(studentDTO.getStudentName(), "Student name is null");
    Assert.notNull(studentDTO.getEmail(), "Student email is null");
    user.setName(studentDTO.getStudentName());
    user.setEmail(studentDTO.getEmail());
    user.setImage(studentDTO.getStudentImage() != null ? FileUtil.saveImage(studentDTO.getStudentImage()) : user.getImage());
    user.setUpdateDatetime(LocalDateTime.now());
    userRepo.save(user);
    return user;
  }

  @Override
    public Page<StudentPointInClassroomDTO> viewPointInClassroom (String classroomCode, Integer pageIndex, Integer pageSize){
      List<StudentPointInClassroomDTO> listStudent = new ArrayList<>();
      List<StudentInClassroomSubject> listStudentInClass = studentInClassroomSubjectRepo.getStudentInClassroomSubjectsByClassroomCode(classroomCode);
      for (StudentInClassroomSubject item : listStudentInClass) {
        User user = userRepo.getById(item.getIdUser());
        Subject subject = subjectRepo.getSubjectByClassId(item.getIdClassroomInSubject());
        double mediumPoint = 0.0;
        if (item.getRegularPointOne() != null && item.getRegularPointTwo() != null && item.getMidtermPointOne() != null) {
          mediumPoint = ((item.getRegularPointOne() + item.getRegularPointTwo())/2 + item.getMidtermPointOne()*2)/3;
        }
        double accumulatedPoint = 0;
        if (item.getRegularPointOne() != null && item.getRegularPointTwo() != null && item.getMidtermPointOne() != null && item.getTestPointOne() != null) {
          accumulatedPoint = Math.ceil(((item.getRegularPointOne() + item.getRegularPointTwo()) / 2 * subject.getCoefficientRegular()/100 + item.getMidtermPointOne() * subject.getCoefficientMid()/100 + item.getTestPointOne() * subject.getCoefficientTest()/100)  * 100) / 100;
        }
        double point = accumulatedPoint / 2.5;
        StudentPointInClassroomDTO studentDTO = StudentPointInClassroomDTO.builder()
            .id(item.getId())
            .studentCode(user.getCode())
            .studentName(user.getName())
            .regularPointOne(item.getRegularPointOne())
            .regularPointTwo(item.getRegularPointTwo())
            .midtermPointOne(item.getMidtermPointOne())
            .testPointOne(item.getTestPointOne())
            .mediumPoint(Math.ceil(mediumPoint*100)/100)
            .accumulated_point(Math.ceil(point*100)/100)
            .point(Math.ceil(accumulatedPoint*100)/100)
            .build();
        listStudent.add(studentDTO);
      }
      Pageable pageRequest = PageRequest.of(pageIndex - 1, pageSize);
      int start = (int) pageRequest.getOffset();
      int end = Math.min(start + pageRequest.getPageSize(), listStudent.size());
      List<StudentPointInClassroomDTO> pageContent = listStudent.subList(start, end);

      return new PageImpl<>(pageContent, pageRequest, listStudent.size());
    }

  @Override
  public StudentPointInClassroomDTO sendRequestChangePointClass(String classroomCode) {
    classroomSubjectRepo.getClassroomSubjectByClassroomCodeAndStatus(classroomCode);
    return null;
  }

  @Override
    public StudentPointInClassroomDTO changePointInClassroom (StudentPointInClassroomDTO studentPointInClassroomDTO) throws Exception {
      User user = SecurityUtil.getCurrentUserLogin();
      if (user == null){
        throw new Exception(HttpStatus.UNAUTHORIZED.toString());
      }
      Long userId = classroomSubjectRepo.getTeacherId(studentPointInClassroomDTO.getId());
      if (user.getId() == userId){
        StudentInClassroomSubject studentInClassroomSubject = studentInClassroomSubjectRepo.getClassroomSubjectByClassroomCodeAndStatus(studentPointInClassroomDTO.getId());
        StudentPointInClassroomDTO studentDTO = studentPointInClassroomDTO;
        if (studentInClassroomSubject != null) {
          studentInClassroomSubject.setRegularPointOne(studentPointInClassroomDTO.getRegularPointOne());
          studentInClassroomSubject.setRegularPointTwo(studentPointInClassroomDTO.getRegularPointTwo());
          studentInClassroomSubject.setMidtermPointOne(studentPointInClassroomDTO.getMidtermPointOne());
          studentInClassroomSubject.setTestPointOne(studentPointInClassroomDTO.getTestPointOne());
          studentInClassroomSubject.setUpdateDatetime(LocalDateTime.now());
          studentInClassroomSubject.setUpdateUser(user.getUsername());
          studentInClassroomSubjectRepo.save(studentInClassroomSubject);
          Subject subject = subjectRepo.getSubjectByClassId(studentInClassroomSubject.getIdClassroomInSubject());
          double mediumPoint = 0.0;
          if (studentInClassroomSubject.getRegularPointOne() != null && studentInClassroomSubject.getRegularPointTwo() != null && studentInClassroomSubject.getMidtermPointOne() != null) {
            mediumPoint = Math.ceil(((studentInClassroomSubject.getRegularPointOne() + studentInClassroomSubject.getRegularPointTwo()) / 2 * subject.getCoefficientRegular()/100 + studentInClassroomSubject.getMidtermPointOne() * subject.getCoefficientMid()/100) * 100) / 100;
          }
          double accumulatedPoint = 0;
          if (studentInClassroomSubject.getRegularPointOne() != null && studentInClassroomSubject.getRegularPointTwo() != null && studentInClassroomSubject.getMidtermPointOne() != null && studentInClassroomSubject.getTestPointOne() != null) {
            accumulatedPoint = Math.ceil((mediumPoint + studentInClassroomSubject.getTestPointOne() * subject.getCoefficientTest()/100)  * 100) / 100;
          }
          double point = accumulatedPoint / 2.5;
          studentDTO.setRegularPointOne(studentPointInClassroomDTO.getRegularPointOne());
          studentDTO.setRegularPointTwo(studentPointInClassroomDTO.getRegularPointTwo());
          studentDTO.setMidtermPointOne(studentPointInClassroomDTO.getMidtermPointOne());
          studentDTO.setTestPointOne(studentPointInClassroomDTO.getTestPointOne());
          studentDTO.setMediumPoint(Math.ceil(mediumPoint));
          studentDTO.setAccumulated_point(Math.ceil(point));
//          studentInClassroomSubject.setUpdateDatetime(LocalDateTime.now());
//          studentInClassroomSubject.setUpdateUser(user.getUsername());
        }
        return studentDTO;
      }
      else {
        throw new Exception("Bạn không có quyền sử dụng chức năng này");
      }
    }

    @Override
    public void deleteStudentInClass (Long studentClassId){
      studentInClassroomSubjectRepo.delete(studentInClassroomSubjectRepo.getById(studentClassId));
    }

    @Override
    public LinkedHashMap<String, String> getColumnForInputPoint () {
      LinkedHashMap<String, String> fields = new LinkedHashMap<>();
      fields.put("code", "Mã sinh viên");
      fields.put("name", "Họ tên");
      fields.put("regular_point_one", "Điểm thường xuyên 1");
      fields.put("regular_point_two", "Điểm thường xuyên 2");
      fields.put("midterm_point_one", "Điểm giữa kỳ");
      fields.put("test_point_one", "Điểm thi");
      return fields;
    }

    @Override
    public LinkedHashMap<String, String> getColumnForInput () {
      LinkedHashMap<String, String> fields = new LinkedHashMap<>();
      fields.put("code", "Mã sinh viên");
      fields.put("name", "Họ tên");
      fields.put("id_course", "Khóa");
      fields.put("id_class", "Lớp");
      fields.put("image", "Ảnh");
      return fields;
    }

  @Override
  public Page<ClassroomSubjectDTO> viewSubjectClassRegister(String subjectCode, Integer pageIndex, Integer pageSize) throws Exception {
    User userStudent = SecurityUtil.getCurrentUserLogin();
    if (userStudent == null){
      throw new Exception(HttpStatus.UNAUTHORIZED.toString());
    }
    List<ClassroomSubjectDTO> listClass = new ArrayList<>();
    Subject subject = subjectRepo.getSubjectBySubjectCode(subjectCode);
    List<ClassroomSubject> classroomSubjects = classroomSubjectRepo.getByIdSubjectAndStatus(subject.getId());
    for (ClassroomSubject item: classroomSubjects) {
      List<StudentInClassroomSubject> listStudent = studentInClassroomSubjectRepo.getStudentInClassroomSubjectsByClassroomCode(item.getClassroomCode());
      StudentInClassroomSubject student = studentInClassroomSubjectRepo.getStudentInClassroomSubjectByClassCodeAndStudentId(item.getClassroomCode(), userStudent.getId());

      User userTeacher = userRepo.getById(item.getIdUser());
      ClassroomSubjectDTO classroom = ClassroomSubjectDTO.builder()
          .subjectCode(subjectCode)
          .subjectName(subject.getSubjectName())
          .numberOfCredits(subject.getNumberOfCredits())
          .classroomCode(item.getClassroomCode())
          .quantityStudent(item.getQuantityStudent())
          .quantityStudentNow(listStudent!=null ? Long.parseLong(listStudent.size()+"") : 0)
          .teacher(userTeacher!=null ? userTeacher.getName() : null)
          .status(item.getStatus())
          .checkStudent(student!= null ? 1 : 0)
          .subjectId(subject.getId())
          .build();
      listClass.add(classroom);
    }

    Pageable pageRequest = PageRequest.of(pageIndex - 1, pageSize);
    int start = (int) pageRequest.getOffset();
    int end = Math.min(start + pageRequest.getPageSize(), listClass.size());
    List<ClassroomSubjectDTO> pageContent = listClass.subList(start, end);

    return new PageImpl<>(pageContent, pageRequest, listClass.size());
  }

  @Override
  public Page<SubjectDTO> viewSubjectRegister(String subjectName, Integer pageIndex, Integer pageSize) {
    List<Subject> subjects ;
    List<SubjectDTO> listClass = new ArrayList<>();
    if (subjectName != null){
      subjects = subjectRepo.getSubjectBySubjectName(subjectName);
    } else {
      subjects = subjectRepo.findAll();
    }
    for (Subject item: subjects) {
      ClassroomSubject classroomSubjects = classroomSubjectRepo.getByIdAndStatus(item.getId());
      SubjectDTO subject = SubjectDTO.builder()
          .subjectCode(item.getSubjectCode())
          .subjectName(item.getSubjectName())
          .idSemester(item.getIdSemester())
          .numberOfCredits(item.getNumberOfCredits())
          .status(classroomSubjects!=null ? 0 : -1)
          .build();
      listClass.add(subject);
    }
    Pageable pageRequest = PageRequest.of(pageIndex - 1, pageSize);
    int start = (int) pageRequest.getOffset();
    int end = Math.min(start + pageRequest.getPageSize(), listClass.size());
    List<SubjectDTO> pageContent = listClass.subList(start, end);

    return new PageImpl<>(pageContent, pageRequest, listClass.size());
  }

  @Override
  public StudentInClassroomSubject registerClassSubject(String classroomCode, Long subjectId) throws Exception {
    User user = SecurityUtil.getCurrentUserLogin();
    if (user == null){
      throw new Exception(HttpStatus.UNAUTHORIZED.toString());
    }
    Long result = studentInClassroomSubjectRepo.getStudentInStudentClass(user.getId(), subjectId);
    if (result == null){
      ClassroomSubject classroomSubject = classroomSubjectRepo.getClassroomSubjectByClassroomCode(classroomCode);
      Long quantityStudentInClass = studentInClassroomSubjectRepo.getQuantityStudent(classroomSubject.getId());
      if (quantityStudentInClass < classroomSubject.getQuantityStudent()){
        StudentInClassroomSubject studentInClassroomSubject = StudentInClassroomSubject.builder()
            .idClassroomInSubject(classroomSubject.getId())
            .idUser(user.getId())
            .createUser(user.getUsername())
            .createDatetime(LocalDateTime.now())
            .createDatetime(LocalDateTime.now())
            .build();
        studentInClassroomSubjectRepo.save(studentInClassroomSubject);
        return studentInClassroomSubject;
      } else {
        throw new Exception("Lớp đã đầy.");
      }
    } else {
      throw new Exception("Bạn đang học môn học này hoặc đã đăng ký môn học này");
    }
  }

  @Override
  public Boolean cancelRegisterClassSubject(String classroomCode, Long subjectId) throws Exception {
    User user = SecurityUtil.getCurrentUserLogin();
    if (user == null){
      throw new Exception(HttpStatus.UNAUTHORIZED.toString());
    }
    Long result = studentInClassroomSubjectRepo.getStudentByIdAndClassroomCode(classroomCode, user.getId());
    if (result != null){
      ClassroomSubject classroomSubject = classroomSubjectRepo.getClassroomSubjectByClassroomCode(classroomCode);
      studentInClassroomSubjectRepo.deleteStudentInClassSubject(user.getId(), classroomSubject.getId());
      return true;
    }
    return false;
  }
}