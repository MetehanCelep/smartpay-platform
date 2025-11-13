package com.smartpay.invoice.dto;

import java.time.LocalDateTime;

public class InvoiceResponse {

    private Long id;
    private String invoiceNumber;
    private String transactionId;
    private String merchantId;
    private String merchantEmail;
    private Double amount;
    private String status;
    private LocalDateTime createdAt;
    private String pdfPath;

    public InvoiceResponse() {}

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getInvoiceNumber() { return invoiceNumber; }
    public void setInvoiceNumber(String invoiceNumber) { this.invoiceNumber = invoiceNumber; }

    public String getTransactionId() { return transactionId; }
    public void setTransactionId(String transactionId) { this.transactionId = transactionId; }

    public String getMerchantId() { return merchantId; }
    public void setMerchantId(String merchantId) { this.merchantId = merchantId; }

    public String getMerchantEmail() { return merchantEmail; }
    public void setMerchantEmail(String merchantEmail) { this.merchantEmail = merchantEmail; }

    public Double getAmount() { return amount; }
    public void setAmount(Double amount) { this.amount = amount; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public String getPdfPath() { return pdfPath; }
    public void setPdfPath(String pdfPath) { this.pdfPath = pdfPath; }
}