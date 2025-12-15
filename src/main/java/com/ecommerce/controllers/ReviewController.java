package com.ecommerce.controllers;

import com.ecommerce.dto.ApiResponse;
import com.ecommerce.dto.ReviewDto;
import com.ecommerce.security.UserPrincipal;
import com.ecommerce.services.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
@CrossOrigin(origins = "*")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @GetMapping("/product/{productId}")
    public ResponseEntity<ApiResponse<List<ReviewDto>>> getProductReviews(@PathVariable Long productId) {
        try {
            if (productId == null) {
                return ResponseEntity.badRequest().body(ApiResponse.error("Invalid product ID"));
            }
            List<ReviewDto> reviews = reviewService.getProductReviews(productId);
            return ResponseEntity.ok(ApiResponse.success(reviews != null ? reviews : List.of()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error("Error fetching reviews: " + e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ReviewDto>> getReviewById(@PathVariable Long id) {
        try {
            if (id == null) {
                return ResponseEntity.badRequest().body(ApiResponse.error("Invalid review ID"));
            }
            ReviewDto review = reviewService.getReviewById(id);
            if (review == null) {
                return ResponseEntity.status(404).body(ApiResponse.error("Review not found"));
            }
            return ResponseEntity.ok(ApiResponse.success(review));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error("Error fetching review: " + e.getMessage()));
        }
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ReviewDto>> createReview(
            @RequestBody ReviewDto reviewDto,
            Authentication authentication) {
        try {
            if (authentication == null || authentication.getPrincipal() == null) {
                return ResponseEntity.status(401).body(ApiResponse.error("User not authenticated"));
            }
            if (reviewDto == null) {
                return ResponseEntity.badRequest().body(ApiResponse.error("Review data is required"));
            }
            UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
            ReviewDto createdReview = reviewService.createReview(reviewDto, userPrincipal.getId());
            return ResponseEntity.ok(ApiResponse.success("Review created successfully", createdReview));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error("Error creating review: " + e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ReviewDto>> updateReview(
            @PathVariable Long id,
            @RequestBody ReviewDto reviewDto,
            Authentication authentication) {
        try {
            if (authentication == null || authentication.getPrincipal() == null) {
                return ResponseEntity.status(401).body(ApiResponse.error("User not authenticated"));
            }
            if (id == null || reviewDto == null) {
                return ResponseEntity.badRequest().body(ApiResponse.error("Invalid review ID or data"));
            }
            UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
            ReviewDto updatedReview = reviewService.updateReview(id, reviewDto, userPrincipal.getId());
            if (updatedReview == null) {
                return ResponseEntity.status(404).body(ApiResponse.error("Review not found"));
            }
            return ResponseEntity.ok(ApiResponse.success("Review updated successfully", updatedReview));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error("Error updating review: " + e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Object>> deleteReview(
            @PathVariable Long id,
            Authentication authentication) {
        try {
            if (authentication == null || authentication.getPrincipal() == null) {
                return ResponseEntity.status(401).body(ApiResponse.error("User not authenticated"));
            }
            if (id == null) {
                return ResponseEntity.badRequest().body(ApiResponse.error("Invalid review ID"));
            }
            UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
            reviewService.deleteReview(id, userPrincipal.getId());
            return ResponseEntity.ok(ApiResponse.success("Review deleted successfully", null));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error("Error deleting review: " + e.getMessage()));
        }
    }
}

