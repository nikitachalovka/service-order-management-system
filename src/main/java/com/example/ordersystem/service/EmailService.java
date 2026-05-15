package com.example.ordersystem.service;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

import java.util.Properties;

public class EmailService {

  private static final String EMAIL =
      "nikitachalovka1@gmail.com";

  private static final String PASSWORD =
      "kmrsjeqbvdmnsagn";

  public boolean sendCode(String to, String code) {

    Properties props = new Properties();

    props.put("mail.smtp.auth", "true");
    props.put("mail.smtp.starttls.enable", "true");
    props.put("mail.smtp.host", "smtp.gmail.com");
    props.put("mail.smtp.port", "587");

    Session session =
        Session.getInstance(
            props,
            new Authenticator() {
              @Override
              protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(EMAIL, PASSWORD);
              }
            }
        );

    try {
      Message message = new MimeMessage(session);
      message.setFrom(new InternetAddress(EMAIL));
      message.setRecipients(
          Message.RecipientType.TO,
          InternetAddress.parse(to)
      );
      message.setSubject("Service Order System Verification Code");
      message.setText("Your verification code: " + code);

      Transport.send(message);
      return true;

    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
  }
}