package com.ecommerce.controllers;

import com.ecommerce.dto.ApiResponse;
import com.ecommerce.dto.CategoryDto;
import com.ecommerce.dto.SubCategoryDto;
import com.ecommerce.services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@CrossOrigin(origins = "*")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<CategoryDto>>> getAllCategories() {
        List<CategoryDto> categories = categoryService.getAllCategories();
        return ResponseEntity.ok(ApiResponse.success(categories));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CategoryDto>> getCategoryById(@PathVariable Long id) {
        CategoryDto category = categoryService.getCategoryById(id);
        return ResponseEntity.ok(ApiResponse.success(category));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<CategoryDto>> createCategory(@RequestBody CategoryDto categoryDto) {
        CategoryDto createdCategory = categoryService.createCategory(categoryDto);
        return ResponseEntity.ok(ApiResponse.success("Category created successfully", createdCategory));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<CategoryDto>> updateCategory(
            @PathVariable Long id,
            @RequestBody CategoryDto categoryDto) {
        CategoryDto updatedCategory = categoryService.updateCategory(id, categoryDto);
        return ResponseEntity.ok(ApiResponse.success("Category updated successfully", updatedCategory));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Object>> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.ok(ApiResponse.success("Category deleted successfully", null));
    }

    @GetMapping("/{categoryId}/subcategories")
    public ResponseEntity<ApiResponse<List<SubCategoryDto>>> getSubCategories(@PathVariable Long categoryId) {
        List<SubCategoryDto> subCategories = categoryService.getSubCategoriesByCategory(categoryId);
        return ResponseEntity.ok(ApiResponse.success(subCategories));
    }

    @PostMapping("/subcategories")
    public ResponseEntity<ApiResponse<SubCategoryDto>> createSubCategory(@RequestBody SubCategoryDto subCategoryDto) {
        SubCategoryDto createdSubCategory = categoryService.createSubCategory(subCategoryDto);
        return ResponseEntity.ok(ApiResponse.success("SubCategory created successfully", createdSubCategory));
    }

    @PutMapping("/subcategories/{id}")
    public ResponseEntity<ApiResponse<SubCategoryDto>> updateSubCategory(
            @PathVariable Long id,
            @RequestBody SubCategoryDto subCategoryDto) {
        SubCategoryDto updatedSubCategory = categoryService.updateSubCategory(id, subCategoryDto);
        return ResponseEntity.ok(ApiResponse.success("SubCategory updated successfully", updatedSubCategory));
    }

    @DeleteMapping("/subcategories/{id}")
    public ResponseEntity<ApiResponse<Object>> deleteSubCategory(@PathVariable Long id) {
        categoryService.deleteSubCategory(id);
        return ResponseEntity.ok(ApiResponse.success("SubCategory deleted successfully", null));
    }
}



