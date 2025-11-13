package com.smartpay.payment.service;

import java.util.UUID;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.smartpay.payment.client.FraudDetectionClient;
import com.smartpay.payment.client.FraudDetectionClient.FraudAnalysisResponse;
import com.smartpay.payment.config.RabbitMQConfig;
import com.smartpay.payment.dto.PaymentRequest;
import com.smartpay.payment.event.PaymentEvent;

@Service
public class PaymentService {

    private final FraudDetectionClient fraudDetectionClient;
    private final RabbitTemplate rabbitTemplate;

    public PaymentService(FraudDetectionClient fraudDetectionClient, RabbitTemplate rabbitTemplate) {
        this.fraudDetectionClient = fraudDetectionClient;
        this.rabbitTemplate = rabbitTemplate;
    }

    public String processPayment(PaymentRequest request) {

        String txId = UUID.randomUUID().toString();

        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();

        FraudAnalysisResponse fraudAnalysis = fraudDetectionClient.analyzeFraud(
                request.getMerchantId(),
                request.getAmount(),
                txId
        );

        System.out.println("=== FRAUD ANALYSIS ===");
        System.out.println("Transaction ID: " + txId);
        System.out.println("Fraud Score: " + fraudAnalysis.getFraudScore());
        System.out.println("Risk Level: " + fraudAnalysis.getRiskLevel());
        System.out.println("Recommendation: " + fraudAnalysis.getRecommendation());

        if ("BLOCK".equals(fraudAnalysis.getRecommendation())) {
            sendPaymentNotification(txId, request.getMerchantId(), userEmail, request.getAmount(), "BLOCKED");
            throw new RuntimeException("Transaction blocked due to high fraud risk");
        }

        if ("REVIEW".equals(fraudAnalysis.getRecommendation())) {
            System.out.println("⚠️  Transaction flagged for manual review");
        }

        System.out.println("✅ PAYMENT SUCCESS => " + txId);

        sendPaymentNotification(txId, request.getMerchantId(), userEmail, request.getAmount(), "SUCCESS");

        return txId;
    }

    private void sendPaymentNotification(String txId, String merchantId, String email, Double amount, String status) {
        try {
            PaymentEvent event = new PaymentEvent(txId, merchantId, email, amount, status);
            rabbitTemplate.convertAndSend(RabbitMQConfig.PAYMENT_EXCHANGE, "", event);
            System.out.println("📧 Payment notification sent to EXCHANGE");
        } catch (Exception e) {
            System.err.println("Failed to send payment notification: " + e.getMessage());
        }
    }
}