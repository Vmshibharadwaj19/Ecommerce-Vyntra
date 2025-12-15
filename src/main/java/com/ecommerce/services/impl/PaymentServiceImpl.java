package com.ecommerce.services.impl;

import com.ecommerce.dto.*;
import com.ecommerce.entities.Order;
import com.ecommerce.entities.Payment;
import com.ecommerce.entities.PaymentTransaction;
import com.ecommerce.exceptions.ResourceNotFoundException;
import com.ecommerce.repositories.OrderRepository;
import com.ecommerce.repositories.PaymentRepository;
import com.ecommerce.repositories.PaymentTransactionRepository;
import com.ecommerce.repositories.UserRepository;
import com.ecommerce.services.PaymentService;
import com.razorpay.Refund;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import org.json.JSONObject;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class PaymentServiceImpl implements PaymentService {

    @Value("${razorpay.key.id}")
    private String razorpayKeyId;

    @Value("${razorpay.key.secret}")
    private String razorpayKeySecret;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private PaymentTransactionRepository paymentTransactionRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;

    private RazorpayClient getRazorpayClient() {
        if (razorpayKeyId == null || razorpayKeyId.isEmpty() || razorpayKeyId.equals("your_razorpay_key_id") ||
            razorpayKeySecret == null || razorpayKeySecret.isEmpty() || razorpayKeySecret.equals("your_razorpay_key_secret")) {
            throw new RuntimeException("Razorpay API keys are not configured. Please set razorpay.key.id and razorpay.key.secret in application.properties.");
        }
        try {
            return new RazorpayClient(razorpayKeyId, razorpayKeySecret);
        } catch (RazorpayException e) {
            throw new RuntimeException("Error initializing Razorpay client: " + e.getMessage());
        }
    }

    @Override
    public RazorpayOrderResponse createRazorpayOrder(RazorpayOrderRequest request, Long userId) {
        // Check if Razorpay keys are configured
        boolean razorpayConfigured = razorpayKeyId != null && !razorpayKeyId.isEmpty() && 
                                    !razorpayKeyId.equals("your_razorpay_key_id") &&
                                    razorpayKeySecret != null && !razorpayKeySecret.isEmpty() && 
                                    !razorpayKeySecret.equals("your_razorpay_key_secret");

        // If Razorpay not configured, return test mode response
        if (!razorpayConfigured) {
            RazorpayOrderResponse response = new RazorpayOrderResponse();
            response.setOrderId("test_order_" + System.currentTimeMillis());
            response.setAmount(request.getAmount().toString());
            response.setCurrency(request.getCurrency() != null ? request.getCurrency() : "INR");
            response.setKey("test_key");
            return response;
        }

        // Use real Razorpay if configured
        try {
            RazorpayClient razorpay = new RazorpayClient(razorpayKeyId, razorpayKeySecret);
            
            JSONObject orderRequest = new JSONObject();
            orderRequest.put("amount", request.getAmount().multiply(new BigDecimal("100")).intValue());
            orderRequest.put("currency", request.getCurrency() != null ? request.getCurrency() : "INR");
            orderRequest.put("receipt", request.getReceipt() != null ? request.getReceipt() : "receipt_" + System.currentTimeMillis());
            orderRequest.put("notes", new JSONObject().put("userId", userId.toString()));

            com.razorpay.Order razorpayOrder = razorpay.orders.create(orderRequest);

            RazorpayOrderResponse response = new RazorpayOrderResponse();
            response.setOrderId(razorpayOrder.get("id"));
            response.setAmount(request.getAmount().toString());
            response.setCurrency(request.getCurrency() != null ? request.getCurrency() : "INR");
            response.setKey(razorpayKeyId);

            return response;
        } catch (RazorpayException e) {
            String errorMessage = e.getMessage();
            if (errorMessage != null && errorMessage.contains("Authentication failed")) {
                throw new RuntimeException("Razorpay authentication failed. Please check your API keys in application.properties.");
            }
            throw new RuntimeException("Error creating Razorpay order: " + errorMessage);
        }
    }

    @Override
    public Boolean verifyPayment(PaymentVerificationRequest request) {
        try {
            String generatedSignature = calculateSignature(
                    request.getRazorpayOrderId() + "|" + request.getRazorpayPaymentId(),
                    razorpayKeySecret
            );
            return generatedSignature.equals(request.getRazorpaySignature());
        } catch (Exception e) {
            throw new RuntimeException("Error verifying payment: " + e.getMessage());
        }
    }

    @Override
    public PaymentDto getPaymentById(Long paymentId) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new ResourceNotFoundException("Payment", "id", paymentId));
        return mapToDto(payment);
    }

    @Override
    public PaymentDto getPaymentByOrderId(Long orderId) {
        Payment payment = paymentRepository.findByOrderId(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Payment", "orderId", orderId));
        return mapToDto(payment);
    }

    @Override
    public List<PaymentDto> getPaymentHistory(Long userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
        
        List<Payment> payments = paymentRepository.findAll().stream()
                .filter(p -> p.getOrder().getUser().getId().equals(userId))
                .sorted((p1, p2) -> p2.getCreatedAt().compareTo(p1.getCreatedAt()))
                .collect(Collectors.toList());
        
        return payments.stream().map(this::mapToDto).collect(Collectors.toList());
    }

    @Override
    public Page<PaymentDto> getAllPayments(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Payment> payments = paymentRepository.findAll(pageable);
        return payments.map(this::mapToDto);
    }

    @Override
    public RefundResponse processRefund(RefundRequest request) {
        Payment payment = paymentRepository.findById(request.getPaymentId())
                .orElseThrow(() -> new ResourceNotFoundException("Payment", "id", request.getPaymentId()));

        if (payment.getStatus() != Payment.PaymentStatus.SUCCESS) {
            throw new RuntimeException("Cannot refund payment that is not successful");
        }

        BigDecimal refundAmount = request.getAmount() != null ? request.getAmount() : payment.getAmount();
        
        if (refundAmount.compareTo(payment.getAmount()) > 0) {
            throw new RuntimeException("Refund amount cannot exceed payment amount");
        }

        try {
            RazorpayClient razorpay = getRazorpayClient();
            JSONObject refundRequest = new JSONObject();
            refundRequest.put("amount", refundAmount.multiply(new BigDecimal("100")).intValue());
            refundRequest.put("notes", new JSONObject()
                    .put("reason", request.getReason())
                    .put("notes", request.getNotes()));

            Refund refund = razorpay.payments.refund(payment.getRazorpayPaymentId(), refundRequest);

        // Create refund transaction record
        PaymentTransaction refundTransaction = new PaymentTransaction();
        refundTransaction.setPayment(payment);
        refundTransaction.setTransactionId("REFUND_" + System.currentTimeMillis());
        refundTransaction.setType(PaymentTransaction.TransactionType.REFUND);
        refundTransaction.setStatus(PaymentTransaction.TransactionStatus.SUCCESS);
        refundTransaction.setAmount(refundAmount);
        refundTransaction.setRazorpayRefundId(refund.get("id"));
        refundTransaction.setNotes(request.getNotes());
        paymentTransactionRepository.save(refundTransaction);

        // Update payment status
        if (refundAmount.compareTo(payment.getAmount()) == 0) {
            payment.setStatus(Payment.PaymentStatus.REFUNDED);
            payment.getOrder().setPaymentStatus(Order.PaymentStatus.REFUNDED);
        } else {
            // Partial refund - keep status as SUCCESS but track refunded amount
        }
        paymentRepository.save(payment);
        orderRepository.save(payment.getOrder());

        RefundResponse response = new RefundResponse();
        response.setRefundId(refundTransaction.getId());
        response.setPaymentId(payment.getId());
        response.setRefundAmount(refundAmount);
        response.setRefundStatus("SUCCESS");
        response.setRazorpayRefundId(refund.get("id"));
        response.setReason(request.getReason());
        response.setProcessedAt(LocalDateTime.now());
        response.setMessage("Refund processed successfully");

        return response;
    } catch (RazorpayException e) {
        throw new RuntimeException("Error processing refund: " + e.getMessage());
    }
    }

    @Override
    public PaymentDto retryPayment(Long paymentId) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new ResourceNotFoundException("Payment", "id", paymentId));

        if (payment.getStatus() == Payment.PaymentStatus.SUCCESS) {
            throw new RuntimeException("Payment is already successful");
        }

        // Create new Razorpay order for retry
        RazorpayOrderRequest orderRequest = new RazorpayOrderRequest();
        orderRequest.setAmount(payment.getAmount());
        orderRequest.setCurrency("INR");
        orderRequest.setReceipt("RETRY_" + payment.getOrder().getOrderNumber());

        RazorpayOrderResponse razorpayOrder = createRazorpayOrder(orderRequest, payment.getOrder().getUser().getId());

        // Update payment with new Razorpay order ID
        payment.setRazorpayOrderId(razorpayOrder.getOrderId());
        payment.setStatus(Payment.PaymentStatus.PENDING);
        paymentRepository.save(payment);

        return mapToDto(payment);
    }

    @Override
    public PaymentAnalyticsDto getPaymentAnalytics() {
        LocalDate today = LocalDate.now();
        LocalDate startOfMonth = today.withDayOfMonth(1);
        LocalDate startOfYear = today.withDayOfYear(1);
        
        LocalDateTime todayStart = today.atStartOfDay();
        LocalDateTime monthStart = startOfMonth.atStartOfDay();
        LocalDateTime yearStart = startOfYear.atStartOfDay();
        LocalDateTime now = LocalDateTime.now();

        List<Payment> allPayments = paymentRepository.findAll();
        
        BigDecimal totalRevenue = allPayments.stream()
                .filter(p -> p.getStatus() == Payment.PaymentStatus.SUCCESS)
                .map(p -> p.getAmount())
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal todayRevenue = allPayments.stream()
                .filter(p -> p.getStatus() == Payment.PaymentStatus.SUCCESS && 
                           p.getCreatedAt().isAfter(todayStart) && p.getCreatedAt().isBefore(now))
                .map(p -> p.getAmount())
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal monthRevenue = allPayments.stream()
                .filter(p -> p.getStatus() == Payment.PaymentStatus.SUCCESS && 
                           p.getCreatedAt().isAfter(monthStart) && p.getCreatedAt().isBefore(now))
                .map(p -> p.getAmount())
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal yearRevenue = allPayments.stream()
                .filter(p -> p.getStatus() == Payment.PaymentStatus.SUCCESS && 
                           p.getCreatedAt().isAfter(yearStart) && p.getCreatedAt().isBefore(now))
                .map(p -> p.getAmount())
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        long totalTransactions = allPayments.size();
        long successfulTransactions = allPayments.stream()
                .filter(p -> p.getStatus() == Payment.PaymentStatus.SUCCESS).count();
        long failedTransactions = allPayments.stream()
                .filter(p -> p.getStatus() == Payment.PaymentStatus.FAILED).count();
        long refundedTransactions = paymentTransactionRepository.countByStatusAndDateRange(
                PaymentTransaction.TransactionStatus.SUCCESS,
                LocalDateTime.of(2020, 1, 1, 0, 0),
                now
        );

        BigDecimal totalRefunded = paymentTransactionRepository.sumAmountByTypeAndDateRange(
                PaymentTransaction.TransactionType.REFUND,
                LocalDateTime.of(2020, 1, 1, 0, 0),
                now
        );

        BigDecimal avgTransaction = totalTransactions > 0 ? 
                totalRevenue.divide(BigDecimal.valueOf(totalTransactions), 2, java.math.RoundingMode.HALF_UP) : 
                BigDecimal.ZERO;

        PaymentAnalyticsDto analytics = new PaymentAnalyticsDto();
        analytics.setTotalRevenue(totalRevenue);
        analytics.setTodayRevenue(todayRevenue);
        analytics.setThisMonthRevenue(monthRevenue);
        analytics.setThisYearRevenue(yearRevenue);
        analytics.setTotalTransactions(totalTransactions);
        analytics.setSuccessfulTransactions(successfulTransactions);
        analytics.setFailedTransactions(failedTransactions);
        analytics.setRefundedTransactions(refundedTransactions);
        analytics.setTotalRefundedAmount(totalRefunded != null ? totalRefunded : BigDecimal.ZERO);
        analytics.setAverageTransactionValue(avgTransaction);

        return analytics;
    }

    @Override
    public PaymentAnalyticsDto getPaymentAnalyticsByDateRange(LocalDate startDate, LocalDate endDate) {
        LocalDateTime start = startDate.atStartOfDay();
        LocalDateTime end = endDate.atTime(23, 59, 59);

        List<Payment> payments = paymentRepository.findAll().stream()
                .filter(p -> p.getCreatedAt().isAfter(start) && p.getCreatedAt().isBefore(end))
                .collect(Collectors.toList());

        BigDecimal totalRevenue = payments.stream()
                .filter(p -> p.getStatus() == Payment.PaymentStatus.SUCCESS)
                .map(p -> p.getAmount())
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        PaymentAnalyticsDto analytics = new PaymentAnalyticsDto();
        analytics.setTotalRevenue(totalRevenue);
        analytics.setTotalTransactions((long) payments.size());
        analytics.setSuccessfulTransactions(payments.stream()
                .filter(p -> p.getStatus() == Payment.PaymentStatus.SUCCESS).count());
        analytics.setFailedTransactions(payments.stream()
                .filter(p -> p.getStatus() == Payment.PaymentStatus.FAILED).count());

        return analytics;
    }

    @Override
    public void handleWebhook(String event, String payload) {
        try {
            JSONObject webhookPayload = new JSONObject(payload);
            
            switch (event) {
                case "payment.captured":
                    handlePaymentCaptured(webhookPayload);
                    break;
                case "payment.failed":
                    handlePaymentFailed(webhookPayload);
                    break;
                case "refund.created":
                    handleRefundCreated(webhookPayload);
                    break;
                case "order.paid":
                    handleOrderPaid(webhookPayload);
                    break;
                default:
                    // Log unhandled events
                    break;
            }
        } catch (Exception e) {
            throw new RuntimeException("Error handling webhook: " + e.getMessage());
        }
    }

    private void handlePaymentCaptured(JSONObject payload) {
        JSONObject payment = payload.getJSONObject("payload").getJSONObject("payment").getJSONObject("entity");
        String razorpayPaymentId = payment.getString("id");
        
        Optional<Payment> paymentOpt = paymentRepository.findByRazorpayPaymentId(razorpayPaymentId);
        if (paymentOpt.isPresent()) {
            Payment p = paymentOpt.get();
            p.setStatus(Payment.PaymentStatus.SUCCESS);
            p.getOrder().setPaymentStatus(Order.PaymentStatus.PAID);
            p.getOrder().setStatus(Order.OrderStatus.CONFIRMED);
            paymentRepository.save(p);
            orderRepository.save(p.getOrder());
        }
    }

    private void handlePaymentFailed(JSONObject payload) {
        JSONObject payment = payload.getJSONObject("payload").getJSONObject("payment").getJSONObject("entity");
        String razorpayPaymentId = payment.getString("id");
        
        Optional<Payment> paymentOpt = paymentRepository.findByRazorpayPaymentId(razorpayPaymentId);
        if (paymentOpt.isPresent()) {
            Payment p = paymentOpt.get();
            p.setStatus(Payment.PaymentStatus.FAILED);
            p.getOrder().setPaymentStatus(Order.PaymentStatus.FAILED);
            paymentRepository.save(p);
            orderRepository.save(p.getOrder());
        }
    }

    private void handleRefundCreated(JSONObject payload) {
        // Handle refund webhook
    }

    private void handleOrderPaid(JSONObject payload) {
        // Handle order paid webhook
    }

    private String calculateSignature(String data, String secret) throws Exception {
        Mac mac = Mac.getInstance("HmacSHA256");
        SecretKeySpec secretKeySpec = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
        mac.init(secretKeySpec);
        byte[] hash = mac.doFinal(data.getBytes(StandardCharsets.UTF_8));
        StringBuilder hexString = new StringBuilder();
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }

    private PaymentDto mapToDto(Payment payment) {
        PaymentDto dto = modelMapper.map(payment, PaymentDto.class);
        dto.setOrderId(payment.getOrder().getId());
        dto.setOrderNumber(payment.getOrder().getOrderNumber());
        
        // Calculate refunded amount
        BigDecimal refundedAmount = paymentTransactionRepository.findByPaymentId(payment.getId()).stream()
                .filter(t -> t.getType() == PaymentTransaction.TransactionType.REFUND && 
                           t.getStatus() == PaymentTransaction.TransactionStatus.SUCCESS)
                .map(t -> t.getAmount())
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        dto.setRefundedAmount(refundedAmount);
        dto.setIsRefundable(payment.getStatus() == Payment.PaymentStatus.SUCCESS && 
                           refundedAmount.compareTo(payment.getAmount()) < 0);
        
        return dto;
    }
}
