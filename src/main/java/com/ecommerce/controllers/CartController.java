package com.ecommerce.controllers;

import com.ecommerce.dto.ApiResponse;
import com.ecommerce.dto.CartDto;
import com.ecommerce.security.UserPrincipal;
import com.ecommerce.services.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
@CrossOrigin(origins = "*")
public class CartController {

    @Autowired
    private CartService cartService;

    @GetMapping
    public ResponseEntity<ApiResponse<CartDto>> getCart(Authentication authentication) {
        try {
            if (authentication == null || authentication.getPrincipal() == null) {
                return ResponseEntity.status(401).body(ApiResponse.error("User not authenticated"));
            }
            UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
            CartDto cart = cartService.getCart(userPrincipal.getId());
            return ResponseEntity.ok(ApiResponse.success(cart));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error("Error fetching cart: " + e.getMessage()));
        }
    }

    @PostMapping("/add")
    public ResponseEntity<ApiResponse<CartDto>> addToCart(
            @RequestParam Long productId,
            @RequestParam Integer quantity,
            Authentication authentication) {
        try {
            if (authentication == null || authentication.getPrincipal() == null) {
                return ResponseEntity.status(401).body(ApiResponse.error("User not authenticated"));
            }
            if (productId == null || quantity == null || quantity <= 0) {
                return ResponseEntity.badRequest().body(ApiResponse.error("Invalid product ID or quantity"));
            }
            UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
            CartDto cart = cartService.addToCart(userPrincipal.getId(), productId, quantity);
            return ResponseEntity.ok(ApiResponse.success("Item added to cart", cart));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error("Error adding to cart: " + e.getMessage()));
        }
    }

    @PutMapping("/items/{cartItemId}")
    public ResponseEntity<ApiResponse<CartDto>> updateCartItem(
            @PathVariable Long cartItemId,
            @RequestParam Integer quantity,
            Authentication authentication) {
        try {
            if (authentication == null || authentication.getPrincipal() == null) {
                return ResponseEntity.status(401).body(ApiResponse.error("User not authenticated"));
            }
            if (cartItemId == null || quantity == null || quantity < 0) {
                return ResponseEntity.badRequest().body(ApiResponse.error("Invalid cart item ID or quantity"));
            }
            UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
            CartDto cart = cartService.updateCartItem(userPrincipal.getId(), cartItemId, quantity);
            return ResponseEntity.ok(ApiResponse.success("Cart updated", cart));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error("Error updating cart: " + e.getMessage()));
        }
    }

    @DeleteMapping("/items/{cartItemId}")
    public ResponseEntity<ApiResponse<CartDto>> removeFromCart(
            @PathVariable Long cartItemId,
            Authentication authentication) {
        try {
            if (authentication == null || authentication.getPrincipal() == null) {
                return ResponseEntity.status(401).body(ApiResponse.error("User not authenticated"));
            }
            if (cartItemId == null) {
                return ResponseEntity.badRequest().body(ApiResponse.error("Invalid cart item ID"));
            }
            UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
            CartDto cart = cartService.removeFromCart(userPrincipal.getId(), cartItemId);
            return ResponseEntity.ok(ApiResponse.success("Item removed from cart", cart));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error("Error removing from cart: " + e.getMessage()));
        }
    }

    @DeleteMapping("/clear")
    public ResponseEntity<ApiResponse<Object>> clearCart(Authentication authentication) {
        try {
            if (authentication == null || authentication.getPrincipal() == null) {
                return ResponseEntity.status(401).body(ApiResponse.error("User not authenticated"));
            }
            UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
            cartService.clearCart(userPrincipal.getId());
            return ResponseEntity.ok(ApiResponse.success("Cart cleared", null));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error("Error clearing cart: " + e.getMessage()));
        }
    }

    @GetMapping("/count")
    public ResponseEntity<ApiResponse<Integer>> getCartItemCount(Authentication authentication) {
        try {
            if (authentication == null || authentication.getPrincipal() == null) {
                return ResponseEntity.status(401).body(ApiResponse.error("User not authenticated"));
            }
            UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
            Integer count = cartService.getCartItemCount(userPrincipal.getId());
            return ResponseEntity.ok(ApiResponse.success(count != null ? count : 0));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error("Error fetching cart count: " + e.getMessage()));
        }
    }
}

