package com.example.demo.service;

import com.example.demo.domain.dto.DocumentDTO;
import com.example.demo.domain.dto.UserDocumentDTO;
import com.example.demo.domain.model.Document;
import com.example.demo.domain.model.UserDocument;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import javax.print.Doc;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

public interface DocumentService {
  Page<DocumentDTO> getDocumentByClassroomId(String classroomCode, Integer pageIndex, Integer pageSize);
  Document createDocument(MultipartFile file, String classroomCode, String expiredDate, String title, Integer type) throws Exception;

  Document changeDocument(MultipartFile file, Long documentId, String expiredDate, String title) throws IOException;

  UserDocument submitDocument(MultipartFile file, Long documentId) throws Exception;

  Page<UserDocumentDTO> viewSubmitDocument(Long documentId, Integer pageIndex, Integer pageSize);
}
