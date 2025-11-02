package com.smartpay.notification.dto;

public class NotificationResponse {

    private String status;
    private String message;
    private String notificationId;

    public NotificationResponse() {}

    public NotificationResponse(String status, String message, String notificationId) {
        this.status = status;
        this.message = message;
        this.notificationId = notificationId;
    }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public String getNotificationId() { return notificationId; }
    public void setNotificationId(String notificationId) { this.notificationId = notificationId; }
}