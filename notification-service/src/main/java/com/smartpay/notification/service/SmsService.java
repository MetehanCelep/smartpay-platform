package com.smartpay.notification.service;

import org.springframework.stereotype.Service;

@Service
public class SmsService {

    public void sendSms(String phoneNumber, String message) {
        // In production, integrate with SMS provider (Twilio, AWS SNS, etc.)
        System.out.println("=== SMS SENT ===");
        System.out.println("To: " + phoneNumber);
        System.out.println("Message: " + message);
        System.out.println("================");
    }
}