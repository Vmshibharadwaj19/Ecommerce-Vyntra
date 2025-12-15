# Complete Operation Testing Guide

## All Operations Test Checklist

### ✅ Authentication Operations

#### 1. User Registration (Signup)
- [x] Customer registration - Success
- [x] Seller registration - Success (requires approval)
- [x] Duplicate email - Error 400
- [x] Invalid email format - Error 400
- [x] Weak password (< 6 chars) - Error 400
- [x] Missing required fields - Error 400

#### 2. User Login
- [x] Valid credentials - Success, returns JWT
- [x] Invalid email - Error 401
- [x] Invalid password - Error 401
- [x] Blocked user - Error 403
- [x] Inactive user - Error 403

#### 3. Get Current User
- [x] Authenticated user - Returns user data
- [x] Unauthenticated - Error 401

---

### ✅ Product Operations

#### 1. Create Product
- [x] Valid product with all fields - Success
- [x] Valid product without images - Success
- [x] Valid product without subcategory - Success
- [x] Missing name - Error 400
- [x] Missing price - Error 400
- [x] Missing category - Error 400
- [x] Invalid category ID - Error 404
- [x] Invalid subcategory ID - Error 404
- [x] Subcategory not belonging to category - Error 400
- [x] Negative price - Error 400
- [x] Discount > Regular price - Error 400
- [x] Negative stock - Error 400
- [x] Customer trying to create - Error 403
- [x] Unauthenticated - Error 401

#### 2. Update Product
- [x] Valid update - Success
- [x] Update own product - Success
- [x] Update other seller's product - Error 403
- [x] Invalid product ID - Error 404
- [x] Unauthenticated - Error 401

#### 3. Delete Product
- [x] Delete own product - Success
- [x] Delete other seller's product - Error 403
- [x] Invalid product ID - Error 404

#### 4. Get Products
- [x] Get all products (public) - Success
- [x] Get product by ID - Success
- [x] Get products by category - Success
- [x] Search products - Success
- [x] Pagination works - Success

#### 5. Get My Products (Seller)
- [x] Get seller's products - Success
- [x] Unauthenticated - Error 401
- [x] Customer trying to access - Error 403

---

### ✅ Cart Operations

#### 1. Add to Cart
- [x] Add valid product - Success
- [x] Add existing product (increase quantity) - Success
- [x] Add product with insufficient stock - Error 400
- [x] Add inactive product - Error 400
- [x] Invalid product ID - Error 404
- [x] Unauthenticated - Error 401

#### 2. Update Cart Item
- [x] Update quantity - Success
- [x] Set quantity to 0 (remove) - Success
- [x] Quantity exceeds stock - Error 400
- [x] Invalid cart item ID - Error 404

#### 3. Remove from Cart
- [x] Remove item - Success
- [x] Invalid cart item ID - Error 404

#### 4. Get Cart
- [x] Get user cart - Success
- [x] Empty cart - Returns empty cart
- [x] Unauthenticated - Error 401

#### 5. Clear Cart
- [x] Clear cart - Success

---

### ✅ Order Operations

#### 1. Create Order
- [x] Create order from cart - Success
- [x] Empty cart - Error 400
- [x] Invalid address - Error 404
- [x] Address not belonging to user - Error 400
- [x] Insufficient stock - Error 400
- [x] Unauthenticated - Error 401

#### 2. Get User Orders
- [x] Get orders - Success
- [x] No orders - Returns empty list

#### 3. Get Order Details
- [x] Get order by ID - Success
- [x] Invalid order ID - Error 404
- [x] Access other user's order - Error 403

#### 4. Update Order Status (Seller)
- [x] Mark as shipped - Success
- [x] Mark as delivered - Success
- [x] Invalid status - Error 400
- [x] Unauthorized - Error 403

---

### ✅ Payment Operations

#### 1. Create Razorpay Order
- [x] Create order - Success, returns order ID
- [x] Invalid amount - Error 400
- [x] Unauthenticated - Error 401

#### 2. Verify Payment
- [x] Valid payment - Success, creates order
- [x] Invalid signature - Error 400
- [x] Payment verification failed - Error 400

---

### ✅ Category Operations

#### 1. Get Categories
- [x] Get all categories - Success
- [x] Get category by ID - Success
- [x] Get subcategories - Success

#### 2. Create Category (Admin)
- [x] Create category - Success
- [x] Duplicate name - Error 400
- [x] Unauthorized - Error 403

---

### ✅ Review Operations

#### 1. Create Review
- [x] Create review - Success
- [x] Duplicate review - Error 400
- [x] Invalid rating (not 1-5) - Error 400
- [x] Invalid product ID - Error 404

#### 2. Get Product Reviews
- [x] Get reviews - Success
- [x] No reviews - Returns empty list

---

### ✅ Wishlist Operations

#### 1. Add to Wishlist
- [x] Add product - Success
- [x] Duplicate product - Error 400
- [x] Invalid product ID - Error 404

#### 2. Remove from Wishlist
- [x] Remove product - Success
- [x] Product not in wishlist - Error 404

#### 3. Get Wishlist
- [x] Get wishlist - Success
- [x] Empty wishlist - Returns empty list

---

### ✅ Address Operations

#### 1. Create Address
- [x] Create address - Success
- [x] Set as default - Success, unsets others
- [x] Missing required fields - Error 400

#### 2. Update Address
- [x] Update address - Success
- [x] Update other user's address - Error 403

#### 3. Delete Address
- [x] Delete address - Success
- [x] Delete other user's address - Error 403

---

### ✅ Admin Operations

#### 1. Get Dashboard Stats
- [x] Get stats - Success
- [x] Unauthorized - Error 403

#### 2. Approve/Reject Seller
- [x] Approve seller - Success
- [x] Reject seller - Success
- [x] Invalid seller ID - Error 404
- [x] Not a seller - Error 400

#### 3. Approve/Reject Product
- [x] Approve product - Success
- [x] Reject product - Success
- [x] Invalid product ID - Error 404

#### 4. Block/Unblock User
- [x] Block user - Success
- [x] Unblock user - Success
- [x] Invalid user ID - Error 404

---

## Error Response Format

All errors follow this format:
```json
{
  "success": false,
  "message": "Error message here",
  "data": null
}
```

Validation errors:
```json
{
  "fieldName": "Error message for this field"
}
```

---

## Testing Commands

### Using Postman/Thunder Client:

1. **Register User:**
   ```
   POST http://localhost:8080/api/auth/signup
   Body: { "email": "...", "password": "...", "firstName": "...", "lastName": "...", "role": "ROLE_CUSTOMER" }
   ```

2. **Login:**
   ```
   POST http://localhost:8080/api/auth/signin
   Body: { "email": "...", "password": "..." }
   Response: { "token": "..." }
   ```

3. **Create Product (with token):**
   ```
   POST http://localhost:8080/api/products
   Headers: Authorization: Bearer {token}
   Body: multipart/form-data
     - product: { JSON string }
     - images: [files]
   ```

---

## Common Issues and Solutions

### Issue: TransientPropertyValueException
**Solution:** Ensure all related entities (Category, SubCategory) are loaded from database, not created new

### Issue: Null Constraint Violation
**Solution:** Set default values for all non-nullable fields (averageRating, totalReviews)

### Issue: ModelMapper Ambiguity
**Solution:** Use manual mapping for complex relationships

### Issue: 401 Unauthorized
**Solution:** Check JWT token is valid and included in Authorization header

### Issue: 403 Forbidden
**Solution:** Verify user has correct role for the operation

---

## Performance Testing

- [x] Load 1000 products - Response time < 2s
- [x] Search with filters - Response time < 1s
- [x] Pagination works correctly
- [x] Image upload handles multiple files

---

## Security Testing

- [x] SQL Injection attempts - Blocked
- [x] XSS attempts - Sanitized
- [x] CSRF protection - Enabled
- [x] JWT token expiration - Works
- [x] Role-based access - Enforced



