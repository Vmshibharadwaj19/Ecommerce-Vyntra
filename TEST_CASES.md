# Test Cases for Product Creation

## Test Scenarios

### ✅ Test Case 1: Valid Product Creation
**Input:**
- Name: "Test Product"
- Description: "This is a test product description"
- Price: 1000.00
- Discount Price: 899.00
- Stock Quantity: 50
- Category: Electronics (ID: 1)
- SubCategory: Smartphones (optional)
- Brand: "TestBrand"
- Color: "Black"
- Size: "Large"
- Images: 1-3 image files

**Expected Result:** Product created successfully, returns 200 OK

---

### ✅ Test Case 2: Product Without Images
**Input:**
- All required fields filled
- No images uploaded

**Expected Result:** Product created successfully (images are optional)

---

### ✅ Test Case 3: Product Without SubCategory
**Input:**
- All required fields filled
- Category selected but no SubCategory

**Expected Result:** Product created successfully (SubCategory is optional)

---

### ❌ Test Case 4: Missing Product Name
**Input:**
- Name: "" (empty)
- All other required fields filled

**Expected Result:** 
- Frontend: Shows error "Product name must be at least 3 characters"
- Backend: Returns 400 Bad Request with error message

---

### ❌ Test Case 5: Product Name Too Short
**Input:**
- Name: "AB" (less than 3 characters)
- All other required fields filled

**Expected Result:**
- Frontend: Shows error "Product name must be at least 3 characters"
- Backend: Returns 400 Bad Request

---

### ❌ Test Case 6: Missing Description
**Input:**
- Description: "" (empty)
- All other required fields filled

**Expected Result:**
- Frontend: Shows error "Description is required"
- Backend: Returns 400 Bad Request

---

### ❌ Test Case 7: Invalid Price (Zero or Negative)
**Input:**
- Price: 0 or -100
- All other required fields filled

**Expected Result:**
- Frontend: Shows error "Price must be a valid number greater than 0"
- Backend: Returns 400 Bad Request

---

### ❌ Test Case 8: Invalid Discount Price
**Input:**
- Discount Price: 0 or -50
- All other required fields filled

**Expected Result:**
- Frontend: Shows error "Discount price must be a valid number greater than 0"
- Backend: Returns 400 Bad Request

---

### ❌ Test Case 9: Discount Price Greater Than Regular Price
**Input:**
- Price: 1000
- Discount Price: 1500
- All other required fields filled

**Expected Result:**
- Frontend: Shows error "Discount price cannot be greater than regular price"
- Backend: Returns 400 Bad Request

---

### ❌ Test Case 10: Invalid Stock Quantity
**Input:**
- Stock Quantity: -10
- All other required fields filled

**Expected Result:**
- Frontend: Shows error "Stock quantity must be a valid number (0 or greater)"
- Backend: Returns 400 Bad Request

---

### ❌ Test Case 11: Missing Category
**Input:**
- Category: Not selected
- All other required fields filled

**Expected Result:**
- Frontend: Shows error "Category is required"
- Backend: Returns 400 Bad Request with "Category is required"

---

### ❌ Test Case 12: Invalid Category ID
**Input:**
- Category ID: 99999 (non-existent)
- All other required fields filled

**Expected Result:**
- Backend: Returns 404 Not Found with "Category not found with id: 99999"

---

### ❌ Test Case 13: Invalid SubCategory ID
**Input:**
- Category: Electronics (valid)
- SubCategory ID: 99999 (non-existent)
- All other required fields filled

**Expected Result:**
- Backend: Returns 404 Not Found with "SubCategory not found with id: 99999"

---

### ❌ Test Case 14: Non-Numeric Price
**Input:**
- Price: "abc"
- All other required fields filled

**Expected Result:**
- Frontend: Shows error "Price must be a valid number greater than 0"
- Backend: Returns 400 Bad Request (JSON parsing error)

---

### ❌ Test Case 15: Non-Numeric Stock Quantity
**Input:**
- Stock Quantity: "xyz"
- All other required fields filled

**Expected Result:**
- Frontend: Shows error "Stock quantity must be a valid number (0 or greater)"
- Backend: Returns 400 Bad Request

---

### ✅ Test Case 16: Product with Maximum Length Name
**Input:**
- Name: 200 characters (maximum allowed)
- All other required fields filled

**Expected Result:** Product created successfully

---

### ❌ Test Case 17: Product Name Exceeds Maximum Length
**Input:**
- Name: 201+ characters
- All other required fields filled

**Expected Result:**
- Frontend: HTML5 validation prevents submission
- Backend: Returns 400 Bad Request if bypassed

---

### ✅ Test Case 18: Product with Zero Stock
**Input:**
- Stock Quantity: 0
- All other required fields filled

**Expected Result:** Product created successfully (zero stock is allowed)

---

### ✅ Test Case 19: Product with Optional Fields Empty
**Input:**
- Required fields: All filled
- Brand: "" (empty)
- Color: "" (empty)
- Size: "" (empty)
- SubCategory: Not selected

**Expected Result:** Product created successfully (optional fields can be empty)

---

### ❌ Test Case 20: Unauthorized Access (Not Logged In)
**Input:**
- Valid product data
- No authentication token

**Expected Result:**
- Backend: Returns 401 Unauthorized

---

### ❌ Test Case 21: Customer Trying to Create Product
**Input:**
- Valid product data
- Logged in as Customer role

**Expected Result:**
- Backend: Returns 403 Forbidden or error "Only sellers can create products"

---

### ✅ Test Case 22: Seller Creating Product
**Input:**
- Valid product data
- Logged in as Seller role

**Expected Result:** Product created successfully

---

### ✅ Test Case 23: Admin Creating Product
**Input:**
- Valid product data
- Logged in as Admin role

**Expected Result:** Product created successfully

---

## Field Validation Summary

### Required Fields (Non-Nullable):
1. ✅ **name** - String, min 3 chars, max 200 chars
2. ✅ **description** - String, max 2000 chars
3. ✅ **price** - BigDecimal, > 0
4. ✅ **discountPrice** - BigDecimal, > 0, <= price
5. ✅ **categoryId** - Long, must exist
6. ✅ **stockQuantity** - Integer, >= 0

### Optional Fields (Nullable):
1. ✅ **subCategoryId** - Long, can be null
2. ✅ **brand** - String, can be null
3. ✅ **color** - String, can be null
4. ✅ **size** - String, can be null
5. ✅ **images** - List<MultipartFile>, can be empty

### Auto-Set Fields (Not in Form):
1. ✅ **averageRating** - Defaults to 0.0
2. ✅ **totalReviews** - Defaults to 0
3. ✅ **isActive** - Defaults to true
4. ✅ **isApproved** - Defaults to false
5. ✅ **sellerId** - Set from authenticated user
6. ✅ **createdAt** - Auto-set by JPA
7. ✅ **updatedAt** - Auto-set by JPA

## Testing Checklist

- [x] Valid product creation with all fields
- [x] Valid product creation with minimal required fields
- [x] Frontend validation for all required fields
- [x] Backend validation for all required fields
- [x] Error messages displayed correctly
- [x] Category dropdown loads correctly
- [x] SubCategory dropdown loads based on category selection
- [x] Real-time validation for price comparison
- [x] Image upload handling
- [x] Authentication and authorization checks
- [x] Default values set for averageRating and totalReviews
- [x] Database constraints satisfied

## How to Test

1. **Start the application** - Categories will be auto-created
2. **Login as seller** - Use `seller1@ecommerce.com` / `seller123`
3. **Navigate to Add Product** - `/seller/products/add`
4. **Test each scenario** - Fill/leave fields as per test cases
5. **Check error messages** - Verify they appear correctly
6. **Check backend logs** - For detailed error information
7. **Verify database** - Check that products are saved correctly



