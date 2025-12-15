package com.ecommerce.controllers;

import com.ecommerce.dto.ApiResponse;
import com.ecommerce.dto.CheckoutRequest;
import com.ecommerce.dto.OrderDto;
import com.ecommerce.security.UserPrincipal;
import com.ecommerce.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@CrossOrigin(origins = "*")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<OrderDto>>> getUserOrders(Authentication authentication) {
        try {
            if (authentication == null || authentication.getPrincipal() == null) {
                return ResponseEntity.status(401).body(ApiResponse.error("User not authenticated"));
            }
            UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
            List<OrderDto> orders = orderService.getUserOrders(userPrincipal.getId());
            return ResponseEntity.ok(ApiResponse.success(orders != null ? orders : List.of()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error("Error fetching orders: " + e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<OrderDto>> getOrderById(@PathVariable Long id) {
        try {
            if (id == null) {
                return ResponseEntity.badRequest().body(ApiResponse.error("Invalid order ID"));
            }
            OrderDto order = orderService.getOrderById(id);
            if (order == null) {
                return ResponseEntity.status(404).body(ApiResponse.error("Order not found"));
            }
            return ResponseEntity.ok(ApiResponse.success(order));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error("Error fetching order: " + e.getMessage()));
        }
    }

    @PostMapping
    public ResponseEntity<ApiResponse<OrderDto>> createOrder(
            @RequestParam Long addressId,
            @RequestParam String razorpayOrderId,
            @RequestParam String razorpayPaymentId,
            @RequestParam String razorpaySignature,
            Authentication authentication) {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        OrderDto order = orderService.createOrder(
                userPrincipal.getId(),
                addressId,
                razorpayOrderId,
                razorpayPaymentId,
                razorpaySignature
        );
        return ResponseEntity.ok(ApiResponse.success("Order placed successfully", order));
    }

    @PostMapping("/checkout")
    public ResponseEntity<ApiResponse<OrderDto>> checkout(
            @RequestBody CheckoutRequest request,
            Authentication authentication) {
        try {
            UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
            OrderDto order = orderService.createOrderWithPaymentMethod(userPrincipal.getId(), request);
            return ResponseEntity.ok(ApiResponse.success("Order placed successfully", order));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Error creating order: " + e.getMessage()));
        }
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<ApiResponse<OrderDto>> updateOrderStatus(
            @PathVariable Long id,
            @RequestParam String status,
            Authentication authentication) {
        try {
            if (authentication == null || authentication.getPrincipal() == null) {
                return ResponseEntity.status(401).body(ApiResponse.error("User not authenticated"));
            }
            if (id == null || status == null || status.trim().isEmpty()) {
                return ResponseEntity.badRequest().body(ApiResponse.error("Invalid order ID or status"));
            }
            OrderDto order = orderService.updateOrderStatus(id, status);
            if (order == null) {
                return ResponseEntity.status(404).body(ApiResponse.error("Order not found"));
            }
            return ResponseEntity.ok(ApiResponse.success("Order status updated", order));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error("Error updating order status: " + e.getMessage()));
        }
    }
}

