package com.ecommerce.services.impl;

import com.ecommerce.dto.CategoryDto;
import com.ecommerce.dto.SubCategoryDto;
import com.ecommerce.entities.Category;
import com.ecommerce.entities.SubCategory;
import com.ecommerce.exceptions.ResourceNotFoundException;
import com.ecommerce.repositories.CategoryRepository;
import com.ecommerce.repositories.SubCategoryRepository;
import com.ecommerce.services.CategoryService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private SubCategoryRepository subCategoryRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public CategoryDto createCategory(CategoryDto categoryDto) {
        if (categoryRepository.existsByName(categoryDto.getName())) {
            throw new RuntimeException("Category with this name already exists");
        }

        Category category = modelMapper.map(categoryDto, Category.class);
        Category savedCategory = categoryRepository.save(category);
        return modelMapper.map(savedCategory, CategoryDto.class);
    }

    @Override
    public CategoryDto updateCategory(Long categoryId, CategoryDto categoryDto) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", categoryId));

        if (categoryDto.getName() != null) category.setName(categoryDto.getName());
        if (categoryDto.getDescription() != null) category.setDescription(categoryDto.getDescription());
        if (categoryDto.getImageUrl() != null) category.setImageUrl(categoryDto.getImageUrl());
        if (categoryDto.getIsActive() != null) category.setIsActive(categoryDto.getIsActive());

        Category savedCategory = categoryRepository.save(category);
        return modelMapper.map(savedCategory, CategoryDto.class);
    }

    @Override
    public void deleteCategory(Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", categoryId));
        categoryRepository.delete(category);
    }

    @Override
    public CategoryDto getCategoryById(Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", categoryId));
        return modelMapper.map(category, CategoryDto.class);
    }

    @Override
    public List<CategoryDto> getAllCategories() {
        List<Category> categories = categoryRepository.findByIsActiveTrue();
        return categories.stream()
                .map(category -> modelMapper.map(category, CategoryDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public SubCategoryDto createSubCategory(SubCategoryDto subCategoryDto) {
        Category category = categoryRepository.findById(subCategoryDto.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", subCategoryDto.getCategoryId()));

        SubCategory subCategory = modelMapper.map(subCategoryDto, SubCategory.class);
        subCategory.setCategory(category);
        SubCategory savedSubCategory = subCategoryRepository.save(subCategory);
        
        SubCategoryDto dto = modelMapper.map(savedSubCategory, SubCategoryDto.class);
        dto.setCategoryName(category.getName());
        return dto;
    }

    @Override
    public SubCategoryDto updateSubCategory(Long subCategoryId, SubCategoryDto subCategoryDto) {
        SubCategory subCategory = subCategoryRepository.findById(subCategoryId)
                .orElseThrow(() -> new ResourceNotFoundException("SubCategory", "id", subCategoryId));

        if (subCategoryDto.getName() != null) subCategory.setName(subCategoryDto.getName());
        if (subCategoryDto.getDescription() != null) subCategory.setDescription(subCategoryDto.getDescription());
        if (subCategoryDto.getIsActive() != null) subCategory.setIsActive(subCategoryDto.getIsActive());

        if (subCategoryDto.getCategoryId() != null) {
            Category category = categoryRepository.findById(subCategoryDto.getCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("Category", "id", subCategoryDto.getCategoryId()));
            subCategory.setCategory(category);
        }

        SubCategory savedSubCategory = subCategoryRepository.save(subCategory);
        SubCategoryDto dto = modelMapper.map(savedSubCategory, SubCategoryDto.class);
        dto.setCategoryName(savedSubCategory.getCategory().getName());
        return dto;
    }

    @Override
    public void deleteSubCategory(Long subCategoryId) {
        SubCategory subCategory = subCategoryRepository.findById(subCategoryId)
                .orElseThrow(() -> new ResourceNotFoundException("SubCategory", "id", subCategoryId));
        subCategoryRepository.delete(subCategory);
    }

    @Override
    public List<SubCategoryDto> getSubCategoriesByCategory(Long categoryId) {
        List<SubCategory> subCategories = subCategoryRepository.findByCategoryIdAndIsActiveTrue(categoryId);
        return subCategories.stream()
                .map(subCategory -> {
                    SubCategoryDto dto = modelMapper.map(subCategory, SubCategoryDto.class);
                    dto.setCategoryName(subCategory.getCategory().getName());
                    return dto;
                })
                .collect(Collectors.toList());
    }
}



