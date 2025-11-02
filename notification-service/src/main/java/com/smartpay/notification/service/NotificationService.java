package com.smartpay.notification.service;

import com.smartpay.notification.dto.NotificationRequest;
import com.smartpay.notification.dto.NotificationResponse;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class NotificationService {

    private final EmailService emailService;
    private final SmsService smsService;

    public NotificationService(EmailService emailService, SmsService smsService) {
        this.emailService = emailService;
        this.smsService = smsService;
    }

    public NotificationResponse sendNotification(NotificationRequest request) {
        
        String notificationId = UUID.randomUUID().toString();
        
        try {
            switch (request.getType()) {
                case EMAIL:
                    emailService.sendEmail(
                        request.getRecipient(),
                        request.getSubject(),
                        request.getMessage()
                    );
                    break;
                    
                case SMS:
                    smsService.sendSms(
                        request.getRecipient(),
                        request.getMessage()
                    );
                    break;
                    
                case PUSH:
                    // Push notification implementation
                    System.out.println("=== PUSH NOTIFICATION ===");
                    System.out.println("Recipient: " + request.getRecipient());
                    System.out.println("Message: " + request.getMessage());
                    System.out.println("========================");
                    break;
                    
                default:
                    throw new IllegalArgumentException("Unsupported notification type");
            }
            
            return new NotificationResponse(
                "SUCCESS",
                "Notification sent successfully",
                notificationId
            );
            
        } catch (Exception e) {
            return new NotificationResponse(
                "FAILED",
                "Failed to send notification: " + e.getMessage(),
                notificationId
            );
        }
    }
}