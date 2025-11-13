package com.smartpay.analytics.listener;

import java.time.LocalDateTime;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import com.smartpay.analytics.config.RabbitMQConfig;
import com.smartpay.analytics.entity.TransactionAnalytics;
import com.smartpay.analytics.event.PaymentEvent;
import com.smartpay.analytics.service.AnalyticsService;

@Component
public class PaymentEventListener {

    private final AnalyticsService analyticsService;

    public PaymentEventListener(AnalyticsService analyticsService) {
        this.analyticsService = analyticsService;
    }

    @RabbitListener(queues = RabbitMQConfig.ANALYTICS_QUEUE)
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