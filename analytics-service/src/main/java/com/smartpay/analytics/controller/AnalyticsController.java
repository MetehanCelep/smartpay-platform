package com.smartpay.analytics.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.smartpay.analytics.dto.AnalyticsReport;
import com.smartpay.analytics.entity.TransactionAnalytics;
import com.smartpay.analytics.service.AnalyticsService;

@RestController
@RequestMapping("/api/analytics")
public class AnalyticsController {

    private final AnalyticsService analyticsService;

    public AnalyticsController(AnalyticsService analyticsService) {
        this.analyticsService = analyticsService;
    }

    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Analytics Service is UP");
    }

    @GetMapping("/transactions")
    public ResponseEntity<List<TransactionAnalytics>> getAllTransactions() {
        return ResponseEntity.ok(analyticsService.getAllTransactions());
    }

    @GetMapping("/transactions/merchant/{merchantId}")
    public ResponseEntity<List<TransactionAnalytics>> getTransactionsByMerchant(@PathVariable String merchantId) {
        return ResponseEntity.ok(analyticsService.getTransactionsByMerchant(merchantId));
    }

    @GetMapping("/transactions/date-range")
    public ResponseEntity<List<TransactionAnalytics>> getTransactionsByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        return ResponseEntity.ok(analyticsService.getTransactionsByDateRange(startDate, endDate));
    }

    @GetMapping("/report")
    public ResponseEntity<AnalyticsReport> generateReport() {
        return ResponseEntity.ok(analyticsService.generateReport());
    }

    @GetMapping("/report/merchant/{merchantId}")
    public ResponseEntity<AnalyticsReport> generateMerchantReport(@PathVariable String merchantId) {
        return ResponseEntity.ok(analyticsService.generateMerchantReport(merchantId));
    }
}