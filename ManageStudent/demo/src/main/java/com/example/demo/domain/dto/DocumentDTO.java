package com.example.demo.domain.dto;

import com.example.demo.domain.model.UserDocument;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class DocumentDTO {
  private Long classroomId;
  private LocalDateTime expiredDate;
  private String path;
  private String title;
  private Boolean mailStatus;
  private Integer type;

  private Integer sumStudent;
  private Integer nowStudent;
  private Long documentId;
}
