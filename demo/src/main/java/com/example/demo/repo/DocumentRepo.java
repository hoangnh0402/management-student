package com.example.demo.repo;

import com.example.demo.domain.model.Document;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DocumentRepo extends JpaRepository<Document, Long> {
  @Query(value = "select * from documents s where s.classroom_id = :classroomId", nativeQuery = true)
  List<Document> getDocumentsByClassroomId(Long classroomId);

  @Query(value = "select * from documents s where s.id = :documentId", nativeQuery = true)
  Document getDocumentByDocumentId(Long documentId);

  @Query(value = "select * from documents s where s.mail_status = false and type = 0", nativeQuery = true)
  List<Document> getDocumentByMailStatus();

  @Query(value = "select count(ud.`path`)  from documents d join user_document ud on d.id = ud.document_id where d.classroom_id = :classroomId and d.id = :documentId", nativeQuery = true)
  Integer getStudentNow(Long classroomId, Long documentId);

  @Query(value = "select count(ud.user_id)  from documents d join user_document ud on d.id = ud.document_id where d.classroom_id = :classroomId and d.id = :documentId", nativeQuery = true)
  Integer getSumStudent(Long classroomId, Long documentId);
}
