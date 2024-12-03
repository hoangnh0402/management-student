package com.example.demo.service.impl;

import com.example.demo.domain.dto.DocumentDTO;
import com.example.demo.domain.dto.UserDocumentDTO;
import com.example.demo.domain.model.*;
import com.example.demo.repo.*;
import com.example.demo.service.DocumentService;
import com.example.demo.utils.FileUtil;
import com.example.demo.utils.SecurityUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class DocumentServiceImpl implements DocumentService {
  private final DocumentRepo documentRepo;
  private final ClassroomSubjectRepo classroomSubjectRepo;
  private final StudentInClassroomSubjectRepo studentInClassroomSubjectRepo;
  private final UserDocumentRepo userDocumentRepo;
  private final UserRepo userRepo;

  public DocumentServiceImpl(DocumentRepo documentRepo, ClassroomSubjectRepo classroomSubjectRepo, StudentInClassroomSubjectRepo studentInClassroomSubjectRepo, UserDocumentRepo userDocumentRepo, UserRepo userRepo) {
    this.documentRepo = documentRepo;
    this.classroomSubjectRepo = classroomSubjectRepo;
    this.studentInClassroomSubjectRepo = studentInClassroomSubjectRepo;
    this.userDocumentRepo = userDocumentRepo;
    this.userRepo = userRepo;
  }

  @Override
  public Page<DocumentDTO> getDocumentByClassroomId(String classroomCode, Integer pageIndex, Integer pageSize) {
    Assert.notNull(classroomCode, "Mã lớp null");
    ClassroomSubject classroomSubject = classroomSubjectRepo.getClassroomSubjectByClassroomCode(classroomCode);
    List<Document> listDocument = documentRepo.getDocumentsByClassroomId(classroomSubject.getId());
    List<DocumentDTO> list = new ArrayList<>();
    for (Document item: listDocument) {
      Integer studentSubmit = documentRepo.getStudentNow(classroomSubject.getId(), item.getId());
      Integer student = documentRepo.getSumStudent(classroomSubject.getId(), item.getId());
      DocumentDTO documentDTO = new DocumentDTO();
      documentDTO.setClassroomId(item.getClassroomId());
      documentDTO.setPath(item.getPath());
      documentDTO.setTitle(item.getTitle());
      documentDTO.setExpiredDate(item.getExpiredDate());
      documentDTO.setType(item.getType());
      documentDTO.setMailStatus(item.getMailStatus());
      documentDTO.setNowStudent(studentSubmit);
      documentDTO.setSumStudent(student);
      documentDTO.setDocumentId(item.getId());
      list.add(documentDTO);
    }
    Pageable pageRequest = PageRequest.of(pageIndex - 1, pageSize);
    int start = (int) pageRequest.getOffset();
    int end = Math.min(start + pageRequest.getPageSize(), list.size());
    List<DocumentDTO> pageContent = list.subList(start, end);

    return new PageImpl<>(pageContent, pageRequest, list.size());
  }

  @Override
  public Document createDocument(MultipartFile file,  String classroomCode, String expiredDate, String title, Integer type) throws Exception {
    User user = SecurityUtil.getCurrentUserLogin();
    if (user==null){
      throw new Exception(HttpStatus.UNAUTHORIZED.toString());
    }
    ClassroomSubject classroomSubject = classroomSubjectRepo.getClassroomSubjectByClassroomCode(classroomCode);
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    Assert.notNull(expiredDate, "Ngày hết hạn null");
    Document documentNew = Document.builder()
        .expiredDate(LocalDateTime.parse(expiredDate, formatter))
        .title(title)
        .classroomId(classroomSubject.getId())
        .type(type)
        .fileName(file.getOriginalFilename())
        .mailStatus(false)
        .path(FileUtil.saveDocument(file))
        .build();
    documentRepo.save(documentNew);
    List<Long> listStudent = studentInClassroomSubjectRepo.getUserIdInClass(classroomSubject.getId());
    if (type == 0){
      for (Long studentId: listStudent) {
        createUserDocument(documentNew.getId(), studentId, user.getUsername());
      }
    }
    return documentNew;
  }

  @Override
  public Document changeDocument(MultipartFile file, Long documentId, String expiredDate, String title) throws IOException {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    Document document = documentRepo.getDocumentByDocumentId(documentId);
    Assert.notNull(document, "Không tìm thấy thông tin tài liệu");
    document.setPath(file != null ? FileUtil.saveDocument(file) : document.getPath());
    document.setTitle(title);
    document.setFileName(file!=null ? file.getOriginalFilename() : document.getFileName());
    document.setExpiredDate(LocalDateTime.parse(expiredDate, formatter));
    documentRepo.save(document);
    return document;
  }

  @Override
  public UserDocument submitDocument(MultipartFile file,  Long documentId) throws Exception {
    User user = SecurityUtil.getCurrentUserLogin();
    if (user==null){
      throw new Exception(HttpStatus.UNAUTHORIZED.toString());
    }
    UserDocument userDocument = userDocumentRepo.getUserDocumentByDocumentIdAndUserId(documentId, user.getId());
    if (userDocument != null){
      userDocument.setPath(FileUtil.saveDocument(file));
      userDocument.setSubmitDate(LocalDateTime.now());
      userDocument.setFilename(file.getOriginalFilename());
      userDocument.setIsSendMail(true);
      userDocument.setUpdateUser(user.getUsername());
      userDocument.setUpdateDatetime(LocalDateTime.now());
      return userDocumentRepo.save(userDocument);
    } else throw new Exception("Không tồn tại thông tin");
  }

  @Override
  public Page<UserDocumentDTO> viewSubmitDocument(Long documentId, Integer pageIndex, Integer pageSize) {
    Document document = documentRepo.getDocumentByDocumentId(documentId);
    List<UserDocument> userDocumentList = userDocumentRepo.getListByDocumentIdAndPathAndSubmitDate(documentId);
    List<UserDocumentDTO> list = new ArrayList<>();
    for (UserDocument item: userDocumentList) {
      User user = userRepo.getUserByUserId(item.getUserId());
      UserDocumentDTO userDocumentDTO = new UserDocumentDTO();
      userDocumentDTO.setFilename(item.getFilename());
      userDocumentDTO.setSubmitDate(item.getSubmitDate());
      userDocumentDTO.setStudentName(user.getName());
      userDocumentDTO.setStudentCode(user.getCode());
      userDocumentDTO.setUserDocumentId(item.getId());
      if (document.getExpiredDate().compareTo(item.getSubmitDate()) == 1){
        userDocumentDTO.setStatus(true);
      } else userDocumentDTO.setStatus(false);
      list.add(userDocumentDTO);
    }

    Pageable pageRequest = PageRequest.of(pageIndex - 1, pageSize);
    int start = (int) pageRequest.getOffset();
    int end = Math.min(start + pageRequest.getPageSize(), list.size());
    List<UserDocumentDTO> pageContent = list.subList(start, end);

    return new PageImpl<>(pageContent, pageRequest, list.size());
  }

  public UserDocument createUserDocument(Long documentId, Long userId, String username){
    return userDocumentRepo.save(UserDocument.builder()
        .documentId(documentId)
        .userId(userId)
        .isSendMail(false)
        .createUser(username)
        .createDatetime(LocalDateTime.now())
        .build());
  }
}
