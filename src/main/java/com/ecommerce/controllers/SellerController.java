package com.ecommerce.controllers;

import com.ecommerce.dto.ApiResponse;
import com.ecommerce.dto.OrderDto;
import com.ecommerce.security.UserPrincipal;
import com.ecommerce.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/seller")
@CrossOrigin(origins = "*")
public class SellerController {

    @Autowired
    private OrderService orderService;

    @GetMapping("/orders")
    public ResponseEntity<ApiResponse<List<OrderDto>>> getSellerOrders(Authentication authentication) {
        try {
            if (authentication == null || authentication.getPrincipal() == null) {
                return ResponseEntity.status(401).body(ApiResponse.error("User not authenticated"));
            }
            UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
            List<OrderDto> orders = orderService.getSellerOrders(userPrincipal.getId());
            return ResponseEntity.ok(ApiResponse.success(orders != null ? orders : List.of()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error("Error fetching seller orders: " + e.getMessage()));
        }
    }

    @PutMapping("/orders/{id}/status")
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

