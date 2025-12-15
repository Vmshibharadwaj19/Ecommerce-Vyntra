package com.ecommerce.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDto {

    private Long id;
    private String name;
    private String description;
    private String imageUrl;
    private Boolean isActive;
    private List<SubCategoryDto> subCategories = new ArrayList<>();
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}



