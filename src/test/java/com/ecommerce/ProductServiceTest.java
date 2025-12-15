package com.ecommerce;

import com.ecommerce.dto.ProductDto;
import com.ecommerce.entities.Category;
import com.ecommerce.entities.User;
import com.ecommerce.repositories.CategoryRepository;
import com.ecommerce.repositories.ProductRepository;
import com.ecommerce.repositories.SubCategoryRepository;
import com.ecommerce.repositories.UserRepository;
import com.ecommerce.services.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class ProductServiceTest {

    @Autowired
    private ProductService productService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private SubCategoryRepository subCategoryRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private User testSeller;
    private Category testCategory;

    @BeforeEach
    void setUp() {
        // Create test seller
        testSeller = new User();
        testSeller.setEmail("testseller@test.com");
        testSeller.setPassword(passwordEncoder.encode("password"));
        testSeller.setFirstName("Test");
        testSeller.setLastName("Seller");
        testSeller.setRole(User.Role.ROLE_SELLER);
        testSeller.setIsActive(true);
        testSeller.setIsApproved(true);
        testSeller.setIsBlocked(false);
        testSeller.setBusinessName("Test Business");
        testSeller = userRepository.save(testSeller);

        // Create test category
        testCategory = new Category();
        testCategory.setName("Test Category");
        testCategory.setDescription("Test Description");
        testCategory.setIsActive(true);
        testCategory = categoryRepository.save(testCategory);
    }

    @Test
    void testCreateProduct_ValidData_Success() {
        ProductDto productDto = new ProductDto();
        productDto.setName("Test Product");
        productDto.setDescription("Test Description");
        productDto.setPrice(new BigDecimal("100.00"));
        productDto.setDiscountPrice(new BigDecimal("90.00"));
        productDto.setCategoryId(testCategory.getId());
        productDto.setStockQuantity(10);

        ProductDto result = productService.createProduct(productDto, null, testSeller.getId());

        assertNotNull(result);
        assertNotNull(result.getId());
        assertEquals("Test Product", result.getName());
        assertEquals(0.0, result.getAverageRating());
        assertEquals(0, result.getTotalReviews());
        assertFalse(result.getIsApproved());
        assertTrue(result.getIsActive());
    }

    @Test
    void testCreateProduct_InvalidCategory_ThrowsException() {
        ProductDto productDto = new ProductDto();
        productDto.setName("Test Product");
        productDto.setDescription("Test Description");
        productDto.setPrice(new BigDecimal("100.00"));
        productDto.setDiscountPrice(new BigDecimal("90.00"));
        productDto.setCategoryId(99999L);
        productDto.setStockQuantity(10);

        assertThrows(Exception.class, () -> {
            productService.createProduct(productDto, null, testSeller.getId());
        });
    }

    @Test
    void testCreateProduct_NullName_ThrowsException() {
        ProductDto productDto = new ProductDto();
        productDto.setName(null);
        productDto.setDescription("Test Description");
        productDto.setPrice(new BigDecimal("100.00"));
        productDto.setDiscountPrice(new BigDecimal("90.00"));
        productDto.setCategoryId(testCategory.getId());
        productDto.setStockQuantity(10);

        assertThrows(Exception.class, () -> {
            productService.createProduct(productDto, null, testSeller.getId());
        });
    }
}



