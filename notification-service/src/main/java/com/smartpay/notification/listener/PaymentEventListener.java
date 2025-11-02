package com.smartpay.notification.listener;

import com.smartpay.notification.dto.NotificationRequest;
import com.smartpay.notification.event.PaymentEvent;
import com.smartpay.notification.service.NotificationService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class PaymentEventListener {

    private final NotificationService notificationService;

    public PaymentEventListener(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @RabbitListener(queues = "payment.events")
    public void handlePaymentEvent(PaymentEvent event) {
        System.out.println("Received payment event: " + event.getTransactionId());

        // Send email notification
        NotificationRequest emailNotification = new NotificationRequest(
            event.getEmail(),
            "Payment " + event.getStatus(),
            buildEmailMessage(event),
            NotificationRequest.NotificationType.EMAIL
        );

        notificationService.sendNotification(emailNotification);
    }

    private String buildEmailMessage(PaymentEvent event) {
        return String.format(
            "Dear Customer,\n\n" +
            "Your payment has been %s.\n\n" +
            "Transaction ID: %s\n" +
            "Amount: %.2f TL\n" +
            "Date: %s\n\n" +
            "Thank you for using SmartPay!\n\n" +
            "Best regards,\n" +
            "SmartPay Team",
            event.getStatus().toLowerCase(),
            event.getTransactionId(),
            event.getAmount(),
            event.getTimestamp()
        );
    }
}