package com.smartpay.analytics.dto;

public class AnalyticsReport {

    private Long totalTransactions;
    private Long successfulTransactions;
    private Long blockedTransactions;
    private Double totalAmount;
    private Double averageAmount;
    private Double successRate;

    public AnalyticsReport() {}

    // Getters and Setters
    public Long getTotalTransactions() { return totalTransactions; }
    public void setTotalTransactions(Long totalTransactions) { this.totalTransactions = totalTransactions; }

    public Long getSuccessfulTransactions() { return successfulTransactions; }
    public void setSuccessfulTransactions(Long successfulTransactions) { this.successfulTransactions = successfulTransactions; }

    public Long getBlockedTransactions() { return blockedTransactions; }
    public void setBlockedTransactions(Long blockedTransactions) { this.blockedTransactions = blockedTransactions; }

    public Double getTotalAmount() { return totalAmount; }
    public void setTotalAmount(Double totalAmount) { this.totalAmount = totalAmount; }

    public Double getAverageAmount() { return averageAmount; }
    public void setAverageAmount(Double averageAmount) { this.averageAmount = averageAmount; }

    public Double getSuccessRate() { return successRate; }
    public void setSuccessRate(Double successRate) { this.successRate = successRate; }
}