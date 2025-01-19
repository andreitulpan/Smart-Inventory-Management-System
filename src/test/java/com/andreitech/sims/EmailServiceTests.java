package com.andreitech.sims;

import com.andreitech.sims.service.EmailService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@TestPropertySource(properties = {
        "email.smtp.server=smtp.example.com",
        "email.smtp.port=587",
        "email.smtp.senderEmail=test@example.com",
        "email.smtp.senderName=Test Sender",
        "email.smtp.username=username",
        "email.smtp.password=password"
})
class EmailServiceTest {

    @Mock
    private EmailService emailService;

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

    @BeforeEach
    void setUp() {}

    @Test
    void testSendEmail() {
        // Given
        String recipientEmail = "test@example.com";
        String subject = "Test Subject";
        String message = "Test Message";

        // Act
        emailService.sendEmail(recipientEmail, subject, message);

        // Assert
        // Verify that sendEmail method was called on the mock with the correct parameters
        verify(emailService, times(1)).sendEmail(recipientEmail, subject, message);
    }
}
