package com.andreitech.sims.service;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Getter
@Setter
@Service
public class EmailService {
    @Value("${email.smtp.server}")
    private String smtpServer;

    @Value("${email.smtp.port}")
    private int smtpPort;

    @Value("${email.smtp.senderEmail}")
    private String senderEmail;

    @Value("${email.smtp.senderName}")
    private String senderName;

    @Value("${email.smtp.username}")
    private String smtpUsername;

    @Value("${email.smtp.password}")
    private String smtpPassword;

    // Simulate to send an email
    public void sendEmail(String email, String subject, String message) {
        System.out.println("Email sent to: " + email + " with subject: " + subject + " and message: " + message);
    }
}
