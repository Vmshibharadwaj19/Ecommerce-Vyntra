package com.ecommerce.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RefundResponse {

    private Long refundId;
    private Long paymentId;
    private BigDecimal refundAmount;
    private String refundStatus;
    private String razorpayRefundId;
    private String reason;
    private LocalDateTime processedAt;
    private String message;
}



