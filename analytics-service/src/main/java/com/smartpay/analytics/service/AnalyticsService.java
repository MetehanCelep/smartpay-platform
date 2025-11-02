package com.smartpay.analytics.service;

import com.smartpay.analytics.dto.AnalyticsReport;
import com.smartpay.analytics.entity.TransactionAnalytics;
import com.smartpay.analytics.repository.TransactionAnalyticsRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AnalyticsService {

    private final TransactionAnalyticsRepository repository;

    public AnalyticsService(TransactionAnalyticsRepository repository) {
        this.repository = repository;
    }

    public TransactionAnalytics saveTransaction(TransactionAnalytics transaction) {
        return repository.save(transaction);
    }

    public List<TransactionAnalytics> getAllTransactions() {
        return repository.findAll();
    }

    public List<TransactionAnalytics> getTransactionsByMerchant(String merchantId) {
        return repository.findByMerchantId(merchantId);
    }

    public List<TransactionAnalytics> getTransactionsByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return repository.findByDateRange(startDate, endDate);
    }

    public AnalyticsReport generateReport() {
        AnalyticsReport report = new AnalyticsReport();

        Long total = repository.count();
        Long successful = repository.countByStatus("SUCCESS");
        Long blocked = repository.countByStatus("BLOCKED");
        Double totalAmount = repository.getTotalSuccessfulAmount();
        Double avgAmount = repository.getAverageTransactionAmount();

        report.setTotalTransactions(total);
        report.setSuccessfulTransactions(successful);
        report.setBlockedTransactions(blocked);
        report.setTotalAmount(totalAmount != null ? totalAmount : 0.0);
        report.setAverageAmount(avgAmount != null ? avgAmount : 0.0);
        
        if (total > 0) {
            report.setSuccessRate((successful.doubleValue() / total.doubleValue()) * 100);
        } else {
            report.setSuccessRate(0.0);
        }

        return report;
    }
}