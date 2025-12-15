package com.ecommerce.services;

import com.ecommerce.dto.CheckoutRequest;
import com.ecommerce.dto.OrderDto;

import java.util.List;

public interface OrderService {

    OrderDto createOrder(Long userId, Long addressId, String razorpayOrderId, 
                        String razorpayPaymentId, String razorpaySignature);

    OrderDto createOrderWithPaymentMethod(Long userId, CheckoutRequest request);

    OrderDto getOrderById(Long orderId);

    List<OrderDto> getUserOrders(Long userId);

    OrderDto updateOrderStatus(Long orderId, String status);

    List<OrderDto> getSellerOrders(Long sellerId);

    List<OrderDto> getAllOrders();
}

