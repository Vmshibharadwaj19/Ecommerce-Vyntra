package com.ecommerce.controllers;

import com.ecommerce.dto.ApiResponse;
import com.ecommerce.dto.ProductDto;
import com.ecommerce.dto.ProductSearchRequest;
import com.ecommerce.security.UserPrincipal;
import com.ecommerce.services.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/products")
@CrossOrigin(origins = "*")
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private ObjectMapper objectMapper;

    @GetMapping("/public")
    public ResponseEntity<ApiResponse<Page<ProductDto>>> getAllProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Page<ProductDto> products = productService.getAllProducts(page, size);
        return ResponseEntity.ok(ApiResponse.success(products));
    }

    @GetMapping("/public/{id}")
    public ResponseEntity<ApiResponse<ProductDto>> getProductById(@PathVariable Long id) {
        ProductDto product = productService.getProductById(id);
        return ResponseEntity.ok(ApiResponse.success(product));
    }

    @PostMapping("/search")
    public ResponseEntity<ApiResponse<Page<ProductDto>>> searchProducts(@RequestBody ProductSearchRequest searchRequest) {
        Page<ProductDto> products = productService.searchProducts(searchRequest);
        return ResponseEntity.ok(ApiResponse.success(products));
    }

    @GetMapping("/public/category/{categoryId}")
    public ResponseEntity<ApiResponse<Page<ProductDto>>> getProductsByCategory(
            @PathVariable Long categoryId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Page<ProductDto> products = productService.getProductsByCategory(categoryId, page, size);
        return ResponseEntity.ok(ApiResponse.success(products));
    }

    @PostMapping(consumes = {"multipart/form-data"})
    public ResponseEntity<ApiResponse<ProductDto>> createProduct(
            @RequestPart("product") MultipartFile productFile,
            @RequestPart(value = "images", required = false) List<MultipartFile> images,
            Authentication authentication) {
        try {
            String productJson = new String(productFile.getBytes(), "UTF-8");
            ProductDto productDto = objectMapper.readValue(productJson, ProductDto.class);
            
            // Validate required fields
            if (productDto.getName() == null || productDto.getName().trim().isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(ApiResponse.error("Product name is required"));
            }
            if (productDto.getPrice() == null || productDto.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
                return ResponseEntity.badRequest()
                        .body(ApiResponse.error("Valid price is required"));
            }
            if (productDto.getDiscountPrice() == null || productDto.getDiscountPrice().compareTo(BigDecimal.ZERO) <= 0) {
                return ResponseEntity.badRequest()
                        .body(ApiResponse.error("Valid discount price is required"));
            }
            if (productDto.getCategoryId() == null) {
                return ResponseEntity.badRequest()
                        .body(ApiResponse.error("Category is required"));
            }
            if (productDto.getStockQuantity() == null || productDto.getStockQuantity() < 0) {
                return ResponseEntity.badRequest()
                        .body(ApiResponse.error("Valid stock quantity is required"));
            }
            
            UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
            ProductDto createdProduct = productService.createProduct(productDto, images, userPrincipal.getId());
            return ResponseEntity.ok(ApiResponse.success("Product created successfully", createdProduct));
        } catch (com.fasterxml.jackson.databind.JsonMappingException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Invalid product data format: " + e.getMessage()));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Error creating product: " + e.getMessage()));
        }
    }

    // Alternative endpoint for testing without images
    @PostMapping(value = "/simple", consumes = {"application/json"})
    public ResponseEntity<ApiResponse<ProductDto>> createProductSimple(
            @RequestBody ProductDto productDto,
            Authentication authentication) {
        try {
            UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
            ProductDto createdProduct = productService.createProduct(productDto, null, userPrincipal.getId());
            return ResponseEntity.ok(ApiResponse.success("Product created successfully", createdProduct));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Error creating product: " + e.getMessage()));
        }
    }

    @PutMapping(value = "/{id}", consumes = {"multipart/form-data"})
    public ResponseEntity<ApiResponse<ProductDto>> updateProduct(
            @PathVariable Long id,
            @RequestPart("product") MultipartFile productFile,
            @RequestPart(value = "images", required = false) List<MultipartFile> images,
            Authentication authentication) {
        try {
            String productJson = new String(productFile.getBytes());
            ProductDto productDto = objectMapper.readValue(productJson, ProductDto.class);
            UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
            ProductDto updatedProduct = productService.updateProduct(id, productDto, images, userPrincipal.getId());
            return ResponseEntity.ok(ApiResponse.success("Product updated successfully", updatedProduct));
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error parsing product data: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Object>> deleteProduct(
            @PathVariable Long id,
            Authentication authentication) {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        productService.deleteProduct(id, userPrincipal.getId());
        return ResponseEntity.ok(ApiResponse.success("Product deleted successfully", null));
    }

    @GetMapping("/seller/my-products")
    public ResponseEntity<ApiResponse<Page<ProductDto>>> getMyProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            Authentication authentication) {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        Page<ProductDto> products = productService.getProductsBySeller(userPrincipal.getId(), page, size);
        return ResponseEntity.ok(ApiResponse.success(products));
    }
}

