package com.smartpay.payment.client;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Component
public class FraudDetectionClient {

    private final RestTemplate restTemplate;
    private final String fraudServiceUrl = "http://localhost:8083/api/fraud/analyze";

    public FraudDetectionClient() {
        this.restTemplate = new RestTemplate();
    }

    public FraudAnalysisResponse analyzeFraud(String merchantId, Double amount, String transactionId) {
        try {
            Map<String, Object> request = new HashMap<>();
            request.put("merchantId", merchantId);
            request.put("amount", amount);
            request.put("transactionId", transactionId);
            request.put("timestamp", Instant.now().toString());

            Map<String, Object> response = restTemplate.postForObject(
                    fraudServiceUrl,
                    request,
                    Map.class
            );

            return mapToFraudResponse(response);

        } catch (RestClientException e) {
            System.err.println("Fraud detection service unavailable: " + e.getMessage());
            // Fallback: allow transaction if fraud service is down
            return new FraudAnalysisResponse(0.0, "LOW", "APPROVE", "Fraud service unavailable");
        }
    }

    private FraudAnalysisResponse mapToFraudResponse(Map<String, Object> response) {
        if (response == null) {
            return new FraudAnalysisResponse(0.0, "LOW", "APPROVE", "No response from fraud service");
        }

        Double fraudScore = response.get("fraudScore") != null 
            ? ((Number) response.get("fraudScore")).doubleValue() 
            : 0.0;
        
        String riskLevel = (String) response.getOrDefault("riskLevel", "LOW");
        String recommendation = (String) response.getOrDefault("recommendation", "APPROVE");
        String message = "Fraud analysis completed";

        return new FraudAnalysisResponse(fraudScore, riskLevel, recommendation, message);
    }

    public static class FraudAnalysisResponse {
        private final Double fraudScore;
        private final String riskLevel;
        private final String recommendation;
        private final String message;

        public FraudAnalysisResponse(Double fraudScore, String riskLevel, String recommendation, String message) {
            this.fraudScore = fraudScore;
            this.riskLevel = riskLevel;
            this.recommendation = recommendation;
            this.message = message;
        }

        public Double getFraudScore() { return fraudScore; }
        public String getRiskLevel() { return riskLevel; }
        public String getRecommendation() { return recommendation; }
        public String getMessage() { return message; }
    }
}