package com.smartpay.notification.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendEmail(String to, String subject, String text) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("noreply@smartpay.com");
            message.setTo(to);
            message.setSubject(subject);
            message.setText(text);
            
            // In development, we just log instead of actually sending
            System.out.println("=== EMAIL SENT ===");
            System.out.println("To: " + to);
            System.out.println("Subject: " + subject);
            System.out.println("Message: " + text);
            System.out.println("==================");
            
            // Uncomment in production with real SMTP config
            // mailSender.send(message);
            
        } catch (Exception e) {
            System.err.println("Failed to send email: " + e.getMessage());
            throw new RuntimeException("Email sending failed", e);
        }
    }
}