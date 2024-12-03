package com.example.demo.repo;

import com.example.demo.domain.model.UserDocument;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserDocumentRepo extends JpaRepository<UserDocument, Long> {
  @Query(value = "select * from user_document ud where document_id = :documentId and user_id = :userId", nativeQuery = true)
  UserDocument getUserDocumentByDocumentIdAndUserId(Long documentId, Long userId);

  @Query(value = "select * from user_document ud where id = :userDocumentId", nativeQuery = true)
  UserDocument getUserDocumentByDocumentIdAndUserId(Long userDocumentId);

  @Query(value = "select ud.* from user_document ud join documents d on ud.document_id = d.id where ud.is_send_mail = false and ud.submit_date is null and ud.path is null and expired_date <= NOW() + INTERVAL 1 DAY", nativeQuery = true)
  List<UserDocument> getUserDocumentBySendMailAndSubmitDateAndPath();

  @Query(value = "select ud.*  from documents d join user_document ud on d.id = ud.document_id where d.id = :documentId and ud.`path` is not null and ud.submit_date is not null", nativeQuery = true)
  List<UserDocument> getListByDocumentIdAndPathAndSubmitDate(Long documentId);
}
