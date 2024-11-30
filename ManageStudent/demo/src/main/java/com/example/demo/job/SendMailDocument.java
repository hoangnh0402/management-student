package com.example.demo.job;

import com.example.demo.domain.model.Document;
import com.example.demo.domain.model.Subject;
import com.example.demo.domain.model.User;
import com.example.demo.repo.DocumentRepo;
import com.example.demo.repo.StudentInClassroomSubjectRepo;
import com.example.demo.repo.SubjectRepo;
import com.example.demo.repo.UserRepo;
import com.example.demo.service.MailService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class SendMailDocument {
  private final DocumentRepo documentRepo;
  private final StudentInClassroomSubjectRepo studentInClassroomSubjectRepo;
  private final UserRepo userRepo;
  private final SubjectRepo subjectRepo;
  private final MailService mailService;
  @SneakyThrows
  @Scheduled(fixedDelay = 60*1000)
  private void sendMailDocumentNew(){
    List<Document> listDocument = documentRepo.getDocumentByMailStatus();
    for (Document document: listDocument) {
      List<Long> listStudent = studentInClassroomSubjectRepo.getUserIdInClass(document.getClassroomId());
      for (Long studentId: listStudent) {
        User user = userRepo.getUserByUserId(studentId);
        Subject subject = subjectRepo.getSubjectByClassId(document.getClassroomId());
        // send email
        mailService.send(user.getEmail(), "Bài tập mới lớp " + subject.getSubjectName(), "" +
            "Bạn có một bài tập mới trong lớp  " + subject.getSubjectName() + ". Hãy kiểm tra và hoàn thành bài tập trước ngày " + document.getExpiredDate());
      }
      document.setMailStatus(true);
      documentRepo.save(document);
    }
  }
}
