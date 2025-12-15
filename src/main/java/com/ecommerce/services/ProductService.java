package com.ecommerce.services;

import com.ecommerce.dto.ProductDto;
import com.ecommerce.dto.ProductSearchRequest;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProductService {

    ProductDto createProduct(ProductDto productDto, List<MultipartFile> images, Long sellerId);

    ProductDto updateProduct(Long productId, ProductDto productDto, List<MultipartFile> images, Long sellerId);

    void deleteProduct(Long productId, Long sellerId);

    ProductDto getProductById(Long productId);

    Page<ProductDto> getAllProducts(int page, int size);

    Page<ProductDto> searchProducts(ProductSearchRequest searchRequest);

    Page<ProductDto> getProductsByCategory(Long categoryId, int page, int size);

    Page<ProductDto> getProductsBySeller(Long sellerId, int page, int size);

    ProductDto approveProduct(Long productId);

    ProductDto rejectProduct(Long productId);
}



