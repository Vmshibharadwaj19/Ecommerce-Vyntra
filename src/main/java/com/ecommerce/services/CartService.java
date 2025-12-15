package com.ecommerce.services;

import com.ecommerce.dto.CartDto;

public interface CartService {

    CartDto getCart(Long userId);

    CartDto addToCart(Long userId, Long productId, Integer quantity);

    CartDto updateCartItem(Long userId, Long cartItemId, Integer quantity);

    CartDto removeFromCart(Long userId, Long cartItemId);

    void clearCart(Long userId);

    Integer getCartItemCount(Long userId);
}



