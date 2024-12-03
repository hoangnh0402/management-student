package com.example.demo.service.impl;

import com.example.demo.domain.dto.ClassroomDTO;
import com.example.demo.domain.dto.ClassroomSubjectDTO;
import com.example.demo.domain.dto.SubjectDTO;
import com.example.demo.domain.model.ClassroomSubject;
import com.example.demo.domain.model.Subject;
import com.example.demo.domain.model.User;
import com.example.demo.repo.ClassroomSubjectRepo;
import com.example.demo.repo.SubjectRepo;
import com.example.demo.repo.UserRepo;
import com.example.demo.service.SubjectService;
import com.example.demo.utils.SecurityUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class SubjectServiceImpl implements SubjectService {
  private final SubjectRepo subjectRepo;
  private final ClassroomSubjectRepo classroomSubjectRepo;
  private final UserRepo userRepo;

  public SubjectServiceImpl(SubjectRepo subjectRepo, ClassroomSubjectRepo classroomSubjectRepo, UserRepo userRepo) {
    this.subjectRepo = subjectRepo;
    this.classroomSubjectRepo = classroomSubjectRepo;
    this.userRepo = userRepo;
  }

  @Override
  public Subject getSubjectBySubjectCode(String subjectCode) {
    Subject subject = subjectRepo.getSubjectBySubjectCode(subjectCode);
    return subject;
  }

  @Override
  public Page<Subject> getAllSubject(String subjectName , Integer pageIndex, Integer pageSize) {
    List<Subject> subjects;
    if (subjectName!=null){
       subjects = subjectRepo.getSubjectBySubjectName(subjectName);
    } else subjects = subjectRepo.findAll();
    Pageable pageRequest = PageRequest.of(pageIndex - 1, pageSize);
    int start = (int) pageRequest.getOffset();
    int end = Math.min(start + pageRequest.getPageSize(), subjects.size());
    List<Subject> pageContent = subjects.subList(start, end);

    return new PageImpl<>(pageContent, pageRequest, subjects.size());
  }

  @Override
  public List<ClassroomDTO> getClassroomBySubjectId(Long subjectId) {
    List<ClassroomDTO> classroomDTOS = new ArrayList<>();
    Subject subject = subjectRepo.getById(subjectId);
    List<ClassroomSubject> classroomSubjects = classroomSubjectRepo.getByIdSubject(subjectId);
    for (ClassroomSubject item: classroomSubjects) {
      ClassroomDTO classroomDTO = ClassroomDTO.builder()
          .classroomCode(item.getClassroomCode())
          .subjectName(subject.getSubjectName())
          .quantityStudent(item.getQuantityStudent())
          .status(item.getStatus())
          .teacherName(userRepo.findById(item.getIdUser()).get() != null ? userRepo.findById(item.getIdUser()).get().getName() : null)
          .build();
      classroomDTOS.add(classroomDTO);
    }
    return classroomDTOS;
  }

  @Override
  public Subject getSubjectBySubjectId(Long subjectId) throws Exception {
    Subject subject = subjectRepo.getSubjectBySubjectId(subjectId);
    if(subject==null){
      throw new Exception("Không tim thấy môn học");
    }
    return subject;
  }

  @Override
  public Subject createSubject(SubjectDTO subjectDTO) throws Exception {
    User user = SecurityUtil.getCurrentUserLogin();
    if (user == null){
      throw new Exception(HttpStatus.UNAUTHORIZED.toString());
    }
    if (user.getIdRole() != 1){
      throw new Exception("Bạn không có quyền sử dụng chức năng này");
    }
    Subject subject = subjectRepo.getSubjectBySubjectCode(subjectDTO.getSubjectCode());
    if (subject == null){
      Assert.notNull(subjectDTO.getSubjectCode(), "Subject code is null");
      Assert.notNull(subjectDTO.getSubjectName(), "Subject name is null");
      Assert.notNull(subjectDTO.getIdSemester(), "Semester is null");
      Assert.notNull(subjectDTO.getNumberOfCredits(), "Number of credits is null");
      Assert.notNull(subjectDTO.getCoefficientMid(), "Coefficient Mid is null");
      Assert.notNull(subjectDTO.getCoefficientRegular(), "Coefficient Regular is null");
      Assert.notNull(subjectDTO.getCoefficientTest(), "Coefficient Test is null");
      Subject subjectNew = Subject.builder()
          .subjectCode(subjectDTO.getSubjectCode())
          .subjectName(subjectDTO.getSubjectName())
          .idSemester(subjectDTO.getIdSemester())
          .numberOfCredits(subjectDTO.getNumberOfCredits())
          .coefficientRegular(subjectDTO.getCoefficientRegular())
          .coefficientMid(subjectDTO.getCoefficientMid())
          .coefficientTest(subjectDTO.getCoefficientTest())
          .createDatetime(LocalDateTime.now())
          .createUser(user.getUsername())
          .build();
      subjectRepo.save(subjectNew);
      return subjectNew;
    } else {
     throw new Exception("Môn học đã tồn tại");
    }
  }

  @Override
  public Subject changeSubject(SubjectDTO subjectDTO, Long subjectId) throws Exception {
    User user = SecurityUtil.getCurrentUserLogin();
    if (user == null){
      throw new Exception(HttpStatus.UNAUTHORIZED.toString());
    }
    if (user.getIdRole() != 1){
      throw new Exception("Bạn không có quyền sử dụng chức năng này");
    }
    Subject subject = subjectRepo.findById(subjectId).get();
    if (subject != null){
      Assert.notNull(subjectDTO.getSubjectCode(), "Subject code is null");
      Assert.notNull(subjectDTO.getSubjectName(), "Subject name is null");
      Assert.notNull(subjectDTO.getIdSemester(), "Semester is null");
      Assert.notNull(subjectDTO.getNumberOfCredits(), "Number of credits is null");
      Assert.notNull(subjectDTO.getCoefficientMid(), "Coefficient Mid is null");
      Assert.notNull(subjectDTO.getCoefficientRegular(), "Coefficient Regular is null");
      Assert.notNull(subjectDTO.getCoefficientTest(), "Coefficient Test is null");
      subject.setSubjectCode(subjectDTO.getSubjectCode());
      subject.setSubjectName(subjectDTO.getSubjectName());
      subject.setIdSemester(subjectDTO.getIdSemester());
      subject.setNumberOfCredits(subjectDTO.getNumberOfCredits());
      subject.setUpdateDatetime(LocalDateTime.now());
      subject.setUpdateUser(user.getUsername());
      subject.setCoefficientMid(subjectDTO.getCoefficientMid());
      subject.setCoefficientTest(subjectDTO.getCoefficientTest());
      subject.setCoefficientRegular(subjectDTO.getCoefficientRegular());
      subjectRepo.save(subject);
      return subject;
    } else {
      throw new Exception("Môn học chưa tồn tại");
    }
  }


}
