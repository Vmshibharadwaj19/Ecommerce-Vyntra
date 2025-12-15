package com.ecommerce.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductSearchRequest {

    private String keyword;
    private Long categoryId;
    private Long subCategoryId;
    private BigDecimal minPrice;
    private BigDecimal maxPrice;
    private String brand;
    private String sortBy; // price_asc, price_desc, rating_desc, newest
    private Integer page = 0;
    private Integer size = 20;
}

