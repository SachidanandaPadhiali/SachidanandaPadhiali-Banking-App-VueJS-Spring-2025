package com.banking.bank_app_java.service;

import com.banking.bank_app_java.dto.EmailDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username")
    private String senderEmail;

    @Override
    public void sendEmail(EmailDetails emailDetails) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(senderEmail);
            message.setTo(emailDetails.getRecipient());
            message.setSubject(emailDetails.getSubject());
            message.setText(emailDetails.getMsgBody());
            mailSender.send(message);
            System.out.println("Email sent Successfully");
        } catch (MailException e) {
            throw new RuntimeException(e);
        }
    }
}
