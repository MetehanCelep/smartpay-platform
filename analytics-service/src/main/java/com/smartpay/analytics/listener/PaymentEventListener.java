package com.smartpay.analytics.listener;

import com.smartpay.analytics.entity.TransactionAnalytics;
import com.smartpay.analytics.event.PaymentEvent;
import com.smartpay.analytics.service.AnalyticsService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class PaymentEventListener {

    private final AnalyticsService analyticsService;

    public PaymentEventListener(AnalyticsService analyticsService) {
        this.analyticsService = analyticsService;
    }

    @RabbitListener(queues = "payment.events")
    public void handlePaymentEvent(PaymentEvent event) {
        System.out.println("📊 Received payment event for analytics: " + event.getTransactionId());

        TransactionAnalytics analytics = new TransactionAnalytics();
        analytics.setTransactionId(event.getTransactionId());
        analytics.setMerchantId(event.getMerchantId());
        analytics.setEmail(event.getEmail());
        analytics.setAmount(event.getAmount());
        analytics.setStatus(event.getStatus());
        analytics.setTransactionDate(LocalDateTime.now());

        analyticsService.saveTransaction(analytics);
        
        System.out.println("✅ Transaction analytics saved");
    }
}