package com.example.demo.domain.model;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "process_file_import")
@Entity
@Builder
public class ProcessFileImport {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", nullable = false)
  private Long id;

  @Column(name = "type")
  private Long type;
  @Column(name = "schema")
  private String schema;
  @Column(name = "table")
  private String table;
  @Column(name = "map_field")
  private String mapField;
  @Column(name = "file_content")
  private byte[] fileContent;
  @Column(name = "status")
  private Integer status;
  @Column(name = "id_class_sbject")
  private Long classroomId;
  @Column(name = "key_request")
  private String keyRequest;
  @Column(name = "file_path")
  private String filePath;
  @Column(name = "key_response")
  private String keyResponse;
  @Column(name = "discription")
  private byte[] discription;
  @Column(name = "create_user")
  private String createUser;
  @Column(name = "create_datetime")
    private LocalDateTime createDatetime;
  @Column(name = "update_user")
  private String updateUser;
  @Column(name = "update_datetime")
  private LocalDateTime updateDatetime;
}