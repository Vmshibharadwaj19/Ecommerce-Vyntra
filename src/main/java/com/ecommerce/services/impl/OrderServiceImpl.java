package com.ecommerce.services.impl;

import com.ecommerce.dto.CheckoutRequest;
import com.ecommerce.dto.OrderDto;
import com.ecommerce.dto.OrderItemDto;
import com.ecommerce.entities.*;
import com.ecommerce.exceptions.ResourceNotFoundException;
import com.ecommerce.repositories.*;
import com.ecommerce.services.OrderService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private com.ecommerce.services.NotificationService notificationService;

    @Override
    public OrderDto createOrder(Long userId, Long addressId, String razorpayOrderId,
                               String razorpayPaymentId, String razorpaySignature) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        Address address = addressRepository.findById(addressId)
                .orElseThrow(() -> new ResourceNotFoundException("Address", "id", addressId));

        if (!address.getUser().getId().equals(userId)) {
            throw new RuntimeException("Address does not belong to user");
        }

        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Cart is empty"));

        if (cart.getCartItems().isEmpty()) {
            throw new RuntimeException("Cart is empty");
        }

        Order order = new Order();
        order.setOrderNumber("ORD-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        order.setUser(user);
        order.setShippingAddress(address);
        order.setRazorpayOrderId(razorpayOrderId);
        order.setRazorpayPaymentId(razorpayPaymentId);
        order.setRazorpaySignature(razorpaySignature);
        order.setStatus(Order.OrderStatus.CONFIRMED);
        order.setPaymentStatus(Order.PaymentStatus.PAID);

        BigDecimal totalAmount = BigDecimal.ZERO;

        for (CartItem cartItem : cart.getCartItems()) {
            Product product = cartItem.getProduct();
            
            if (product.getStockQuantity() < cartItem.getQuantity()) {
                throw new RuntimeException("Insufficient stock for product: " + product.getName());
            }

            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(product);
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setPrice(cartItem.getPrice());
            orderItem.setTotalPrice(cartItem.getPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity())));
            order.getOrderItems().add(orderItem);

            totalAmount = totalAmount.add(orderItem.getTotalPrice());

            // Deduct inventory
            product.setStockQuantity(product.getStockQuantity() - cartItem.getQuantity());
            productRepository.save(product);
        }

        order.setTotalAmount(totalAmount);
        order.setShippingCharges(BigDecimal.ZERO);
        order.setDiscountAmount(BigDecimal.ZERO);
        order.setFinalAmount(totalAmount);

        Order savedOrder = orderRepository.save(order);

        // Create payment record
        Payment payment = new Payment();
        payment.setOrder(savedOrder);
        payment.setPaymentId(razorpayPaymentId);
        payment.setRazorpayOrderId(razorpayOrderId);
        payment.setRazorpayPaymentId(razorpayPaymentId);
        payment.setRazorpaySignature(razorpaySignature);
        payment.setAmount(savedOrder.getFinalAmount());
        payment.setPaymentMethod(Payment.PaymentMethod.RAZORPAY);
        payment.setStatus(Payment.PaymentStatus.SUCCESS);
        paymentRepository.save(payment);

        // Clear cart items - do this after order is fully saved
        // First, get cart items to delete (copy the list to avoid lazy loading issues)
        List<CartItem> itemsToDelete = new ArrayList<>(cart.getCartItems());
        
        // Clear the cart items collection from the cart entity
        cart.getCartItems().clear();
        cart.setTotalAmount(BigDecimal.ZERO);
        cartRepository.save(cart);
        
        // Now delete cart items directly using repository
        for (CartItem item : itemsToDelete) {
            cartItemRepository.delete(item);
        }

        // Send order confirmation email to customer
        try {
            notificationService.sendOrderConfirmationEmail(savedOrder);
        } catch (Exception e) {
            // Log error but don't fail the order creation
            System.err.println("Error sending order confirmation email: " + e.getMessage());
        }

        return mapToDto(savedOrder);
    }

    @Override
    public OrderDto createOrderWithPaymentMethod(Long userId, CheckoutRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        Address address = addressRepository.findById(request.getAddressId())
                .orElseThrow(() -> new ResourceNotFoundException("Address", "id", request.getAddressId()));

        if (!address.getUser().getId().equals(userId)) {
            throw new RuntimeException("Address does not belong to user");
        }

        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Cart is empty"));

        if (cart.getCartItems().isEmpty()) {
            throw new RuntimeException("Cart is empty");
        }

        // Create a copy of cart items to avoid lazy loading issues
        List<CartItem> cartItemsCopy = new ArrayList<>(cart.getCartItems());
        
        // Initialize the list to ensure it's loaded
        cartItemsCopy.size();

        Order order = new Order();
        order.setOrderNumber("ORD-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        order.setUser(user);
        order.setShippingAddress(address);

        BigDecimal totalAmount = BigDecimal.ZERO;

        for (CartItem cartItem : cartItemsCopy) {
            if (cartItem == null || cartItem.getProduct() == null || cartItem.getProduct().getId() == null) {
                continue; // Skip invalid cart items
            }
            // Fetch product fresh to avoid detached entity issues
            Product product = productRepository.findById(cartItem.getProduct().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Product", "id", cartItem.getProduct().getId()));
            
            if (product.getStockQuantity() == null || cartItem.getQuantity() == null || 
                product.getStockQuantity() < cartItem.getQuantity()) {
                throw new RuntimeException("Insufficient stock for product: " + (product.getName() != null ? product.getName() : "Unknown"));
            }

            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(product);
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setPrice(cartItem.getPrice());
            orderItem.setTotalPrice(cartItem.getPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity())));
            order.getOrderItems().add(orderItem);

            totalAmount = totalAmount.add(orderItem.getTotalPrice());

            // Deduct inventory
            product.setStockQuantity(product.getStockQuantity() - cartItem.getQuantity());
            productRepository.save(product);
        }

        order.setTotalAmount(totalAmount);
        order.setShippingCharges(BigDecimal.ZERO);
        order.setDiscountAmount(BigDecimal.ZERO);
        order.setFinalAmount(totalAmount);

        // Handle payment method
        String paymentMethod = request.getPaymentMethod() != null ? request.getPaymentMethod() : "COD";
        
        if ("RAZORPAY".equalsIgnoreCase(paymentMethod)) {
            // Razorpay payment
            order.setRazorpayOrderId(request.getRazorpayOrderId());
            order.setRazorpayPaymentId(request.getRazorpayPaymentId());
            order.setRazorpaySignature(request.getRazorpaySignature());
            order.setStatus(Order.OrderStatus.CONFIRMED);
            order.setPaymentStatus(Order.PaymentStatus.PAID);
        } else if ("COD".equalsIgnoreCase(paymentMethod)) {
            // Cash on Delivery - set placeholder values
            order.setRazorpayOrderId("COD_ORDER_" + System.currentTimeMillis());
            order.setRazorpayPaymentId("COD_PAYMENT_" + System.currentTimeMillis());
            order.setRazorpaySignature("");
            order.setStatus(Order.OrderStatus.CONFIRMED);
            order.setPaymentStatus(Order.PaymentStatus.PENDING);
        } else if ("TEST".equalsIgnoreCase(paymentMethod)) {
            // Test/Demo payment (no API keys needed)
            order.setRazorpayOrderId("test_order_" + System.currentTimeMillis());
            order.setRazorpayPaymentId("test_payment_" + System.currentTimeMillis());
            order.setRazorpaySignature("test_signature");
            order.setStatus(Order.OrderStatus.CONFIRMED);
            order.setPaymentStatus(Order.PaymentStatus.PAID);
        } else {
            // Default to COD
            order.setRazorpayOrderId("COD_ORDER_" + System.currentTimeMillis());
            order.setRazorpayPaymentId("COD_PAYMENT_" + System.currentTimeMillis());
            order.setRazorpaySignature("");
            order.setStatus(Order.OrderStatus.CONFIRMED);
            order.setPaymentStatus(Order.PaymentStatus.PENDING);
        }

        Order savedOrder = orderRepository.save(order);

        // Create payment record
        Payment payment = new Payment();
        payment.setOrder(savedOrder);
        payment.setAmount(savedOrder.getFinalAmount());
        
        if ("RAZORPAY".equalsIgnoreCase(paymentMethod)) {
            payment.setPaymentId(request.getRazorpayPaymentId());
            payment.setRazorpayOrderId(request.getRazorpayOrderId());
            payment.setRazorpayPaymentId(request.getRazorpayPaymentId());
            payment.setRazorpaySignature(request.getRazorpaySignature());
            payment.setPaymentMethod(Payment.PaymentMethod.RAZORPAY);
            payment.setStatus(Payment.PaymentStatus.SUCCESS);
        } else if ("COD".equalsIgnoreCase(paymentMethod)) {
            payment.setPaymentId("COD_" + System.currentTimeMillis());
            // Set placeholder values instead of null to avoid database constraint issues
            payment.setRazorpayOrderId("COD_ORDER_" + System.currentTimeMillis());
            payment.setRazorpayPaymentId("COD_PAYMENT_" + System.currentTimeMillis());
            payment.setRazorpaySignature("");
            payment.setPaymentMethod(Payment.PaymentMethod.COD);
            payment.setStatus(Payment.PaymentStatus.PENDING);
        } else if ("TEST".equalsIgnoreCase(paymentMethod)) {
            payment.setPaymentId("TEST_" + System.currentTimeMillis());
            payment.setRazorpayOrderId("test_order_" + System.currentTimeMillis());
            payment.setRazorpayPaymentId("test_payment_" + System.currentTimeMillis());
            payment.setRazorpaySignature("test_signature");
            payment.setPaymentMethod(Payment.PaymentMethod.RAZORPAY);
            payment.setStatus(Payment.PaymentStatus.SUCCESS);
        } else {
            // Default to COD
            payment.setPaymentId("COD_" + System.currentTimeMillis());
            payment.setRazorpayOrderId("COD_ORDER_" + System.currentTimeMillis());
            payment.setRazorpayPaymentId("COD_PAYMENT_" + System.currentTimeMillis());
            payment.setRazorpaySignature("");
        payment.setPaymentMethod(Payment.PaymentMethod.COD);
        payment.setStatus(Payment.PaymentStatus.PENDING);
        }
        
        paymentRepository.save(payment);

        // Clear cart items - do this after order is fully saved
        // First, get cart items to delete (copy the list to avoid lazy loading issues)
        List<CartItem> itemsToDelete = new ArrayList<>(cart.getCartItems());
        
        // Clear the cart items collection from the cart entity
        cart.getCartItems().clear();
        cart.setTotalAmount(BigDecimal.ZERO);
        cartRepository.save(cart);
        
        // Now delete cart items directly using repository
        for (CartItem item : itemsToDelete) {
            cartItemRepository.delete(item);
        }

        // Send order confirmation email to customer
        try {
            notificationService.sendOrderConfirmationEmail(savedOrder);
        } catch (Exception e) {
            // Log error but don't fail the order creation
            System.err.println("Error sending order confirmation email: " + e.getMessage());
        }

        return mapToDto(savedOrder);
    }

    @Override
    public OrderDto getOrderById(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order", "id", orderId));
        return mapToDto(order);
    }

    @Override
    public List<OrderDto> getUserOrders(Long userId) {
        List<Order> orders = orderRepository.findByUserIdOrderByCreatedAtDesc(userId);
        return orders.stream().map(this::mapToDto).collect(Collectors.toList());
    }

    @Override
    public OrderDto updateOrderStatus(Long orderId, String status) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order", "id", orderId));

        try {
            Order.OrderStatus orderStatus = Order.OrderStatus.valueOf(status.toUpperCase());
            order.setStatus(orderStatus);

            Order.OrderStatus oldStatus = order.getStatus();
            
            if (orderStatus == Order.OrderStatus.SHIPPED) {
                order.setShippedDate(LocalDateTime.now());
            } else if (orderStatus == Order.OrderStatus.DELIVERED) {
                order.setDeliveredDate(LocalDateTime.now());
            }

            Order savedOrder = orderRepository.save(order);
            
            // Send email notification to customer about status change
            try {
                if (orderStatus == Order.OrderStatus.SHIPPED) {
                    notificationService.sendOrderShippedEmail(savedOrder);
                } else if (orderStatus == Order.OrderStatus.DELIVERED) {
                    notificationService.sendOrderDeliveredEmail(savedOrder);
                } else if (orderStatus == Order.OrderStatus.CANCELLED) {
                    notificationService.sendOrderCancelledEmail(savedOrder);
                } else if (oldStatus != orderStatus) {
                    // For any other status change
                    notificationService.sendOrderStatusUpdateEmail(savedOrder, status);
                }
            } catch (Exception e) {
                // Log error but don't fail the order update
                System.err.println("Error sending order status email: " + e.getMessage());
            }
            
            return mapToDto(savedOrder);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid order status: " + status);
        }
    }

    @Override
    public List<OrderDto> getSellerOrders(Long sellerId) {
        List<Order> orders = orderRepository.findBySellerId(sellerId, 
                org.springframework.data.domain.PageRequest.of(0, 1000)).getContent();
        return orders.stream().map(this::mapToDto).collect(Collectors.toList());
    }

    @Override
    public List<OrderDto> getAllOrders() {
        List<Order> orders = orderRepository.findAll();
        return orders.stream().map(this::mapToDto).collect(Collectors.toList());
    }

    private OrderDto mapToDto(Order order) {
        if (order == null) {
            return null;
        }
        try {
            OrderDto dto = modelMapper.map(order, OrderDto.class);
            if (order.getUser() != null) {
                dto.setUserId(order.getUser().getId());
                String firstName = order.getUser().getFirstName() != null ? order.getUser().getFirstName() : "";
                String lastName = order.getUser().getLastName() != null ? order.getUser().getLastName() : "";
                dto.setUserName(firstName + " " + lastName);
            }
            
            if (order.getShippingAddress() != null) {
                dto.setShippingAddress(modelMapper.map(order.getShippingAddress(), com.ecommerce.dto.AddressDto.class));
            }

            List<OrderItemDto> orderItemDtos = List.of();
            if (order.getOrderItems() != null && !order.getOrderItems().isEmpty()) {
                orderItemDtos = order.getOrderItems().stream()
                        .filter(item -> item != null)
                        .map(this::mapOrderItemToDto)
                        .filter(dtoItem -> dtoItem != null)
                        .collect(Collectors.toList());
            }
            dto.setOrderItems(orderItemDtos);

            return dto;
        } catch (Exception e) {
            return null;
        }
    }

    private OrderItemDto mapOrderItemToDto(OrderItem orderItem) {
        if (orderItem == null) {
            return null;
        }
        try {
            OrderItemDto dto = modelMapper.map(orderItem, OrderItemDto.class);
            if (orderItem.getProduct() != null) {
                dto.setProductId(orderItem.getProduct().getId());
                dto.setProductName(orderItem.getProduct().getName());
                if (orderItem.getProduct().getImages() != null && !orderItem.getProduct().getImages().isEmpty()) {
                    dto.setProductImage(orderItem.getProduct().getImages().get(0));
                }
            }
            return dto;
        } catch (Exception e) {
            return null;
        }
    }
}

