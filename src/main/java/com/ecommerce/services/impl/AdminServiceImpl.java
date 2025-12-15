package com.ecommerce.services.impl;

import com.ecommerce.dto.OrderDto;
import com.ecommerce.dto.ProductDto;
import com.ecommerce.dto.UserDto;
import com.ecommerce.entities.User;
import com.ecommerce.exceptions.ResourceNotFoundException;
import com.ecommerce.repositories.OrderRepository;
import com.ecommerce.repositories.ProductRepository;
import com.ecommerce.repositories.UserRepository;
import com.ecommerce.services.AdminService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
public class AdminServiceImpl implements AdminService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private com.ecommerce.services.NotificationService notificationService;

    @Override
    public List<UserDto> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(user -> modelMapper.map(user, UserDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public UserDto blockUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
        user.setIsBlocked(true);
        User savedUser = userRepository.save(user);
        return modelMapper.map(savedUser, UserDto.class);
    }

    @Override
    public UserDto unblockUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
        user.setIsBlocked(false);
        User savedUser = userRepository.save(user);
        return modelMapper.map(savedUser, UserDto.class);
    }

    @Override
    public UserDto approveSeller(Long sellerId) {
        User seller = userRepository.findById(sellerId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", sellerId));
        
        if (seller.getRole() != User.Role.ROLE_SELLER) {
            throw new RuntimeException("User is not a seller");
        }

        seller.setIsApproved(true);
        User savedSeller = userRepository.save(seller);
        
        // Send approval notification to seller
        notificationService.sendSellerApprovalNotification(savedSeller);
        
        return modelMapper.map(savedSeller, UserDto.class);
    }

    @Override
    public UserDto rejectSeller(Long sellerId) {
        User seller = userRepository.findById(sellerId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", sellerId));
        
        if (seller.getRole() != User.Role.ROLE_SELLER) {
            throw new RuntimeException("User is not a seller");
        }

        seller.setIsApproved(false);
        User savedSeller = userRepository.save(seller);
        
        // Send rejection notification to seller
        notificationService.sendSellerRejectionNotification(savedSeller);
        
        return modelMapper.map(savedSeller, UserDto.class);
    }

    @Override
    public List<UserDto> getPendingSellers() {
        List<User> sellers = userRepository.findAll().stream()
                .filter(user -> user.getRole() == User.Role.ROLE_SELLER && !user.getIsApproved())
                .collect(Collectors.toList());
        return sellers.stream()
                .map(seller -> modelMapper.map(seller, UserDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductDto> getPendingProducts() {
        List<com.ecommerce.entities.Product> products = productRepository.findAll().stream()
                .filter(product -> product.getIsApproved() == null || !product.getIsApproved())
                .collect(Collectors.toList());
        return products.stream()
                .map(product -> {
                    ProductDto dto = modelMapper.map(product, ProductDto.class);
                    dto.setSellerId(product.getSeller().getId());
                    dto.setSellerName(product.getSeller().getFirstName() + " " + product.getSeller().getLastName());
                    dto.setCategoryId(product.getCategory().getId());
                    dto.setCategoryName(product.getCategory().getName());
                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<OrderDto> getAllOrders() {
        List<com.ecommerce.entities.Order> orders = orderRepository.findAll();
        return orders.stream()
                .map(order -> {
                    OrderDto dto = modelMapper.map(order, OrderDto.class);
                    dto.setUserId(order.getUser().getId());
                    dto.setUserName(order.getUser().getFirstName() + " " + order.getUser().getLastName());
                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Override
    public Map<String, Object> getDashboardStats() {
        Map<String, Object> stats = new HashMap<>();
        
        long totalUsers = userRepository.count();
        long totalCustomers = userRepository.findAll().stream()
                .filter(user -> user.getRole() == User.Role.ROLE_CUSTOMER)
                .count();
        long totalSellers = userRepository.findAll().stream()
                .filter(user -> user.getRole() == User.Role.ROLE_SELLER)
                .count();
        long totalProducts = productRepository.count();
        long totalOrders = orderRepository.count();
        
        BigDecimal totalRevenue = orderRepository.findAll().stream()
                .filter(order -> order.getPaymentStatus() == com.ecommerce.entities.Order.PaymentStatus.PAID)
                .map(com.ecommerce.entities.Order::getFinalAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        long pendingSellers = userRepository.findAll().stream()
                .filter(u -> u.getRole() == User.Role.ROLE_SELLER && 
                           (u.getIsApproved() == null || !u.getIsApproved()))
                .count();
        
        long pendingProducts = productRepository.findAll().stream()
                .filter(p -> p.getIsApproved() == null || !p.getIsApproved())
                .count();
        
        stats.put("totalUsers", totalUsers);
        stats.put("totalCustomers", totalCustomers);
        stats.put("totalSellers", totalSellers);
        stats.put("totalProducts", totalProducts);
        stats.put("totalOrders", totalOrders);
        stats.put("totalRevenue", totalRevenue);
        stats.put("pendingSellers", pendingSellers);
        stats.put("pendingProducts", pendingProducts);
        
        return stats;
    }
}

