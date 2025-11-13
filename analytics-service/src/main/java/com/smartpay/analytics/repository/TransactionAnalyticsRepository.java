package com.smartpay.analytics.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.smartpay.analytics.entity.TransactionAnalytics;

public interface TransactionAnalyticsRepository extends JpaRepository<TransactionAnalytics, Long> {

    List<TransactionAnalytics> findByMerchantId(String merchantId);

    List<TransactionAnalytics> findByStatus(String status);

    @Query("SELECT t FROM TransactionAnalytics t WHERE t.transactionDate BETWEEN :startDate AND :endDate")
    List<TransactionAnalytics> findByDateRange(@Param("startDate") LocalDateTime startDate, 
                                                @Param("endDate") LocalDateTime endDate);

    @Query("SELECT SUM(t.amount) FROM TransactionAnalytics t WHERE t.status = 'SUCCESS'")
    Double getTotalSuccessfulAmount();

    @Query("SELECT COUNT(t) FROM TransactionAnalytics t WHERE t.status = :status")
    Long countByStatus(@Param("status") String status);

    @Query("SELECT AVG(t.amount) FROM TransactionAnalytics t WHERE t.status = 'SUCCESS'")
    Double getAverageTransactionAmount();

    @Query("SELECT COUNT(t) FROM TransactionAnalytics t WHERE t.merchantId = :merchantId")
    Long countByMerchantId(@Param("merchantId") String merchantId);

    @Query("SELECT COUNT(t) FROM TransactionAnalytics t WHERE t.merchantId = :merchantId AND t.status = :status")
    Long countByMerchantIdAndStatus(@Param("merchantId") String merchantId, @Param("status") String status);

    @Query("SELECT SUM(t.amount) FROM TransactionAnalytics t WHERE t.merchantId = :merchantId AND t.status = 'SUCCESS'")
    Double getTotalSuccessfulAmountByMerchantId(@Param("merchantId") String merchantId);

    @Query("SELECT AVG(t.amount) FROM TransactionAnalytics t WHERE t.merchantId = :merchantId AND t.status = 'SUCCESS'")
    Double getAverageTransactionAmountByMerchantId(@Param("merchantId") String merchantId);
}