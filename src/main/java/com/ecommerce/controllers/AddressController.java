package com.ecommerce.controllers;

import com.ecommerce.dto.AddressDto;
import com.ecommerce.dto.ApiResponse;
import com.ecommerce.security.UserPrincipal;
import com.ecommerce.services.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/addresses")
@CrossOrigin(origins = "*")
public class AddressController {

    @Autowired
    private AddressService addressService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<AddressDto>>> getUserAddresses(Authentication authentication) {
        try {
            if (authentication == null || authentication.getPrincipal() == null) {
                return ResponseEntity.status(401).body(ApiResponse.error("User not authenticated"));
            }
            UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
            List<AddressDto> addresses = addressService.getUserAddresses(userPrincipal.getId());
            return ResponseEntity.ok(ApiResponse.success(addresses != null ? addresses : List.of()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error("Error fetching addresses: " + e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<AddressDto>> getAddressById(@PathVariable Long id) {
        try {
            if (id == null) {
                return ResponseEntity.badRequest().body(ApiResponse.error("Invalid address ID"));
            }
            AddressDto address = addressService.getAddressById(id);
            if (address == null) {
                return ResponseEntity.status(404).body(ApiResponse.error("Address not found"));
            }
            return ResponseEntity.ok(ApiResponse.success(address));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error("Error fetching address: " + e.getMessage()));
        }
    }

    @PostMapping
    public ResponseEntity<ApiResponse<AddressDto>> createAddress(
            @RequestBody AddressDto addressDto,
            Authentication authentication) {
        try {
            if (authentication == null || authentication.getPrincipal() == null) {
                return ResponseEntity.status(401).body(ApiResponse.error("User not authenticated"));
            }
            if (addressDto == null) {
                return ResponseEntity.badRequest().body(ApiResponse.error("Address data is required"));
            }
            UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
            AddressDto createdAddress = addressService.createAddress(addressDto, userPrincipal.getId());
            return ResponseEntity.ok(ApiResponse.success("Address created successfully", createdAddress));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error("Error creating address: " + e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<AddressDto>> updateAddress(
            @PathVariable Long id,
            @RequestBody AddressDto addressDto,
            Authentication authentication) {
        try {
            if (authentication == null || authentication.getPrincipal() == null) {
                return ResponseEntity.status(401).body(ApiResponse.error("User not authenticated"));
            }
            if (id == null || addressDto == null) {
                return ResponseEntity.badRequest().body(ApiResponse.error("Invalid address ID or data"));
            }
            UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
            AddressDto updatedAddress = addressService.updateAddress(id, addressDto, userPrincipal.getId());
            if (updatedAddress == null) {
                return ResponseEntity.status(404).body(ApiResponse.error("Address not found"));
            }
            return ResponseEntity.ok(ApiResponse.success("Address updated successfully", updatedAddress));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error("Error updating address: " + e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Object>> deleteAddress(
            @PathVariable Long id,
            Authentication authentication) {
        try {
            if (authentication == null || authentication.getPrincipal() == null) {
                return ResponseEntity.status(401).body(ApiResponse.error("User not authenticated"));
            }
            if (id == null) {
                return ResponseEntity.badRequest().body(ApiResponse.error("Invalid address ID"));
            }
            UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
            addressService.deleteAddress(id, userPrincipal.getId());
            return ResponseEntity.ok(ApiResponse.success("Address deleted successfully", null));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error("Error deleting address: " + e.getMessage()));
        }
    }
}

