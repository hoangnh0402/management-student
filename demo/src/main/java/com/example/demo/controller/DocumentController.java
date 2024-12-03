package com.example.demo.controller;

import com.example.demo.domain.model.Document;
import com.example.demo.domain.model.UserDocument;
import com.example.demo.repo.DocumentRepo;
import com.example.demo.repo.UserDocumentRepo;
import com.example.demo.service.DocumentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import static com.example.demo.common.Const.RETURN_CODE_ERROR;

@RestController
@RequestMapping("/document")
@Slf4j
public class DocumentController extends CommonController{
  private final DocumentService documentService;
  private final DocumentRepo documentRepo;
  private final UserDocumentRepo userDocumentRepo;

  public DocumentController(DocumentService documentService, DocumentRepo documentRepo, UserDocumentRepo userDocumentRepo) {
    this.documentService = documentService;
    this.documentRepo = documentRepo;
    this.userDocumentRepo = userDocumentRepo;
  }

  @Operation(summary = "API tất cả document by classroomId")
  @GetMapping()
  public ResponseEntity<?> getDocumentByClassroomId(@RequestParam(name = "classroomCode") String classroomCode,
                                                    @RequestParam(name = "pageIndex") Integer pageIndex,
                                                    @RequestParam(name = "pageSize") Integer pageSize) {
    try {
      return toSuccessResult(documentService.getDocumentByClassroomId(classroomCode, pageIndex, pageSize));
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      return toExceptionResult(e.getMessage(), RETURN_CODE_ERROR);
    }
  }

  @Operation(summary = "API download document")
  @GetMapping("/download-document")
  public ResponseEntity<?> downloadDocument(@RequestParam("documentId") Long documentId) {
    try {
      Document document = documentRepo.getDocumentByDocumentId(documentId);
      Assert.notNull(document, "File download does not exist");

      File file = new File(document.getPath());
      if (!file.exists()) {
        throw new FileNotFoundException("File not found: " + document.getPath());
      }

      HttpHeaders httpHeaders = new HttpHeaders();
      httpHeaders.setContentType(MediaType.APPLICATION_OCTET_STREAM);
      httpHeaders.setContentDispositionFormData("attachment", document.getPath());
      httpHeaders.setContentLength(file.length());

      InputStreamResource resource = new InputStreamResource(new FileInputStream(file));

      return ResponseEntity.ok()
          .headers(httpHeaders)
          .body(resource);
    } catch (FileNotFoundException ex) {
      log.error("File not found: " + ex.getMessage(), ex);
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("File not found");
    } catch (Exception ex) {
      log.error(ex.getMessage(), ex);
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred while downloading the file");
    }
  }

  @Operation(summary = "API tạo document trong classroomId")
  @PostMapping()
  public ResponseEntity<?> createDocumentInClassroomId(@RequestBody MultipartFile file,
                                                       @RequestParam(name = "classroomCode") String classroomCode,
                                                       @RequestParam(name = "expiredDate", required = false) String expiredDate,
                                                       @RequestParam(name = "title") String title,
                                                       @RequestParam(name = "type") Integer type) {
    try {
      return toSuccessResult(documentService.createDocument(file, classroomCode, expiredDate, title, type));
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      return toExceptionResult(e.getMessage(), RETURN_CODE_ERROR);
    }
  }

  @Operation(summary = "API sửa document trong classroomId")
  @PutMapping()
  public ResponseEntity<?> changeDocumentInClassroomId(@RequestBody (required = false) MultipartFile file,
                                                       @RequestParam(name = "documentId") Long documentId,
                                                       @RequestParam(name = "expiredDate") String expiredDate,
                                                       @RequestParam(name = "title") String title) {
    try {
      return toSuccessResult(documentService.changeDocument(file, documentId, expiredDate, title));
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      return toExceptionResult(e.getMessage(), RETURN_CODE_ERROR);
    }
  }

  @Operation(summary = "API nộp bài tập")
  @PostMapping("/submit")
  public ResponseEntity<?> submitDocument(@RequestBody MultipartFile file,
                                                       @RequestParam(name = "documentId") Long documentId) {
    try {
      return toSuccessResult(documentService.submitDocument(file, documentId));
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      return toExceptionResult(e.getMessage(), RETURN_CODE_ERROR);
    }
  }

  @Operation(summary = "API download baif lamf cua sinh vien")
  @GetMapping("/download-assignment")
  public ResponseEntity<?> downloadAssignmentt(@RequestParam("userDocumentId") Long userDocumentId) {
    try {
      UserDocument document = userDocumentRepo.getUserDocumentByDocumentIdAndUserId(userDocumentId);
      Assert.notNull(document, "File download does not exist");

      File file = new File(document.getPath());
      if (!file.exists()) {
        throw new FileNotFoundException("File not found: " + document.getPath());
      }

      HttpHeaders httpHeaders = new HttpHeaders();
      httpHeaders.setContentType(MediaType.APPLICATION_OCTET_STREAM);
      httpHeaders.setContentDispositionFormData("attachment", document.getPath());
      httpHeaders.setContentLength(file.length());

      InputStreamResource resource = new InputStreamResource(new FileInputStream(file));

      return ResponseEntity.ok()
          .headers(httpHeaders)
          .body(resource);
    } catch (FileNotFoundException ex) {
      log.error("File not found: " + ex.getMessage(), ex);
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("File not found");
    } catch (Exception ex) {
      log.error(ex.getMessage(), ex);
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred while downloading the file");
    }
  }

  @Operation(summary = "API xem bài tập sinh vien da nop")
  @GetMapping("/view-submit")
  public ResponseEntity<?> viewSubmitDocument(@RequestParam(name = "documentId") Long documentId,
                                              @RequestParam(name = "pageIndex") Integer pageIndex,
                                              @RequestParam(name = "pageSize") Integer pageSize) {
    try {
      return toSuccessResult(documentService.viewSubmitDocument(documentId, pageIndex, pageSize));
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      return toExceptionResult(e.getMessage(), RETURN_CODE_ERROR);
    }
  }
}
