package com.ecommerce.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentAnalyticsDto {

    private BigDecimal totalRevenue;
    private BigDecimal todayRevenue;
    private BigDecimal thisMonthRevenue;
    private BigDecimal thisYearRevenue;
    
    private Long totalTransactions;
    private Long successfulTransactions;
    private Long failedTransactions;
    private Long refundedTransactions;
    
    private BigDecimal totalRefundedAmount;
    private BigDecimal averageTransactionValue;
    
    private Map<LocalDate, BigDecimal> dailyRevenue;
    private Map<String, Long> paymentMethodStats;
    private Map<String, BigDecimal> paymentMethodRevenue;
}



