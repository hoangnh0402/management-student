package com.example.demo.domain.model;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "documents")
@Entity
@Builder
public class Document {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", nullable = false)
  private Long id;

  @Column(name = "classroom_id")
  private Long classroomId;
  @Column(name = "expired_date")
  private LocalDateTime expiredDate;
  @Column(name = "path")
  private String path;

  @Column(name = "filename")
  private String fileName;
  @Column(name = "title")
  private String title;
  @Column(name = "mail_status")
  private Boolean mailStatus;
  @Column(name = "type")
  private Integer type;
  @Column(name = "create_user")
  private String createUser;
  @Column(name = "create_datetime")
  private LocalDateTime createDatetime;
  @Column(name = "update_user")
  private String updateUser;
  @Column(name = "update_datetime")
  private LocalDateTime updateDatetime;
}
