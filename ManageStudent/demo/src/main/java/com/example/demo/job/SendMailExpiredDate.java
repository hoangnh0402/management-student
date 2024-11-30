package com.example.demo.job;

import com.example.demo.domain.model.User;
import com.example.demo.domain.model.UserDocument;
import com.example.demo.repo.UserDocumentRepo;
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
public class SendMailExpiredDate {
  private final UserDocumentRepo userDocumentRepo;
  private final UserRepo userRepo;
  private final MailService mailService;


  @SneakyThrows
  @Scheduled(fixedDelay = 60*1000)
  private void  sendMailExpiredDate(){
    List<UserDocument> userDocumentList = userDocumentRepo.getUserDocumentBySendMailAndSubmitDateAndPath();
    for (UserDocument item: userDocumentList) {
      User user = userRepo.getUserByUserId(item.getUserId());
      mailService.send(user.getEmail(), "Hạn nộp bài tập ", "Đã sắp đến hạn nộp bài tập, bạn chưa hoàn thành bài tập, bạn cần hoàn thaành bài nộp ngay");
      item.setIsSendMail(true);
      userDocumentRepo.save(item);
    }
  }
}
