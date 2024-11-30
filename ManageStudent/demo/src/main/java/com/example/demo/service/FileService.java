package com.example.demo.service;

import com.example.demo.domain.dto.InsertFieldDTO;
import com.example.demo.domain.dto.SearchProcessDTO;
import com.example.demo.domain.model.ProcessFileImport;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;

public interface FileService {
  Page<SearchProcessDTO> queryProcess(Integer pageIndex, Integer pageSize);
  Long uploadFile(MultipartFile file, byte[] fileContent, String classroomCode) throws Exception;

  boolean insertField(InsertFieldDTO insertFieldDTO, Integer typeInsert) throws Exception;
  void importFile(ProcessFileImport processFileImport) throws IOException;
}
