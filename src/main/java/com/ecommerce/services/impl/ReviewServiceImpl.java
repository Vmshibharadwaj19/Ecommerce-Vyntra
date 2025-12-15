package com.ecommerce.services.impl;

import com.ecommerce.dto.ReviewDto;
import com.ecommerce.entities.Order;
import com.ecommerce.entities.Product;
import com.ecommerce.entities.Review;
import com.ecommerce.entities.User;
import com.ecommerce.exceptions.ResourceNotFoundException;
import com.ecommerce.repositories.OrderRepository;
import com.ecommerce.repositories.ProductRepository;
import com.ecommerce.repositories.ReviewRepository;
import com.ecommerce.repositories.UserRepository;
import com.ecommerce.services.ReviewService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ReviewServiceImpl implements ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public ReviewDto createReview(ReviewDto reviewDto, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        Product product = productRepository.findById(reviewDto.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", reviewDto.getProductId()));

        // Check if user already reviewed this product
        if (reviewRepository.findByUserIdAndProductId(userId, reviewDto.getProductId()).isPresent()) {
            throw new RuntimeException("You have already reviewed this product");
        }

        Review review = modelMapper.map(reviewDto, Review.class);
        review.setUser(user);
        review.setProduct(product);

        // Check if it's a verified purchase
        if (reviewDto.getOrderId() != null) {
            Order order = orderRepository.findById(reviewDto.getOrderId())
                    .orElseThrow(() -> new ResourceNotFoundException("Order", "id", reviewDto.getOrderId()));
            review.setOrder(order);
            review.setIsVerifiedPurchase(true);
        }

        Review savedReview = reviewRepository.save(review);

        // Update product rating
        updateProductRating(product);

        return mapToDto(savedReview);
    }

    @Override
    public ReviewDto updateReview(Long reviewId, ReviewDto reviewDto, Long userId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ResourceNotFoundException("Review", "id", reviewId));

        if (!review.getUser().getId().equals(userId)) {
            throw new RuntimeException("You can only update your own reviews");
        }

        if (reviewDto.getRating() != null) review.setRating(reviewDto.getRating());
        if (reviewDto.getComment() != null) review.setComment(reviewDto.getComment());

        Review savedReview = reviewRepository.save(review);

        // Update product rating
        updateProductRating(review.getProduct());

        return mapToDto(savedReview);
    }

    @Override
    public void deleteReview(Long reviewId, Long userId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ResourceNotFoundException("Review", "id", reviewId));

        if (!review.getUser().getId().equals(userId)) {
            throw new RuntimeException("You can only delete your own reviews");
        }

        Product product = review.getProduct();
        reviewRepository.delete(review);

        // Update product rating
        updateProductRating(product);
    }

    @Override
    public List<ReviewDto> getProductReviews(Long productId) {
        List<Review> reviews = reviewRepository.findByProductIdOrderByCreatedAtDesc(productId);
        return reviews.stream().map(this::mapToDto).collect(Collectors.toList());
    }

    @Override
    public ReviewDto getReviewById(Long reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ResourceNotFoundException("Review", "id", reviewId));
        return mapToDto(review);
    }

    private void updateProductRating(Product product) {
        List<Review> reviews = reviewRepository.findByProductId(product.getId());
        if (reviews.isEmpty()) {
            product.setAverageRating(0.0);
            product.setTotalReviews(0);
        } else {
            double averageRating = reviews.stream()
                    .mapToInt(Review::getRating)
                    .average()
                    .orElse(0.0);
            product.setAverageRating(averageRating);
            product.setTotalReviews(reviews.size());
        }
        productRepository.save(product);
    }

    private ReviewDto mapToDto(Review review) {
        ReviewDto dto = modelMapper.map(review, ReviewDto.class);
        dto.setUserId(review.getUser().getId());
        dto.setUserName(review.getUser().getFirstName() + " " + review.getUser().getLastName());
        dto.setProductId(review.getProduct().getId());
        dto.setProductName(review.getProduct().getName());
        if (review.getOrder() != null) {
            dto.setOrderId(review.getOrder().getId());
        }
        return dto;
    }
}



