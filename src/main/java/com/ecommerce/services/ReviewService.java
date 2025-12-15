package com.ecommerce.services;

import com.ecommerce.dto.ReviewDto;

import java.util.List;

public interface ReviewService {

    ReviewDto createReview(ReviewDto reviewDto, Long userId);

    ReviewDto updateReview(Long reviewId, ReviewDto reviewDto, Long userId);

    void deleteReview(Long reviewId, Long userId);

    List<ReviewDto> getProductReviews(Long productId);

    ReviewDto getReviewById(Long reviewId);
}



