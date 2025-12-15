package com.ecommerce.services;

import com.ecommerce.entities.Order;
import com.ecommerce.entities.User;

public interface NotificationService {
    void sendSellerApprovalNotification(User seller);
    void sendSellerRejectionNotification(User seller);
    void sendOrderConfirmationEmail(Order order);
    void sendOrderShippedEmail(Order order);
    void sendOrderDeliveredEmail(Order order);
    void sendOrderCancelledEmail(Order order);
    void sendOrderStatusUpdateEmail(Order order, String newStatus);
    void sendPasswordResetOtpEmail(String email, String otp, String firstName);
}

