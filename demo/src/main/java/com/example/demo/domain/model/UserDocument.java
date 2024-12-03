package com.example.demo.domain.model;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "user_document")
@Entity
@Builder
public class UserDocument {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", nullable = false)
  private Long id;
  @Column(name = "document_id")
  private Long documentId;
  @Column(name = "path")
  private String path;
  @Column(name = "filename")
  private String filename;
  @Column(name = "submit_date")
  private LocalDateTime submitDate;
  @Column(name = "is_send_mail")
  private Boolean isSendMail;
  @Column(name = "user_id")
  private Long userId;
  @Column(name = "create_user")
  private String createUser;
  @Column(name = "create_datetime")
  private LocalDateTime createDatetime;
  @Column(name = "update_user")
  private String updateUser;
  @Column(name = "update_datetime")
  private LocalDateTime updateDatetime;
}
