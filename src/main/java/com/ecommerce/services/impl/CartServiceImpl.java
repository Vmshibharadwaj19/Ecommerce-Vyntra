package com.ecommerce.services.impl;

import com.ecommerce.dto.CartDto;
import com.ecommerce.dto.CartItemDto;
import com.ecommerce.entities.Cart;
import com.ecommerce.entities.CartItem;
import com.ecommerce.entities.Product;
import com.ecommerce.entities.User;
import com.ecommerce.exceptions.ResourceNotFoundException;
import com.ecommerce.repositories.CartItemRepository;
import com.ecommerce.repositories.CartRepository;
import com.ecommerce.repositories.ProductRepository;
import com.ecommerce.repositories.UserRepository;
import com.ecommerce.services.CartService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class CartServiceImpl implements CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public CartDto getCart(Long userId) {
        Cart cart = getOrCreateCart(userId);
        return mapToDto(cart);
    }

    @Override
    public CartDto addToCart(Long userId, Long productId, Integer quantity) {
        Cart cart = getOrCreateCart(userId);
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", productId));

        if (!product.getIsActive() || !product.getIsApproved()) {
            throw new RuntimeException("Product is not available");
        }

        if (product.getStockQuantity() < quantity) {
            throw new RuntimeException("Insufficient stock");
        }

        CartItem existingItem = cartItemRepository.findByCartIdAndProductId(cart.getId(), productId)
                .orElse(null);

        if (existingItem != null) {
            int newQuantity = existingItem.getQuantity() + quantity;
            if (newQuantity > product.getStockQuantity()) {
                throw new RuntimeException("Insufficient stock");
            }
            existingItem.setQuantity(newQuantity);
            existingItem.setPrice(product.getDiscountPrice());
            cartItemRepository.save(existingItem);
        } else {
            CartItem cartItem = new CartItem();
            cartItem.setCart(cart);
            cartItem.setProduct(product);
            cartItem.setQuantity(quantity);
            cartItem.setPrice(product.getDiscountPrice());
            cart.getCartItems().add(cartItem);
            cartItemRepository.save(cartItem);
        }

        updateCartTotal(cart);
        return mapToDto(cart);
    }

    @Override
    public CartDto updateCartItem(Long userId, Long cartItemId, Integer quantity) {
        Cart cart = getOrCreateCart(userId);
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new ResourceNotFoundException("CartItem", "id", cartItemId));

        if (!cartItem.getCart().getId().equals(cart.getId())) {
            throw new RuntimeException("Cart item does not belong to your cart");
        }

        if (quantity <= 0) {
            cartItemRepository.delete(cartItem);
        } else {
            if (quantity > cartItem.getProduct().getStockQuantity()) {
                throw new RuntimeException("Insufficient stock");
            }
            cartItem.setQuantity(quantity);
            cartItemRepository.save(cartItem);
        }

        updateCartTotal(cart);
        return mapToDto(cart);
    }

    @Override
    public CartDto removeFromCart(Long userId, Long cartItemId) {
        Cart cart = getOrCreateCart(userId);
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new ResourceNotFoundException("CartItem", "id", cartItemId));

        if (!cartItem.getCart().getId().equals(cart.getId())) {
            throw new RuntimeException("Cart item does not belong to your cart");
        }

        cartItemRepository.delete(cartItem);
        updateCartTotal(cart);
        return mapToDto(cart);
    }

    @Override
    public void clearCart(Long userId) {
        Cart cart = getOrCreateCart(userId);
        cartItemRepository.deleteByCartId(cart.getId());
        cart.setTotalAmount(BigDecimal.ZERO);
        cartRepository.save(cart);
    }

    @Override
    public Integer getCartItemCount(Long userId) {
        Cart cart = cartRepository.findByUserId(userId).orElse(null);
        if (cart == null) {
            return 0;
        }
        return cart.getCartItems().size();
    }

    private Cart getOrCreateCart(Long userId) {
        return cartRepository.findByUserId(userId)
                .orElseGet(() -> {
                    User user = userRepository.findById(userId)
                            .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
                    Cart newCart = new Cart();
                    newCart.setUser(user);
                    newCart.setTotalAmount(BigDecimal.ZERO);
                    return cartRepository.save(newCart);
                });
    }

    private void updateCartTotal(Cart cart) {
        if (cart == null) {
            return;
        }
        BigDecimal total = BigDecimal.ZERO;
        if (cart.getCartItems() != null && !cart.getCartItems().isEmpty()) {
            total = cart.getCartItems().stream()
                    .filter(item -> item != null && item.getPrice() != null && item.getQuantity() != null)
                    .map(item -> item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
        }
        cart.setTotalAmount(total);
        cartRepository.save(cart);
    }

    private CartDto mapToDto(Cart cart) {
        if (cart == null) {
            return null;
        }
        CartDto dto = modelMapper.map(cart, CartDto.class);
        if (cart.getUser() != null) {
            dto.setUserId(cart.getUser().getId());
        }
        
        List<CartItemDto> cartItemDtos = List.of();
        if (cart.getCartItems() != null && !cart.getCartItems().isEmpty()) {
            cartItemDtos = cart.getCartItems().stream()
                    .filter(item -> item != null)
                    .map(this::mapCartItemToDto)
                    .filter(dtoItem -> dtoItem != null)
                    .collect(Collectors.toList());
        }
        dto.setCartItems(cartItemDtos);
        
        return dto;
    }

    private CartItemDto mapCartItemToDto(CartItem cartItem) {
        if (cartItem == null) {
            return null;
        }
        try {
            CartItemDto dto = modelMapper.map(cartItem, CartItemDto.class);
            if (cartItem.getProduct() != null) {
                dto.setProductId(cartItem.getProduct().getId());
                dto.setProductName(cartItem.getProduct().getName());
                if (cartItem.getProduct().getImages() != null && !cartItem.getProduct().getImages().isEmpty()) {
                    dto.setProductImage(cartItem.getProduct().getImages().get(0));
                }
                if (cartItem.getProduct().getDiscountPrice() != null) {
                    dto.setProductPrice(cartItem.getProduct().getDiscountPrice());
                }
            }
            if (cartItem.getPrice() != null && cartItem.getQuantity() != null) {
                dto.setTotalPrice(cartItem.getPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity())));
            }
            return dto;
        } catch (Exception e) {
            return null;
        }
    }
}

