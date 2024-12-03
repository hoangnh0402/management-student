package com.example.demo.service.impl;

import com.example.demo.service.MailService;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class MailServiceImpl implements MailService {

  private final JavaMailSender javaMailSender;

  public MailServiceImpl(JavaMailSender javaMailSender) {
    this.javaMailSender = javaMailSender;
  }

  @Override
  public void send(String to, String subject, String content) {
    try {
      SimpleMailMessage simpleMailMessage = new SimpleMailMessage();

      simpleMailMessage.setSubject(subject);
      simpleMailMessage.setText(content);
      simpleMailMessage.setTo(to);

      javaMailSender.send(simpleMailMessage);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

}
