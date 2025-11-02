package com.smartpay.notification.dto;

public class NotificationRequest {

    private String recipient;
    private String subject;
    private String message;
    private NotificationType type;

    public enum NotificationType {
        EMAIL, SMS, PUSH
    }

    public NotificationRequest() {}

    public NotificationRequest(String recipient, String subject, String message, NotificationType type) {
        this.recipient = recipient;
        this.subject = subject;
        this.message = message;
        this.type = type;
    }

    public String getRecipient() { return recipient; }
    public void setRecipient(String recipient) { this.recipient = recipient; }

    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public NotificationType getType() { return type; }
    public void setType(NotificationType type) { this.type = type; }
}