package com.ecommerce.controllers;

import com.ecommerce.dto.*;
import com.ecommerce.services.AdminService;
import com.ecommerce.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "*")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @Autowired
    private ProductService productService;

    @GetMapping("/dashboard")
    public ResponseEntity<ApiResponse<Object>> getDashboardStats(Authentication authentication) {
        try {
            if (authentication == null || authentication.getPrincipal() == null) {
                return ResponseEntity.status(401).body(ApiResponse.error("User not authenticated"));
            }
            Map<String, Object> stats = adminService.getDashboardStats();
            return ResponseEntity.ok(ApiResponse.success(stats));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error("Error fetching dashboard: " + e.getMessage()));
        }
    }

    @GetMapping("/users")
    public ResponseEntity<ApiResponse<List<UserDto>>> getAllUsers() {
        try {
            List<UserDto> users = adminService.getAllUsers();
            return ResponseEntity.ok(ApiResponse.success(users != null ? users : List.of()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error("Error fetching users: " + e.getMessage()));
        }
    }

    @PutMapping("/users/{id}/block")
    public ResponseEntity<ApiResponse<UserDto>> blockUser(@PathVariable Long id) {
        try {
            if (id == null) {
                return ResponseEntity.badRequest().body(ApiResponse.error("Invalid user ID"));
            }
            UserDto user = adminService.blockUser(id);
            if (user == null) {
                return ResponseEntity.status(404).body(ApiResponse.error("User not found"));
            }
            return ResponseEntity.ok(ApiResponse.success("User blocked successfully", user));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error("Error blocking user: " + e.getMessage()));
        }
    }

    @PutMapping("/users/{id}/unblock")
    public ResponseEntity<ApiResponse<UserDto>> unblockUser(@PathVariable Long id) {
        try {
            if (id == null) {
                return ResponseEntity.badRequest().body(ApiResponse.error("Invalid user ID"));
            }
            UserDto user = adminService.unblockUser(id);
            if (user == null) {
                return ResponseEntity.status(404).body(ApiResponse.error("User not found"));
            }
            return ResponseEntity.ok(ApiResponse.success("User unblocked successfully", user));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error("Error unblocking user: " + e.getMessage()));
        }
    }

    @GetMapping("/sellers/pending")
    public ResponseEntity<ApiResponse<List<UserDto>>> getPendingSellers() {
        try {
            List<UserDto> sellers = adminService.getPendingSellers();
            return ResponseEntity.ok(ApiResponse.success(sellers != null ? sellers : List.of()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error("Error fetching pending sellers: " + e.getMessage()));
        }
    }

    @PutMapping("/sellers/{id}/approve")
    public ResponseEntity<ApiResponse<UserDto>> approveSeller(@PathVariable Long id) {
        try {
            if (id == null) {
                return ResponseEntity.badRequest().body(ApiResponse.error("Invalid seller ID"));
            }
            UserDto seller = adminService.approveSeller(id);
            if (seller == null) {
                return ResponseEntity.status(404).body(ApiResponse.error("Seller not found"));
            }
            return ResponseEntity.ok(ApiResponse.success("Seller approved successfully", seller));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error("Error approving seller: " + e.getMessage()));
        }
    }

    @PutMapping("/sellers/{id}/reject")
    public ResponseEntity<ApiResponse<UserDto>> rejectSeller(@PathVariable Long id) {
        try {
            if (id == null) {
                return ResponseEntity.badRequest().body(ApiResponse.error("Invalid seller ID"));
            }
            UserDto seller = adminService.rejectSeller(id);
            if (seller == null) {
                return ResponseEntity.status(404).body(ApiResponse.error("Seller not found"));
            }
            return ResponseEntity.ok(ApiResponse.success("Seller rejected successfully", seller));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error("Error rejecting seller: " + e.getMessage()));
        }
    }

    @GetMapping("/products/pending")
    public ResponseEntity<ApiResponse<List<ProductDto>>> getPendingProducts() {
        try {
            List<ProductDto> products = adminService.getPendingProducts();
            return ResponseEntity.ok(ApiResponse.success(products != null ? products : List.of()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error("Error fetching pending products: " + e.getMessage()));
        }
    }

    @PutMapping("/products/{id}/approve")
    public ResponseEntity<ApiResponse<ProductDto>> approveProduct(@PathVariable Long id) {
        try {
            if (id == null) {
                return ResponseEntity.badRequest().body(ApiResponse.error("Invalid product ID"));
            }
            ProductDto product = productService.approveProduct(id);
            if (product == null) {
                return ResponseEntity.status(404).body(ApiResponse.error("Product not found"));
            }
            return ResponseEntity.ok(ApiResponse.success("Product approved successfully", product));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error("Error approving product: " + e.getMessage()));
        }
    }

    @PutMapping("/products/{id}/reject")
    public ResponseEntity<ApiResponse<ProductDto>> rejectProduct(@PathVariable Long id) {
        try {
            if (id == null) {
                return ResponseEntity.badRequest().body(ApiResponse.error("Invalid product ID"));
            }
            ProductDto product = productService.rejectProduct(id);
            if (product == null) {
                return ResponseEntity.status(404).body(ApiResponse.error("Product not found"));
            }
            return ResponseEntity.ok(ApiResponse.success("Product rejected successfully", product));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error("Error rejecting product: " + e.getMessage()));
        }
    }

    // New endpoint to approve all pending products (for testing)
    @PutMapping("/products/approve-all")
    public ResponseEntity<ApiResponse<Object>> approveAllPendingProducts() {
        List<ProductDto> pendingProducts = adminService.getPendingProducts();
        int approvedCount = 0;
        for (ProductDto product : pendingProducts) {
            try {
                productService.approveProduct(product.getId());
                approvedCount++;
            } catch (Exception e) {
                // Continue with next product
            }
        }
        return ResponseEntity.ok(ApiResponse.success(
            String.format("Approved %d out of %d pending products", approvedCount, pendingProducts.size()),
            null
        ));
    }

    @GetMapping("/orders")
    public ResponseEntity<ApiResponse<List<OrderDto>>> getAllOrders() {
        try {
            List<OrderDto> orders = adminService.getAllOrders();
            return ResponseEntity.ok(ApiResponse.success(orders != null ? orders : List.of()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error("Error fetching orders: " + e.getMessage()));
        }
    }
}
