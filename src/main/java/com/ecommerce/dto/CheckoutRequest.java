package com.ecommerce.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CheckoutRequest {

    private Long addressId;
    private String paymentMethod; // "RAZORPAY", "COD", "TEST"
    private BigDecimal amount;
    private String currency = "INR";
    
    // For Razorpay
    private String razorpayOrderId;
    private String razorpayPaymentId;
    private String razorpaySignature;
}



