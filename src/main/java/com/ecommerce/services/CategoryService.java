package com.ecommerce.services;

import com.ecommerce.dto.CategoryDto;
import com.ecommerce.dto.SubCategoryDto;

import java.util.List;

public interface CategoryService {

    CategoryDto createCategory(CategoryDto categoryDto);

    CategoryDto updateCategory(Long categoryId, CategoryDto categoryDto);

    void deleteCategory(Long categoryId);

    CategoryDto getCategoryById(Long categoryId);

    List<CategoryDto> getAllCategories();

    SubCategoryDto createSubCategory(SubCategoryDto subCategoryDto);

    SubCategoryDto updateSubCategory(Long subCategoryId, SubCategoryDto subCategoryDto);

    void deleteSubCategory(Long subCategoryId);

    List<SubCategoryDto> getSubCategoriesByCategory(Long categoryId);
}



