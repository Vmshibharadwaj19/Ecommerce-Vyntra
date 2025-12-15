package com.ecommerce.controllers;

import com.ecommerce.dto.*;
import com.ecommerce.security.UserPrincipal;
import com.ecommerce.services.OrderService;
import com.ecommerce.services.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/payment")
@CrossOrigin(origins = "*")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private OrderService orderService;

    // Basic Payment Operations
    @PostMapping("/create-order")
    public ResponseEntity<ApiResponse<RazorpayOrderResponse>> createRazorpayOrder(
            @RequestBody RazorpayOrderRequest request,
            Authentication authentication) {
        try {
            UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
            RazorpayOrderResponse response = paymentService.createRazorpayOrder(request, userPrincipal.getId());
            return ResponseEntity.ok(ApiResponse.success("Payment order created successfully", response));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Error creating payment order: " + e.getMessage()));
        }
    }

    @PostMapping("/verify")
    public ResponseEntity<ApiResponse<Object>> verifyPayment(
            @RequestBody PaymentVerificationRequest request,
            Authentication authentication) {
        try {
            UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
            
            Boolean isValid = paymentService.verifyPayment(request);
            
            if (isValid) {
                orderService.createOrder(
                        userPrincipal.getId(),
                        request.getAddressId(),
                        request.getRazorpayOrderId(),
                        request.getRazorpayPaymentId(),
                        request.getRazorpaySignature()
                );
                return ResponseEntity.ok(ApiResponse.success("Payment verified and order created successfully", null));
            } else {
                return ResponseEntity.badRequest().body(ApiResponse.error("Payment verification failed"));
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Error verifying payment: " + e.getMessage()));
        }
    }

    // Enterprise Payment Management
    @GetMapping("/{paymentId}")
    public ResponseEntity<ApiResponse<PaymentDto>> getPaymentById(
            @PathVariable Long paymentId,
            Authentication authentication) {
        try {
            PaymentDto payment = paymentService.getPaymentById(paymentId);
            return ResponseEntity.ok(ApiResponse.success(payment));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error("Error fetching payment: " + e.getMessage()));
        }
    }

    @GetMapping("/order/{orderId}")
    public ResponseEntity<ApiResponse<PaymentDto>> getPaymentByOrderId(
            @PathVariable Long orderId,
            Authentication authentication) {
        try {
            PaymentDto payment = paymentService.getPaymentByOrderId(orderId);
            return ResponseEntity.ok(ApiResponse.success(payment));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error("Error fetching payment: " + e.getMessage()));
        }
    }

    @GetMapping("/history")
    public ResponseEntity<ApiResponse<List<PaymentDto>>> getPaymentHistory(Authentication authentication) {
        try {
            UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
            List<PaymentDto> payments = paymentService.getPaymentHistory(userPrincipal.getId());
            return ResponseEntity.ok(ApiResponse.success(payments));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error("Error fetching payment history: " + e.getMessage()));
        }
    }

    @GetMapping("/all")
    public ResponseEntity<ApiResponse<Page<PaymentDto>>> getAllPayments(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            Authentication authentication) {
        try {
            Page<PaymentDto> payments = paymentService.getAllPayments(page, size);
            return ResponseEntity.ok(ApiResponse.success(payments));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error("Error fetching payments: " + e.getMessage()));
        }
    }

    // Refund Management
    @PostMapping("/refund")
    public ResponseEntity<ApiResponse<RefundResponse>> processRefund(
            @RequestBody RefundRequest request,
            Authentication authentication) {
        try {
            RefundResponse refund = paymentService.processRefund(request);
            return ResponseEntity.ok(ApiResponse.success("Refund processed successfully", refund));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error("Error processing refund: " + e.getMessage()));
        }
    }

    // Payment Retry
    @PostMapping("/{paymentId}/retry")
    public ResponseEntity<ApiResponse<PaymentDto>> retryPayment(
            @PathVariable Long paymentId,
            Authentication authentication) {
        try {
            PaymentDto payment = paymentService.retryPayment(paymentId);
            return ResponseEntity.ok(ApiResponse.success("Payment retry initiated", payment));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error("Error retrying payment: " + e.getMessage()));
        }
    }

    // Payment Analytics
    @GetMapping("/analytics")
    public ResponseEntity<ApiResponse<PaymentAnalyticsDto>> getPaymentAnalytics(Authentication authentication) {
        try {
            PaymentAnalyticsDto analytics = paymentService.getPaymentAnalytics();
            return ResponseEntity.ok(ApiResponse.success(analytics));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error("Error fetching analytics: " + e.getMessage()));
        }
    }

    @GetMapping("/analytics/range")
    public ResponseEntity<ApiResponse<PaymentAnalyticsDto>> getPaymentAnalyticsByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            Authentication authentication) {
        try {
            PaymentAnalyticsDto analytics = paymentService.getPaymentAnalyticsByDateRange(startDate, endDate);
            return ResponseEntity.ok(ApiResponse.success(analytics));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error("Error fetching analytics: " + e.getMessage()));
        }
    }

    // Webhook Handler
    @PostMapping("/webhook")
    public ResponseEntity<ApiResponse<Object>> handleWebhook(
            @RequestHeader("X-Razorpay-Signature") String signature,
            @RequestBody String payload) {
        try {
            // Verify webhook signature
            // For production, verify signature before processing
            
            // Parse webhook event
            org.json.JSONObject webhookData = new org.json.JSONObject(payload);
            String event = webhookData.getString("event");
            
            paymentService.handleWebhook(event, payload);
            
            return ResponseEntity.ok(ApiResponse.success("Webhook processed successfully", null));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error("Error processing webhook: " + e.getMessage()));
        }
    }
}
