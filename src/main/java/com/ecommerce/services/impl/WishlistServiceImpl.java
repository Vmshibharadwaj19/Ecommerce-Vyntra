package com.ecommerce.services.impl;

import com.ecommerce.dto.ProductDto;
import com.ecommerce.entities.Product;
import com.ecommerce.entities.User;
import com.ecommerce.entities.Wishlist;
import com.ecommerce.exceptions.ResourceNotFoundException;
import com.ecommerce.repositories.ProductRepository;
import com.ecommerce.repositories.UserRepository;
import com.ecommerce.repositories.WishlistRepository;
import com.ecommerce.services.WishlistService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class WishlistServiceImpl implements WishlistService {

    @Autowired
    private WishlistRepository wishlistRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public void addToWishlist(Long userId, Long productId) {
        if (wishlistRepository.existsByUserIdAndProductId(userId, productId)) {
            throw new RuntimeException("Product is already in wishlist");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", productId));

        Wishlist wishlist = new Wishlist();
        wishlist.setUser(user);
        wishlist.setProduct(product);
        wishlistRepository.save(wishlist);
    }

    @Override
    public void removeFromWishlist(Long userId, Long productId) {
        Wishlist wishlist = wishlistRepository.findByUserIdAndProductId(userId, productId)
                .orElseThrow(() -> new ResourceNotFoundException("Wishlist", "productId", productId));

        if (!wishlist.getUser().getId().equals(userId)) {
            throw new RuntimeException("Wishlist item does not belong to user");
        }

        wishlistRepository.delete(wishlist);
    }

    @Override
    public List<ProductDto> getUserWishlist(Long userId) {
        List<Wishlist> wishlists = wishlistRepository.findByUserId(userId);
        return wishlists.stream()
                .map(wishlist -> {
                    ProductDto dto = modelMapper.map(wishlist.getProduct(), ProductDto.class);
                    dto.setSellerId(wishlist.getProduct().getSeller().getId());
                    dto.setSellerName(wishlist.getProduct().getSeller().getFirstName() + " " + 
                                     wishlist.getProduct().getSeller().getLastName());
                    dto.setCategoryId(wishlist.getProduct().getCategory().getId());
                    dto.setCategoryName(wishlist.getProduct().getCategory().getName());
                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Override
    public Boolean isInWishlist(Long userId, Long productId) {
        return wishlistRepository.existsByUserIdAndProductId(userId, productId);
    }
}



