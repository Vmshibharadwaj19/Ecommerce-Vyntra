package com.ecommerce.dto;

import com.ecommerce.entities.Order;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDto {

    private Long id;
    private String orderNumber;
    private Long userId;
    private String userName;
    private AddressDto shippingAddress;
    private List<OrderItemDto> orderItems = new ArrayList<>();
    private BigDecimal totalAmount;
    private BigDecimal shippingCharges;
    private BigDecimal discountAmount;
    private BigDecimal finalAmount;
    private Order.OrderStatus status;
    private Order.PaymentStatus paymentStatus;
    private String razorpayOrderId;
    private String razorpayPaymentId;
    private LocalDateTime shippedDate;
    private LocalDateTime deliveredDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}



