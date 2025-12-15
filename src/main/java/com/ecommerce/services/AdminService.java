package com.ecommerce.services;

import com.ecommerce.dto.UserDto;
import com.ecommerce.dto.ProductDto;
import com.ecommerce.dto.OrderDto;

import java.util.List;
import java.util.Map;

public interface AdminService {

    List<UserDto> getAllUsers();

    UserDto blockUser(Long userId);

    UserDto unblockUser(Long userId);

    UserDto approveSeller(Long sellerId);

    UserDto rejectSeller(Long sellerId);

    List<UserDto> getPendingSellers();

    List<ProductDto> getPendingProducts();

    List<OrderDto> getAllOrders();

    Map<String, Object> getDashboardStats();
}



