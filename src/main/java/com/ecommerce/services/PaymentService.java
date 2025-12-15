package com.ecommerce.services;

import com.ecommerce.dto.*;
import org.springframework.data.domain.Page;

import java.time.LocalDate;
import java.util.List;

public interface PaymentService {

    RazorpayOrderResponse createRazorpayOrder(RazorpayOrderRequest request, Long userId);

    Boolean verifyPayment(PaymentVerificationRequest request);
    
    // Enterprise features
    PaymentDto getPaymentById(Long paymentId);
    
    PaymentDto getPaymentByOrderId(Long orderId);
    
    List<PaymentDto> getPaymentHistory(Long userId);
    
    Page<PaymentDto> getAllPayments(int page, int size);
    
    RefundResponse processRefund(RefundRequest request);
    
    PaymentDto retryPayment(Long paymentId);
    
    PaymentAnalyticsDto getPaymentAnalytics();
    
    PaymentAnalyticsDto getPaymentAnalyticsByDateRange(LocalDate startDate, LocalDate endDate);
    
    void handleWebhook(String event, String payload);
}

