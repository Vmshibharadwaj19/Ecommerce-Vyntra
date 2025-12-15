package com.ecommerce.dto;

import com.ecommerce.entities.Payment;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentDto {

    private Long id;
    private Long orderId;
    private String orderNumber;
    private String paymentId;
    private String razorpayOrderId;
    private String razorpayPaymentId;
    private BigDecimal amount;
    private Payment.PaymentMethod paymentMethod;
    private Payment.PaymentStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String failureReason;
    private Boolean isRefundable;
    private BigDecimal refundedAmount;
}



