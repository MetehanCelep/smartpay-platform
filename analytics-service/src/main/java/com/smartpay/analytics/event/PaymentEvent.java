package com.smartpay.analytics.event;

import java.io.Serializable;

public class PaymentEvent implements Serializable {

    private String transactionId;
    private String merchantId;
    private String email;
    private Double amount;
    private String status;
    private String timestamp;

    public PaymentEvent() {}

    // Getters and Setters
    public String getTransactionId() { return transactionId; }
    public void setTransactionId(String transactionId) { this.transactionId = transactionId; }

    public String getMerchantId() { return merchantId; }
    public void setMerchantId(String merchantId) { this.merchantId = merchantId; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public Double getAmount() { return amount; }
    public void setAmount(Double amount) { this.amount = amount; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getTimestamp() { return timestamp; }
    public void setTimestamp(String timestamp) { this.timestamp = timestamp; }
}