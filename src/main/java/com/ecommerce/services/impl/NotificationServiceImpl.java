package com.ecommerce.services.impl;

import com.ecommerce.entities.User;
import com.ecommerce.services.NotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class NotificationServiceImpl implements NotificationService {

    private static final Logger logger = LoggerFactory.getLogger(NotificationServiceImpl.class);

    @Autowired(required = false)
    private JavaMailSender mailSender;

    @Value("${mail.from.address:noreply@vyntra.com}")
    private String fromEmail;

    @Value("${mail.from.name:VYNTRA}")
    private String fromName;

    @Value("${mail.enabled:true}")
    private boolean mailEnabled;
    
    @Value("${spring.mail.username:}")
    private String mailUsername;
    
    @Value("${spring.mail.password:}")
    private String mailPassword;
    
    // Log email configuration on startup
    @jakarta.annotation.PostConstruct
    public void logEmailConfiguration() {
        logger.info("========================================");
        logger.info("📧 EMAIL CONFIGURATION STATUS");
        logger.info("========================================");
        logger.info("Email Enabled: {}", mailEnabled);
        logger.info("MailSender Available: {}", mailSender != null);
        logger.info("From Email: {}", fromEmail);
        logger.info("From Name: {}", fromName);
        logger.info("SMTP Username: {}", mailUsername.isEmpty() ? "NOT SET" : mailUsername);
        logger.info("SMTP Password: {}", mailPassword.isEmpty() ? "NOT SET" : "***SET***");
        if (mailSender == null) {
            logger.error("❌ JavaMailSender is NULL! Email will not work.");
            logger.error("❌ Check: 1) spring-boot-starter-mail in pom.xml");
            logger.error("❌ Check: 2) Email properties in application.properties");
            logger.error("❌ Check: 3) Rebuild project after adding mail dependency");
        } else if (!mailEnabled) {
            logger.warn("⚠️  Email is DISABLED (mail.enabled=false). Emails will be logged only.");
        } else if (mailUsername.isEmpty() || mailPassword.isEmpty() || mailUsername.contains("your-email")) {
            logger.error("❌ Email credentials are NOT configured!");
            logger.error("❌ Update spring.mail.username and spring.mail.password in application.properties");
            logger.error("❌ Current username: {}", mailUsername);
        } else {
            logger.info("✅ Email configuration looks good!");
        }
        logger.info("========================================");
    }

    @Override
    public void sendSellerApprovalNotification(User seller) {
        String subject = "Your Seller Account Has Been Approved! - VYNTRA";
        String htmlContent = buildSellerApprovalEmail(seller);
        
        logger.info("========================================");
        logger.info("📧 SELLER APPROVAL NOTIFICATION");
        logger.info("========================================");
        logger.info("To: {}", seller.getEmail());
        logger.info("Email Enabled: {}", mailEnabled);
        logger.info("MailSender Available: {}", mailSender != null);
        logger.info("From Email: {}", fromEmail);
        logger.info("Subject: {}", subject);
        logger.info("========================================");
        
        if (mailEnabled && mailSender != null) {
            try {
                sendHtmlEmail(seller.getEmail(), subject, htmlContent);
                logger.info("✅ Email sent successfully to {}", seller.getEmail());
            } catch (Exception e) {
                logger.error("❌ Failed to send email to {}: {}", seller.getEmail(), e.getMessage());
                // Fallback to simple text email
                try {
                    sendSimpleEmail(seller.getEmail(), subject, getSellerApprovalText(seller));
                } catch (Exception ex) {
                    logger.error("❌ Failed to send simple email: {}", ex.getMessage());
                }
            }
        } else {
            logger.warn("⚠️  Email sending is disabled or mailSender not configured. Email would be sent to: {}", seller.getEmail());
        }
    }

    @Override
    public void sendSellerRejectionNotification(User seller) {
        String subject = "Seller Account Application Status - VYNTRA";
        String htmlContent = buildSellerRejectionEmail(seller);
        
        logger.info("========================================");
        logger.info("📧 SELLER REJECTION NOTIFICATION");
        logger.info("========================================");
        logger.info("To: {}", seller.getEmail());
        logger.info("Subject: {}", subject);
        logger.info("========================================");
        
        if (mailEnabled && mailSender != null) {
            try {
                sendHtmlEmail(seller.getEmail(), subject, htmlContent);
                logger.info("✅ Email sent successfully to {}", seller.getEmail());
            } catch (Exception e) {
                logger.error("❌ Failed to send email to {}: {}", seller.getEmail(), e.getMessage());
                // Fallback to simple text email
                try {
                    sendSimpleEmail(seller.getEmail(), subject, getSellerRejectionText(seller));
                } catch (Exception ex) {
                    logger.error("❌ Failed to send simple email: {}", ex.getMessage());
                }
            }
        } else {
            logger.warn("⚠️  Email sending is disabled or mailSender not configured. Email would be sent to: {}", seller.getEmail());
        }
    }

    /**
     * Send HTML email using Spring Mail features.
     * Uses a safe approach that avoids encoding issues.
     */
    private void sendHtmlEmail(String to, String subject, String htmlContent) {
        logger.info("🔍 Email Debug - To: {}, Enabled: {}, MailSender: {}", to, mailEnabled, mailSender != null);
        
        if (mailSender == null) {
            logger.error("❌ MailSender is NULL! Email cannot be sent. Check email configuration in application.properties");
            logger.warn("⚠️  Email would be sent to: {}", to);
            return;
        }
        
        if (!mailEnabled) {
            logger.warn("⚠️  Email sending is DISABLED (mail.enabled=false). Email would be sent to: {}", to);
            logger.info("📧 Email content (logged only):\nSubject: {}\nTo: {}\nContent: {}", subject, to, htmlContent.substring(0, Math.min(200, htmlContent.length())));
            return;
        }
        
        try {
            // Use Spring's MimeMessage directly with safe encoding
            MimeMessage message = mailSender.createMimeMessage();
            
            // Create helper with multipart support (true) - no encoding parameter to avoid issues
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            
            // Set from address using simple string (no encoding needed)
            helper.setFrom(fromEmail);
            
            // Set recipient
            helper.setTo(to);
            
            // Set subject
            helper.setSubject(subject);
            
            // Set HTML content (true = HTML)
            helper.setText(htmlContent, true);
            
            // Send email using Spring Mail
            mailSender.send(message);
            logger.info("✅ HTML email sent successfully to: {}", to);
            
        } catch (MessagingException e) {
            logger.error("❌ Failed to send HTML email to {}: {}", to, e.getMessage(), e);
            logger.error("❌ Error details: {}", e.getClass().getName());
            if (e.getCause() != null) {
                logger.error("❌ Root cause: {}", e.getCause().getMessage());
            }
            // Try fallback to simple text email
            sendSimpleEmailFallback(to, subject, htmlContent);
        } catch (Exception e) {
            logger.error("❌ Unexpected error sending HTML email to {}: {}", to, e.getMessage(), e);
            logger.error("❌ Error type: {}", e.getClass().getName());
            // Try fallback to simple text email
            sendSimpleEmailFallback(to, subject, htmlContent);
        }
    }
    
    /**
     * Fallback method to send simple text email if HTML email fails.
     */
    private void sendSimpleEmailFallback(String to, String subject, String htmlContent) {
        try {
            // Strip HTML tags and send as plain text
            String plainText = htmlContent.replaceAll("<[^>]+>", "")
                                          .replaceAll("&nbsp;", " ")
                                          .replaceAll("&amp;", "&")
                                          .replaceAll("&lt;", "<")
                                          .replaceAll("&gt;", ">")
                                          .trim();
            sendSimpleEmail(to, subject, plainText);
        } catch (Exception ex) {
            logger.error("❌ Failed to send fallback simple email to {}: {}", to, ex.getMessage());
        }
    }

    /**
     * Send simple text email using Spring Mail SimpleMailMessage.
     * This is the safest method with no encoding issues.
     */
    private void sendSimpleEmail(String to, String subject, String text) {
        if (mailSender == null || !mailEnabled) {
            logger.warn("⚠️  Email sending is disabled. Simple email would be sent to: {}", to);
            return;
        }
        
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(to);
            message.setSubject(subject);
            message.setText(text);
            mailSender.send(message);
            logger.info("✅ Simple email sent successfully to: {}", to);
        } catch (Exception e) {
            logger.error("❌ Failed to send simple email to {}: {}", to, e.getMessage(), e);
        }
    }

    private String buildSellerApprovalEmail(User seller) {
        return "<!DOCTYPE html>" +
                "<html>" +
                "<head>" +
                "<style>" +
                "body { font-family: Arial, sans-serif; line-height: 1.6; color: #333; }" +
                ".container { max-width: 600px; margin: 0 auto; padding: 20px; }" +
                ".header { background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); color: white; padding: 30px; text-align: center; border-radius: 8px 8px 0 0; }" +
                ".content { background: #f9f9f9; padding: 30px; border-radius: 0 0 8px 8px; }" +
                ".button { display: inline-block; padding: 12px 30px; background-color: #28a745; color: white; text-decoration: none; border-radius: 5px; margin: 20px 0; }" +
                ".footer { text-align: center; margin-top: 30px; color: #666; font-size: 12px; }" +
                "</style>" +
                "</head>" +
                "<body>" +
                "<div class='container'>" +
                "<div class='header'>" +
                "<h1>🎉 Account Approved!</h1>" +
                "</div>" +
                "<div class='content'>" +
                "<p>Dear " + seller.getFirstName() + " " + seller.getLastName() + ",</p>" +
                "<p>Congratulations! Your seller account has been <strong>approved</strong>.</p>" +
                "<p>You can now start adding products to <strong>VYNTRA</strong> and begin selling!</p>" +
                "<div style='background: #fff3cd; padding: 15px; border-left: 4px solid #ffc107; margin: 20px 0;'>" +
                "<p><strong>Important:</strong> Each product you add will still require admin approval before it appears on the customer dashboard.</p>" +
                "</div>" +
                "<p><strong>Your Business Details:</strong></p>" +
                "<ul>" +
                "<li>Business Name: " + (seller.getBusinessName() != null ? seller.getBusinessName() : "N/A") + "</li>" +
                "<li>GST Number: " + (seller.getGstNumber() != null ? seller.getGstNumber() : "N/A") + "</li>" +
                "</ul>" +
                "<a href='http://localhost:3000/seller/dashboard' class='button'>Go to Seller Dashboard</a>" +
                "<p>Thank you for joining VYNTRA! We're excited to have you on board.</p>" +
                "<p>Best regards,<br>VYNTRA Team</p>" +
                "</div>" +
                "<div class='footer'>" +
                "<p>This is an automated email. Please do not reply.</p>" +
                "<p>&copy; 2024 VYNTRA. All rights reserved.</p>" +
                "</div>" +
                "</div>" +
                "</body>" +
                "</html>";
    }

    private String buildSellerRejectionEmail(User seller) {
        return "<!DOCTYPE html>" +
                "<html>" +
                "<head>" +
                "<style>" +
                "body { font-family: Arial, sans-serif; line-height: 1.6; color: #333; }" +
                ".container { max-width: 600px; margin: 0 auto; padding: 20px; }" +
                ".header { background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%); color: white; padding: 30px; text-align: center; border-radius: 8px 8px 0 0; }" +
                ".content { background: #f9f9f9; padding: 30px; border-radius: 0 0 8px 8px; }" +
                ".footer { text-align: center; margin-top: 30px; color: #666; font-size: 12px; }" +
                "</style>" +
                "</head>" +
                "<body>" +
                "<div class='container'>" +
                "<div class='header'>" +
                "<h1>Application Status Update</h1>" +
                "</div>" +
                "<div class='content'>" +
                "<p>Dear " + seller.getFirstName() + " " + seller.getLastName() + ",</p>" +
                "<p>We regret to inform you that your seller account application has been reviewed and unfortunately, we cannot approve it at this time.</p>" +
                "<p><strong>Your Business Details:</strong></p>" +
                "<ul>" +
                "<li>Business Name: " + (seller.getBusinessName() != null ? seller.getBusinessName() : "N/A") + "</li>" +
                "<li>GST Number: " + (seller.getGstNumber() != null ? seller.getGstNumber() : "N/A") + "</li>" +
                "</ul>" +
                "<p>If you have any questions or would like to appeal this decision, please contact our support team.</p>" +
                "<p>Thank you for your interest in VYNTRA.</p>" +
                "<p>Best regards,<br>VYNTRA Team</p>" +
                "</div>" +
                "<div class='footer'>" +
                "<p>This is an automated email. Please do not reply.</p>" +
                "<p>&copy; 2024 VYNTRA. All rights reserved.</p>" +
                "</div>" +
                "</div>" +
                "</body>" +
                "</html>";
    }

    private String getSellerApprovalText(User seller) {
        return "Dear " + seller.getFirstName() + " " + seller.getLastName() + ",\n\n" +
                "Congratulations! Your seller account has been approved.\n\n" +
                "You can now start adding products to VYNTRA.\n\n" +
                "Important: Each product you add will still require admin approval before it appears on the customer dashboard.\n\n" +
                "Business Name: " + (seller.getBusinessName() != null ? seller.getBusinessName() : "N/A") + "\n" +
                "GST Number: " + (seller.getGstNumber() != null ? seller.getGstNumber() : "N/A") + "\n\n" +
                "Login to your seller dashboard: http://localhost:3000/seller/dashboard\n\n" +
                "Thank you for joining VYNTRA!\n\n" +
                "Best regards,\n" +
                "VYNTRA Team";
    }

    private String getSellerRejectionText(User seller) {
        return "Dear " + seller.getFirstName() + " " + seller.getLastName() + ",\n\n" +
                "We regret to inform you that your seller account application has been reviewed and unfortunately, we cannot approve it at this time.\n\n" +
                "Business Name: " + (seller.getBusinessName() != null ? seller.getBusinessName() : "N/A") + "\n" +
                "GST Number: " + (seller.getGstNumber() != null ? seller.getGstNumber() : "N/A") + "\n\n" +
                "If you have any questions or would like to appeal this decision, please contact our support team.\n\n" +
                "Thank you for your interest in VYNTRA.\n\n" +
                "Best regards,\n" +
                "VYNTRA Team";
    }

    // Order Email Notifications
    @Override
    public void sendOrderConfirmationEmail(com.ecommerce.entities.Order order) {
        String subject = "Order Confirmed - " + order.getOrderNumber() + " - VYNTRA";
        String htmlContent = buildOrderConfirmationEmail(order);
        
        logger.info("📧 ORDER CONFIRMATION EMAIL - Order: {} - To: {}", 
                   order.getOrderNumber(), order.getUser().getEmail());
        
        if (mailEnabled && mailSender != null) {
            try {
                sendHtmlEmail(order.getUser().getEmail(), subject, htmlContent);
                logger.info("✅ Order confirmation email sent successfully");
            } catch (Exception e) {
                logger.error("❌ Failed to send order confirmation email: {}", e.getMessage());
            }
        } else {
            logger.warn("⚠️  Email sending disabled. Order confirmation email would be sent to: {}", 
                       order.getUser().getEmail());
        }
    }

    @Override
    public void sendOrderShippedEmail(com.ecommerce.entities.Order order) {
        String subject = "Your Order Has Been Shipped - " + order.getOrderNumber() + " - VYNTRA";
        String htmlContent = buildOrderShippedEmail(order);
        
        logger.info("📧 ORDER SHIPPED EMAIL - Order: {} - To: {}", 
                   order.getOrderNumber(), order.getUser().getEmail());
        
        if (mailEnabled && mailSender != null) {
            try {
                sendHtmlEmail(order.getUser().getEmail(), subject, htmlContent);
                logger.info("✅ Order shipped email sent successfully");
            } catch (Exception e) {
                logger.error("❌ Failed to send order shipped email: {}", e.getMessage());
            }
        } else {
            logger.warn("⚠️  Email sending disabled. Order shipped email would be sent to: {}", 
                       order.getUser().getEmail());
            logger.warn("⚠️  DIAGNOSTIC: mailEnabled={}, mailSender={}, fromEmail={}", 
                       mailEnabled, mailSender != null ? "NOT NULL" : "NULL", fromEmail);
            logger.warn("⚠️  SMTP Username: {}, Password: {}", 
                       mailUsername.isEmpty() ? "NOT SET" : mailUsername, 
                       mailPassword.isEmpty() ? "NOT SET" : "SET");
            if (mailSender == null) {
                logger.error("❌ JavaMailSender is NULL! Spring Boot couldn't create it.");
                logger.error("❌ This usually means email credentials are placeholders or invalid.");
                logger.error("❌ Fix: Update spring.mail.username and spring.mail.password in application.properties");
                logger.error("❌ Then RESTART the application.");
            } else if (!mailEnabled) {
                logger.error("❌ Email is DISABLED! Set mail.enabled=true in application.properties");
            }
        }
    }

    @Override
    public void sendOrderDeliveredEmail(com.ecommerce.entities.Order order) {
        String subject = "Your Order Has Been Delivered - " + order.getOrderNumber() + " - VYNTRA";
        String htmlContent = buildOrderDeliveredEmail(order);
        
        logger.info("📧 ORDER DELIVERED EMAIL - Order: {} - To: {}", 
                   order.getOrderNumber(), order.getUser().getEmail());
        
        if (mailEnabled && mailSender != null) {
            try {
                sendHtmlEmail(order.getUser().getEmail(), subject, htmlContent);
                logger.info("✅ Order delivered email sent successfully");
            } catch (Exception e) {
                logger.error("❌ Failed to send order delivered email: {}", e.getMessage());
            }
        } else {
            logger.warn("⚠️  Email sending disabled. Order delivered email would be sent to: {}", 
                       order.getUser().getEmail());
        }
    }

    @Override
    public void sendOrderCancelledEmail(com.ecommerce.entities.Order order) {
        String subject = "Order Cancelled - " + order.getOrderNumber() + " - VYNTRA";
        String htmlContent = buildOrderCancelledEmail(order);
        
        logger.info("📧 ORDER CANCELLED EMAIL - Order: {} - To: {}", 
                   order.getOrderNumber(), order.getUser().getEmail());
        
        if (mailEnabled && mailSender != null) {
            try {
                sendHtmlEmail(order.getUser().getEmail(), subject, htmlContent);
                logger.info("✅ Order cancelled email sent successfully");
            } catch (Exception e) {
                logger.error("❌ Failed to send order cancelled email: {}", e.getMessage());
            }
        } else {
            logger.warn("⚠️  Email sending disabled. Order cancelled email would be sent to: {}", 
                       order.getUser().getEmail());
        }
    }

    @Override
    public void sendOrderStatusUpdateEmail(com.ecommerce.entities.Order order, String newStatus) {
        String subject = "Order Status Updated - " + order.getOrderNumber() + " - VYNTRA";
        String htmlContent = buildOrderStatusUpdateEmail(order, newStatus);
        
        logger.info("📧 ORDER STATUS UPDATE EMAIL - Order: {} - Status: {} - To: {}", 
                   order.getOrderNumber(), newStatus, order.getUser().getEmail());
        
        if (mailEnabled && mailSender != null) {
            try {
                sendHtmlEmail(order.getUser().getEmail(), subject, htmlContent);
                logger.info("✅ Order status update email sent successfully");
            } catch (Exception e) {
                logger.error("❌ Failed to send order status update email: {}", e.getMessage());
            }
        } else {
            logger.warn("⚠️  Email sending disabled. Order status update email would be sent to: {}", 
                       order.getUser().getEmail());
        }
    }

    // Email Template Builders
    private String buildOrderConfirmationEmail(com.ecommerce.entities.Order order) {
        StringBuilder itemsHtml = new StringBuilder();
        if (order.getOrderItems() != null) {
            for (com.ecommerce.entities.OrderItem item : order.getOrderItems()) {
                itemsHtml.append("<tr>")
                        .append("<td>").append(item.getProduct() != null ? item.getProduct().getName() : "N/A").append("</td>")
                        .append("<td>").append(item.getQuantity()).append("</td>")
                        .append("<td>₹").append(item.getPrice() != null ? item.getPrice().toPlainString() : "0").append("</td>")
                        .append("<td>₹").append(item.getTotalPrice() != null ? item.getTotalPrice().toPlainString() : "0").append("</td>")
                        .append("</tr>");
            }
        }
        
        return "<!DOCTYPE html>" +
                "<html>" +
                "<head>" +
                "<style>" +
                "body { font-family: Arial, sans-serif; line-height: 1.6; color: #333; }" +
                ".container { max-width: 600px; margin: 0 auto; padding: 20px; }" +
                ".header { background: linear-gradient(135deg, #28a745 0%, #20c997 100%); color: white; padding: 30px; text-align: center; border-radius: 8px 8px 0 0; }" +
                ".content { background: #f9f9f9; padding: 30px; border-radius: 0 0 8px 8px; }" +
                ".order-details { background: white; padding: 20px; border-radius: 5px; margin: 20px 0; }" +
                ".items-table { width: 100%; border-collapse: collapse; margin: 15px 0; }" +
                ".items-table th, .items-table td { padding: 10px; text-align: left; border-bottom: 1px solid #ddd; }" +
                ".items-table th { background-color: #f8f9fa; font-weight: 600; }" +
                ".total { text-align: right; font-size: 18px; font-weight: 600; color: #28a745; margin-top: 15px; }" +
                ".button { display: inline-block; padding: 12px 30px; background-color: #007bff; color: white; text-decoration: none; border-radius: 5px; margin: 20px 0; }" +
                ".footer { text-align: center; margin-top: 30px; color: #666; font-size: 12px; }" +
                "</style>" +
                "</head>" +
                "<body>" +
                "<div class='container'>" +
                "<div class='header'>" +
                "<h1>✅ Order Confirmed!</h1>" +
                "<p>Order #" + order.getOrderNumber() + "</p>" +
                "</div>" +
                "<div class='content'>" +
                "<p>Dear " + order.getUser().getFirstName() + " " + order.getUser().getLastName() + ",</p>" +
                "<p>Thank you for your order! We've received your order and are preparing it for shipment.</p>" +
                "<div class='order-details'>" +
                "<h3>Order Details</h3>" +
                "<p><strong>Order Number:</strong> " + order.getOrderNumber() + "</p>" +
                "<p><strong>Order Date:</strong> " + (order.getCreatedAt() != null ? order.getCreatedAt().toString() : "N/A") + "</p>" +
                "<p><strong>Payment Status:</strong> " + (order.getPaymentStatus() != null ? order.getPaymentStatus().toString() : "N/A") + "</p>" +
                "<h4>Items Ordered:</h4>" +
                "<table class='items-table'>" +
                "<thead><tr><th>Product</th><th>Quantity</th><th>Price</th><th>Total</th></tr></thead>" +
                "<tbody>" + itemsHtml.toString() + "</tbody>" +
                "</table>" +
                "<div class='total'>Total Amount: ₹" + (order.getFinalAmount() != null ? order.getFinalAmount().toPlainString() : "0") + "</div>" +
                "</div>" +
                "<a href='http://localhost:3000/orders/" + order.getId() + "' class='button'>View Order Details</a>" +
                "<p>We'll send you another email when your order ships.</p>" +
                "<p>Best regards,<br>VYNTRA Team</p>" +
                "</div>" +
                "<div class='footer'>" +
                "<p>This is an automated email. Please do not reply.</p>" +
                "<p>&copy; 2024 VYNTRA. All rights reserved.</p>" +
                "</div>" +
                "</div>" +
                "</body>" +
                "</html>";
    }

    private String buildOrderShippedEmail(com.ecommerce.entities.Order order) {
        return "<!DOCTYPE html>" +
                "<html>" +
                "<head>" +
                "<style>" +
                "body { font-family: Arial, sans-serif; line-height: 1.6; color: #333; }" +
                ".container { max-width: 600px; margin: 0 auto; padding: 20px; }" +
                ".header { background: linear-gradient(135deg, #007bff 0%, #0056b3 100%); color: white; padding: 30px; text-align: center; border-radius: 8px 8px 0 0; }" +
                ".content { background: #f9f9f9; padding: 30px; border-radius: 0 0 8px 8px; }" +
                ".info-box { background: #e7f3ff; padding: 15px; border-left: 4px solid #007bff; margin: 20px 0; }" +
                ".button { display: inline-block; padding: 12px 30px; background-color: #007bff; color: white; text-decoration: none; border-radius: 5px; margin: 20px 0; }" +
                ".footer { text-align: center; margin-top: 30px; color: #666; font-size: 12px; }" +
                "</style>" +
                "</head>" +
                "<body>" +
                "<div class='container'>" +
                "<div class='header'>" +
                "<h1>🚚 Your Order Has Been Shipped!</h1>" +
                "<p>Order #" + order.getOrderNumber() + "</p>" +
                "</div>" +
                "<div class='content'>" +
                "<p>Dear " + order.getUser().getFirstName() + " " + order.getUser().getLastName() + ",</p>" +
                "<p>Great news! Your order has been shipped and is on its way to you.</p>" +
                "<div class='info-box'>" +
                "<p><strong>Order Number:</strong> " + order.getOrderNumber() + "</p>" +
                "<p><strong>Shipped Date:</strong> " + (order.getShippedDate() != null ? order.getShippedDate().toString() : "N/A") + "</p>" +
                "<p><strong>Estimated Delivery:</strong> 3-5 business days</p>" +
                "</div>" +
                "<p>You can track your order status anytime from your account.</p>" +
                "<a href='http://localhost:3000/orders/" + order.getId() + "' class='button'>Track Your Order</a>" +
                "<p>Thank you for shopping with VYNTRA!</p>" +
                "<p>Best regards,<br>VYNTRA Team</p>" +
                "</div>" +
                "<div class='footer'>" +
                "<p>This is an automated email. Please do not reply.</p>" +
                "<p>&copy; 2024 VYNTRA. All rights reserved.</p>" +
                "</div>" +
                "</div>" +
                "</body>" +
                "</html>";
    }

    private String buildOrderDeliveredEmail(com.ecommerce.entities.Order order) {
        return "<!DOCTYPE html>" +
                "<html>" +
                "<head>" +
                "<style>" +
                "body { font-family: Arial, sans-serif; line-height: 1.6; color: #333; }" +
                ".container { max-width: 600px; margin: 0 auto; padding: 20px; }" +
                ".header { background: linear-gradient(135deg, #28a745 0%, #20c997 100%); color: white; padding: 30px; text-align: center; border-radius: 8px 8px 0 0; }" +
                ".content { background: #f9f9f9; padding: 30px; border-radius: 0 0 8px 8px; }" +
                ".info-box { background: #d4edda; padding: 15px; border-left: 4px solid #28a745; margin: 20px 0; }" +
                ".button { display: inline-block; padding: 12px 30px; background-color: #28a745; color: white; text-decoration: none; border-radius: 5px; margin: 20px 0; }" +
                ".footer { text-align: center; margin-top: 30px; color: #666; font-size: 12px; }" +
                "</style>" +
                "</head>" +
                "<body>" +
                "<div class='container'>" +
                "<div class='header'>" +
                "<h1>🎉 Order Delivered!</h1>" +
                "<p>Order #" + order.getOrderNumber() + "</p>" +
                "</div>" +
                "<div class='content'>" +
                "<p>Dear " + order.getUser().getFirstName() + " " + order.getUser().getLastName() + ",</p>" +
                "<p>Your order has been successfully delivered!</p>" +
                "<div class='info-box'>" +
                "<p><strong>Order Number:</strong> " + order.getOrderNumber() + "</p>" +
                "<p><strong>Delivered Date:</strong> " + (order.getDeliveredDate() != null ? order.getDeliveredDate().toString() : "N/A") + "</p>" +
                "</div>" +
                "<p>We hope you're happy with your purchase. If you have any questions or concerns, please don't hesitate to contact us.</p>" +
                "<p>We'd love to hear your feedback! Please consider leaving a review for the products you purchased.</p>" +
                "<a href='http://localhost:3000/orders/" + order.getId() + "' class='button'>View Order & Leave Review</a>" +
                "<p>Thank you for shopping with VYNTRA!</p>" +
                "<p>Best regards,<br>VYNTRA Team</p>" +
                "</div>" +
                "<div class='footer'>" +
                "<p>This is an automated email. Please do not reply.</p>" +
                "<p>&copy; 2024 VYNTRA. All rights reserved.</p>" +
                "</div>" +
                "</div>" +
                "</body>" +
                "</html>";
    }

    private String buildOrderCancelledEmail(com.ecommerce.entities.Order order) {
        return "<!DOCTYPE html>" +
                "<html>" +
                "<head>" +
                "<style>" +
                "body { font-family: Arial, sans-serif; line-height: 1.6; color: #333; }" +
                ".container { max-width: 600px; margin: 0 auto; padding: 20px; }" +
                ".header { background: linear-gradient(135deg, #dc3545 0%, #c82333 100%); color: white; padding: 30px; text-align: center; border-radius: 8px 8px 0 0; }" +
                ".content { background: #f9f9f9; padding: 30px; border-radius: 0 0 8px 8px; }" +
                ".info-box { background: #f8d7da; padding: 15px; border-left: 4px solid #dc3545; margin: 20px 0; }" +
                ".button { display: inline-block; padding: 12px 30px; background-color: #007bff; color: white; text-decoration: none; border-radius: 5px; margin: 20px 0; }" +
                ".footer { text-align: center; margin-top: 30px; color: #666; font-size: 12px; }" +
                "</style>" +
                "</head>" +
                "<body>" +
                "<div class='container'>" +
                "<div class='header'>" +
                "<h1>Order Cancelled</h1>" +
                "<p>Order #" + order.getOrderNumber() + "</p>" +
                "</div>" +
                "<div class='content'>" +
                "<p>Dear " + order.getUser().getFirstName() + " " + order.getUser().getLastName() + ",</p>" +
                "<p>We're sorry to inform you that your order has been cancelled.</p>" +
                "<div class='info-box'>" +
                "<p><strong>Order Number:</strong> " + order.getOrderNumber() + "</p>" +
                "<p><strong>Order Amount:</strong> ₹" + (order.getFinalAmount() != null ? order.getFinalAmount().toPlainString() : "0") + "</p>" +
                "</div>" +
                "<p>If payment was made, a refund will be processed within 5-7 business days.</p>" +
                "<p>If you have any questions about this cancellation, please contact our support team.</p>" +
                "<a href='http://localhost:3000/orders' class='button'>View My Orders</a>" +
                "<p>We apologize for any inconvenience.</p>" +
                "<p>Best regards,<br>VYNTRA Team</p>" +
                "</div>" +
                "<div class='footer'>" +
                "<p>This is an automated email. Please do not reply.</p>" +
                "<p>&copy; 2024 VYNTRA. All rights reserved.</p>" +
                "</div>" +
                "</div>" +
                "</body>" +
                "</html>";
    }

    private String buildOrderStatusUpdateEmail(com.ecommerce.entities.Order order, String newStatus) {
        return "<!DOCTYPE html>" +
                "<html>" +
                "<head>" +
                "<style>" +
                "body { font-family: Arial, sans-serif; line-height: 1.6; color: #333; }" +
                ".container { max-width: 600px; margin: 0 auto; padding: 20px; }" +
                ".header { background: linear-gradient(135deg, #6c757d 0%, #5a6268 100%); color: white; padding: 30px; text-align: center; border-radius: 8px 8px 0 0; }" +
                ".content { background: #f9f9f9; padding: 30px; border-radius: 0 0 8px 8px; }" +
                ".info-box { background: #e9ecef; padding: 15px; border-left: 4px solid #6c757d; margin: 20px 0; }" +
                ".button { display: inline-block; padding: 12px 30px; background-color: #007bff; color: white; text-decoration: none; border-radius: 5px; margin: 20px 0; }" +
                ".footer { text-align: center; margin-top: 30px; color: #666; font-size: 12px; }" +
                "</style>" +
                "</head>" +
                "<body>" +
                "<div class='container'>" +
                "<div class='header'>" +
                "<h1>Order Status Updated</h1>" +
                "<p>Order #" + order.getOrderNumber() + "</p>" +
                "</div>" +
                "<div class='content'>" +
                "<p>Dear " + order.getUser().getFirstName() + " " + order.getUser().getLastName() + ",</p>" +
                "<p>Your order status has been updated.</p>" +
                "<div class='info-box'>" +
                "<p><strong>Order Number:</strong> " + order.getOrderNumber() + "</p>" +
                "<p><strong>New Status:</strong> " + newStatus + "</p>" +
                "</div>" +
                "<a href='http://localhost:3000/orders/" + order.getId() + "' class='button'>View Order Details</a>" +
                "<p>Best regards,<br>VYNTRA Team</p>" +
                "</div>" +
                "<div class='footer'>" +
                "<p>This is an automated email. Please do not reply.</p>" +
                "<p>&copy; 2024 VYNTRA. All rights reserved.</p>" +
                "</div>" +
                "</div>" +
                "</body>" +
                "</html>";
    }

    @Override
    public void sendPasswordResetOtpEmail(String email, String otp, String firstName) {
        String subject = "Password Reset OTP - VYNTRA";
        String htmlContent = buildPasswordResetOtpEmail(otp, firstName);
        sendHtmlEmail(email, subject, htmlContent);
    }

    private String buildPasswordResetOtpEmail(String otp, String firstName) {
        return "<!DOCTYPE html>" +
                "<html>" +
                "<head>" +
                "<meta charset='UTF-8'>" +
                "<meta name='viewport' content='width=device-width, initial-scale=1.0'>" +
                "<title>Password Reset OTP</title>" +
                "</head>" +
                "<body style='margin: 0; padding: 0; font-family: Arial, sans-serif; background-color: #f4f4f4;'>" +
                "<div style='max-width: 600px; margin: 0 auto; background-color: #ffffff; padding: 20px;'>" +
                "<div style='text-align: center; padding: 20px; background-color: #0ea5e9; color: white;'>" +
                "<h1 style='margin: 0;'>VYNTRA</h1>" +
                "</div>" +
                "<div style='padding: 30px 20px;'>" +
                "<h2 style='color: #333;'>Password Reset Request</h2>" +
                "<p style='color: #666; font-size: 16px; line-height: 1.6;'>" +
                "Hello " + (firstName != null ? firstName : "User") + "," +
                "</p>" +
                "<p style='color: #666; font-size: 16px; line-height: 1.6;'>" +
                "You have requested to reset your password. Please use the following OTP to complete the process:" +
                "</p>" +
                "<div style='text-align: center; margin: 30px 0;'>" +
                "<div style='display: inline-block; padding: 20px 40px; background-color: #f0f9ff; border: 2px dashed #0ea5e9; border-radius: 8px;'>" +
                "<p style='margin: 0; font-size: 32px; font-weight: bold; color: #0ea5e9; letter-spacing: 5px;'>" + otp + "</p>" +
                "</div>" +
                "</div>" +
                "<p style='color: #666; font-size: 14px; line-height: 1.6;'>" +
                "<strong>This OTP will expire in 10 minutes.</strong>" +
                "</p>" +
                "<p style='color: #666; font-size: 14px; line-height: 1.6;'>" +
                "If you did not request this password reset, please ignore this email or contact support if you have concerns." +
                "</p>" +
                "<div style='margin-top: 30px; padding-top: 20px; border-top: 1px solid #eee;'>" +
                "<p style='color: #999; font-size: 12px; margin: 0;'>" +
                "© 2024 VYNTRA. All rights reserved." +
                "</p>" +
                "</div>" +
                "</div>" +
                "</div>" +
                "</body>" +
                "</html>";
    }
}

