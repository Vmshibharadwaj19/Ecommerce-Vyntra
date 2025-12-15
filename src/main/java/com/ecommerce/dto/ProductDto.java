package com.ecommerce.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto {

    private Long id;

    @NotBlank(message = "Product name is required")
    @Size(min = 3, max = 200, message = "Product name must be between 3 and 200 characters")
    private String name;

    @Size(max = 2000, message = "Description cannot exceed 2000 characters")
    private String description;

    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.01", message = "Price must be greater than 0")
    @Digits(integer = 10, fraction = 2, message = "Price must have at most 10 digits and 2 decimal places")
    private BigDecimal price;

    @NotNull(message = "Discount price is required")
    @DecimalMin(value = "0.01", message = "Discount price must be greater than 0")
    @Digits(integer = 10, fraction = 2, message = "Discount price must have at most 10 digits and 2 decimal places")
    private BigDecimal discountPrice;

    private Long sellerId;
    private String sellerName;

    @NotNull(message = "Category is required")
    private Long categoryId;
    private String categoryName;
    private Long subCategoryId;
    private String subCategoryName;

    @NotNull(message = "Stock quantity is required")
    @Min(value = 0, message = "Stock quantity cannot be negative")
    private Integer stockQuantity;

    private Boolean isActive;
    private Boolean isApproved;
    private String brand;
    private String color;
    private String size;
    private String weight;
    private String dimensions;
    private List<String> images = new ArrayList<>();
    private Double averageRating;
    private Integer totalReviews;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

