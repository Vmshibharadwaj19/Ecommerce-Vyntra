package com.ecommerce.services.impl;

import com.ecommerce.dto.ProductDto;
import com.ecommerce.dto.ProductSearchRequest;
import com.ecommerce.entities.Category;
import com.ecommerce.entities.Product;
import com.ecommerce.entities.SubCategory;
import com.ecommerce.entities.User;
import com.ecommerce.exceptions.ResourceNotFoundException;
import com.ecommerce.repositories.CategoryRepository;
import com.ecommerce.repositories.ProductRepository;
import com.ecommerce.repositories.SubCategoryRepository;
import com.ecommerce.repositories.UserRepository;
import com.ecommerce.services.ProductService;
import com.ecommerce.utils.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@Transactional
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private SubCategoryRepository subCategoryRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FileStorageService fileStorageService;

    @Override
    public ProductDto createProduct(ProductDto productDto, List<MultipartFile> images, Long sellerId) {
        User seller = userRepository.findById(sellerId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", sellerId));

        if (seller.getRole() != User.Role.ROLE_SELLER && seller.getRole() != User.Role.ROLE_ADMIN) {
            throw new RuntimeException("Only sellers can create products");
        }

        Category category = categoryRepository.findById(productDto.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", productDto.getCategoryId()));

        // Create Product manually to avoid ModelMapper issues with relationships
        Product product = new Product();
        product.setName(productDto.getName());
        product.setDescription(productDto.getDescription());
        product.setPrice(productDto.getPrice());
        product.setDiscountPrice(productDto.getDiscountPrice());
        product.setStockQuantity(productDto.getStockQuantity());
        product.setBrand(productDto.getBrand());
        product.setColor(productDto.getColor());
        product.setSize(productDto.getSize());
        product.setWeight(productDto.getWeight());
        product.setDimensions(productDto.getDimensions());
        
        // Set relationships - use managed entities from database
        product.setSeller(seller);
        product.setCategory(category);
        
        // Set SubCategory if provided - must be a managed entity
        if (productDto.getSubCategoryId() != null && productDto.getSubCategoryId() > 0) {
            SubCategory subCategory = subCategoryRepository.findById(productDto.getSubCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("SubCategory", "id", productDto.getSubCategoryId()));
            // Verify subCategory belongs to the selected category
            if (!subCategory.getCategory().getId().equals(category.getId())) {
                throw new RuntimeException("SubCategory does not belong to the selected Category");
            }
            product.setSubCategory(subCategory);
        } else {
            product.setSubCategory(null);
        }
        
        // Set default values
        product.setIsActive(true);
        // Products require admin approval - set to false by default
        product.setIsApproved(false);
        product.setAverageRating(0.0);
        product.setTotalReviews(0);
        product.setImages(new java.util.ArrayList<>());

        if (images != null && !images.isEmpty()) {
            try {
                List<String> imageUrls = fileStorageService.storeFiles(images);
                product.setImages(imageUrls);
            } catch (Exception e) {
                throw new RuntimeException("Error storing product images: " + e.getMessage());
            }
        }

        Product savedProduct = productRepository.save(product);
        return mapToDto(savedProduct);
    }

    @Override
    public ProductDto updateProduct(Long productId, ProductDto productDto, List<MultipartFile> images, Long sellerId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", productId));

        if (!product.getSeller().getId().equals(sellerId)) {
            throw new RuntimeException("You can only update your own products");
        }

        if (productDto.getName() != null) product.setName(productDto.getName());
        if (productDto.getDescription() != null) product.setDescription(productDto.getDescription());
        if (productDto.getPrice() != null) product.setPrice(productDto.getPrice());
        if (productDto.getDiscountPrice() != null) product.setDiscountPrice(productDto.getDiscountPrice());
        if (productDto.getStockQuantity() != null) product.setStockQuantity(productDto.getStockQuantity());
        if (productDto.getBrand() != null) product.setBrand(productDto.getBrand());
        if (productDto.getColor() != null) product.setColor(productDto.getColor());
        if (productDto.getSize() != null) product.setSize(productDto.getSize());

        if (productDto.getCategoryId() != null) {
            Category category = categoryRepository.findById(productDto.getCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("Category", "id", productDto.getCategoryId()));
            product.setCategory(category);
            
            // If category changed, validate subcategory belongs to new category
            if (productDto.getSubCategoryId() != null && productDto.getSubCategoryId() > 0) {
                SubCategory subCategory = subCategoryRepository.findById(productDto.getSubCategoryId())
                        .orElseThrow(() -> new ResourceNotFoundException("SubCategory", "id", productDto.getSubCategoryId()));
                if (!subCategory.getCategory().getId().equals(category.getId())) {
                    throw new RuntimeException("SubCategory does not belong to the selected Category");
                }
                product.setSubCategory(subCategory);
            } else {
                product.setSubCategory(null);
            }
        } else if (productDto.getSubCategoryId() != null && productDto.getSubCategoryId() > 0) {
            // Only subcategory changed, verify it belongs to current category
            SubCategory subCategory = subCategoryRepository.findById(productDto.getSubCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("SubCategory", "id", productDto.getSubCategoryId()));
            if (!subCategory.getCategory().getId().equals(product.getCategory().getId())) {
                throw new RuntimeException("SubCategory does not belong to the product's Category");
            }
            product.setSubCategory(subCategory);
        } else if (productDto.getSubCategoryId() == null || productDto.getSubCategoryId() == 0) {
            // Explicitly set to null if subcategory is removed
            product.setSubCategory(null);
        }

        if (images != null && !images.isEmpty()) {
            try {
                List<String> imageUrls = fileStorageService.storeFiles(images);
                product.getImages().addAll(imageUrls);
            } catch (Exception e) {
                throw new RuntimeException("Error storing product images: " + e.getMessage());
            }
        }

        Product updatedProduct = productRepository.save(product);
        return mapToDto(updatedProduct);
    }

    @Override
    public void deleteProduct(Long productId, Long sellerId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", productId));

        if (!product.getSeller().getId().equals(sellerId)) {
            throw new RuntimeException("You can only delete your own products");
        }

        productRepository.delete(product);
    }

    @Override
    public ProductDto getProductById(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", productId));
        return mapToDto(product);
    }

    @Override
    public Page<ProductDto> getAllProducts(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Product> products = productRepository.findByIsActiveTrueAndIsApprovedTrue(pageable);
        return products.map(this::mapToDto);
    }

    @Override
    public Page<ProductDto> searchProducts(ProductSearchRequest searchRequest) {
        Sort sort = getSort(searchRequest.getSortBy());
        Pageable pageable = PageRequest.of(searchRequest.getPage(), searchRequest.getSize(), sort);
        
        Page<Product> products = productRepository.searchProducts(
                searchRequest.getKeyword(),
                searchRequest.getCategoryId(),
                searchRequest.getSubCategoryId(),
                searchRequest.getMinPrice(),
                searchRequest.getMaxPrice(),
                searchRequest.getBrand(),
                pageable
        );

        return products.map(this::mapToDto);
    }

    @Override
    public Page<ProductDto> getProductsByCategory(Long categoryId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Product> products = productRepository.findByCategoryId(categoryId, pageable);
        return products.map(this::mapToDto);
    }

    @Override
    public Page<ProductDto> getProductsBySeller(Long sellerId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Product> products = productRepository.findBySellerId(sellerId, pageable);
        return products.map(this::mapToDto);
    }

    @Override
    public ProductDto approveProduct(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", productId));
        product.setIsApproved(true);
        Product savedProduct = productRepository.save(product);
        return mapToDto(savedProduct);
    }

    @Override
    public ProductDto rejectProduct(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", productId));
        product.setIsApproved(false);
        product.setIsActive(false);
        Product savedProduct = productRepository.save(product);
        return mapToDto(savedProduct);
    }

    private ProductDto mapToDto(Product product) {
        ProductDto dto = new ProductDto();
        
        // Map basic fields
        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setDescription(product.getDescription());
        dto.setPrice(product.getPrice());
        dto.setDiscountPrice(product.getDiscountPrice());
        dto.setStockQuantity(product.getStockQuantity());
        dto.setIsActive(product.getIsActive());
        dto.setIsApproved(product.getIsApproved());
        dto.setBrand(product.getBrand());
        dto.setColor(product.getColor());
        dto.setSize(product.getSize());
        dto.setWeight(product.getWeight());
        dto.setDimensions(product.getDimensions());
        dto.setImages(product.getImages() != null ? product.getImages() : new java.util.ArrayList<>());
        dto.setAverageRating(product.getAverageRating());
        dto.setTotalReviews(product.getTotalReviews());
        dto.setCreatedAt(product.getCreatedAt());
        dto.setUpdatedAt(product.getUpdatedAt());
        
        // Map relationship fields manually
        dto.setSellerId(product.getSeller().getId());
        dto.setSellerName(product.getSeller().getFirstName() + " " + product.getSeller().getLastName());
        dto.setCategoryId(product.getCategory().getId());
        dto.setCategoryName(product.getCategory().getName());
        if (product.getSubCategory() != null) {
            dto.setSubCategoryId(product.getSubCategory().getId());
            dto.setSubCategoryName(product.getSubCategory().getName());
        }
        
        return dto;
    }

    private Sort getSort(String sortBy) {
        if (sortBy == null) {
            return Sort.by(Sort.Direction.DESC, "createdAt");
        }
        return switch (sortBy) {
            case "price_asc" -> Sort.by(Sort.Direction.ASC, "discountPrice");
            case "price_desc" -> Sort.by(Sort.Direction.DESC, "discountPrice");
            case "rating_desc" -> Sort.by(Sort.Direction.DESC, "averageRating");
            case "newest" -> Sort.by(Sort.Direction.DESC, "createdAt");
            default -> Sort.by(Sort.Direction.DESC, "createdAt");
        };
    }
}

