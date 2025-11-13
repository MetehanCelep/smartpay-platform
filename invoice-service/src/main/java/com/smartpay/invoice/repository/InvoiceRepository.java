package com.smartpay.invoice.repository;

import com.smartpay.invoice.entity.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface InvoiceRepository extends JpaRepository<Invoice, Long> {
    
    Optional<Invoice> findByInvoiceNumber(String invoiceNumber);
    
    Optional<Invoice> findByTransactionId(String transactionId);
    
    List<Invoice> findByMerchantId(String merchantId);
    
    List<Invoice> findByMerchantEmail(String merchantEmail);
    
    List<Invoice> findByStatus(String status);
}