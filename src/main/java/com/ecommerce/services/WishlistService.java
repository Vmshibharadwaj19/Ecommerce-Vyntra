package com.ecommerce.services;

import com.ecommerce.dto.ProductDto;

import java.util.List;

public interface WishlistService {

    void addToWishlist(Long userId, Long productId);

    void removeFromWishlist(Long userId, Long productId);

    List<ProductDto> getUserWishlist(Long userId);

    Boolean isInWishlist(Long userId, Long productId);
}



