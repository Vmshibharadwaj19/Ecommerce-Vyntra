package com.ecommerce.controllers;

import com.ecommerce.dto.ApiResponse;
import com.ecommerce.dto.ProductDto;
import com.ecommerce.security.UserPrincipal;
import com.ecommerce.services.WishlistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/wishlist")
@CrossOrigin(origins = "*")
public class WishlistController {

    @Autowired
    private WishlistService wishlistService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<ProductDto>>> getUserWishlist(Authentication authentication) {
        try {
            if (authentication == null || authentication.getPrincipal() == null) {
                return ResponseEntity.status(401).body(ApiResponse.error("User not authenticated"));
            }
            UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
            List<ProductDto> wishlist = wishlistService.getUserWishlist(userPrincipal.getId());
            return ResponseEntity.ok(ApiResponse.success(wishlist != null ? wishlist : List.of()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error("Error fetching wishlist: " + e.getMessage()));
        }
    }

    @PostMapping("/add")
    public ResponseEntity<ApiResponse<Object>> addToWishlist(
            @RequestParam Long productId,
            Authentication authentication) {
        try {
            if (authentication == null || authentication.getPrincipal() == null) {
                return ResponseEntity.status(401).body(ApiResponse.error("User not authenticated"));
            }
            if (productId == null) {
                return ResponseEntity.badRequest().body(ApiResponse.error("Invalid product ID"));
            }
            UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
            wishlistService.addToWishlist(userPrincipal.getId(), productId);
            return ResponseEntity.ok(ApiResponse.success("Product added to wishlist", null));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error("Error adding to wishlist: " + e.getMessage()));
        }
    }

    @DeleteMapping("/remove")
    public ResponseEntity<ApiResponse<Object>> removeFromWishlist(
            @RequestParam Long productId,
            Authentication authentication) {
        try {
            if (authentication == null || authentication.getPrincipal() == null) {
                return ResponseEntity.status(401).body(ApiResponse.error("User not authenticated"));
            }
            if (productId == null) {
                return ResponseEntity.badRequest().body(ApiResponse.error("Invalid product ID"));
            }
            UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
            wishlistService.removeFromWishlist(userPrincipal.getId(), productId);
            return ResponseEntity.ok(ApiResponse.success("Product removed from wishlist", null));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error("Error removing from wishlist: " + e.getMessage()));
        }
    }

    @GetMapping("/check")
    public ResponseEntity<ApiResponse<Boolean>> isInWishlist(
            @RequestParam Long productId,
            Authentication authentication) {
        try {
            if (authentication == null || authentication.getPrincipal() == null) {
                return ResponseEntity.status(401).body(ApiResponse.error("User not authenticated"));
            }
            if (productId == null) {
                return ResponseEntity.badRequest().body(ApiResponse.error("Invalid product ID"));
            }
            UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
            Boolean isInWishlist = wishlistService.isInWishlist(userPrincipal.getId(), productId);
            return ResponseEntity.ok(ApiResponse.success(isInWishlist != null ? isInWishlist : false));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error("Error checking wishlist: " + e.getMessage()));
        }
    }
}

