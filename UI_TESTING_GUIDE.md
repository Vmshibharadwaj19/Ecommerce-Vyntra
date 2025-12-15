# UI Testing Guide - Complete Flow

## 🎯 Test Accounts Created

### Admin Account
- **Email:** `admin@ecommerce.com`
- **Password:** `admin123`
- **Role:** ADMIN
- **Access:** Full system access

### Seller Accounts

#### Seller 1 - TechStore
- **Email:** `seller1@ecommerce.com`
- **Password:** `seller123`
- **Business:** TechStore
- **Products:** Electronics (Smartphones, Laptops)

#### Seller 2 - FashionHub
- **Email:** `seller2@ecommerce.com`
- **Password:** `seller123`
- **Business:** FashionHub
- **Products:** Clothing (Men's & Women's)

#### Seller 3 - ElectroMart
- **Email:** `seller3@ecommerce.com`
- **Password:** `seller123`
- **Business:** ElectroMart
- **Products:** Electronics (Smartphones, Laptops)

#### Seller 4 - BookWorld
- **Email:** `seller4@ecommerce.com`
- **Password:** `seller123`
- **Business:** BookWorld
- **Products:** Books

### Customer Accounts

#### Customer 1
- **Email:** `customer1@ecommerce.com`
- **Password:** `customer123`
- **Name:** Alice Customer

#### Customer 2
- **Email:** `customer2@ecommerce.com`
- **Password:** `customer123`
- **Name:** Bob Buyer

---

## 📦 Products Created (25 Products)

### Electronics - Smartphones (4 products)
1. **iPhone 15 Pro** - ₹89,999 (was ₹99,999)
   - Seller: TechStore
   - Stock: 50
   - Rating: 4.5/5 (10 reviews)

2. **Samsung Galaxy S24** - ₹69,999 (was ₹79,999)
   - Seller: TechStore
   - Stock: 30
   - Rating: 4.5/5 (10 reviews)

3. **OnePlus 12** - ₹59,999 (was ₹64,999)
   - Seller: ElectroMart
   - Stock: 40
   - Rating: 4.5/5 (10 reviews)

4. **Google Pixel 8 Pro** - ₹79,999 (was ₹89,999)
   - Seller: ElectroMart
   - Stock: 25
   - Rating: 4.5/5 (10 reviews)

### Electronics - Laptops (4 products)
5. **MacBook Pro 16** - ₹2,29,999 (was ₹2,49,999)
   - Seller: TechStore
   - Stock: 20
   - Rating: 4.5/5 (10 reviews)

6. **Dell XPS 15** - ₹1,29,999 (was ₹1,49,999)
   - Seller: TechStore
   - Stock: 25
   - Rating: 4.5/5 (10 reviews)

7. **HP Spectre x360** - ₹1,09,999 (was ₹1,29,999)
   - Seller: ElectroMart
   - Stock: 18
   - Rating: 4.5/5 (10 reviews)

8. **Lenovo ThinkPad X1 Carbon** - ₹1,19,999 (was ₹1,39,999)
   - Seller: ElectroMart
   - Stock: 22
   - Rating: 4.5/5 (10 reviews)

### Clothing - Men's (3 products)
9. **Men's Casual T-Shirt** - ₹699 (was ₹999)
   - Seller: FashionHub
   - Stock: 100
   - Rating: 4.5/5 (10 reviews)

10. **Men's Denim Jeans** - ₹1,999 (was ₹2,499)
    - Seller: FashionHub
    - Stock: 80
    - Rating: 4.5/5 (10 reviews)

11. **Men's Formal Shirt** - ₹1,499 (was ₹1,999)
    - Seller: FashionHub
    - Stock: 60
    - Rating: 4.5/5 (10 reviews)

### Clothing - Women's (3 products)
12. **Women's Summer Dress** - ₹1,999 (was ₹2,999)
    - Seller: FashionHub
    - Stock: 75
    - Rating: 4.5/5 (10 reviews)

13. **Women's Handbag** - ₹3,999 (was ₹4,999)
    - Seller: FashionHub
    - Stock: 50
    - Rating: 4.5/5 (10 reviews)

14. **Women's Running Shoes** - ₹2,999 (was ₹3,999)
    - Seller: FashionHub
    - Stock: 90
    - Rating: 4.5/5 (10 reviews)

### Books (4 products)
15. **The Great Gatsby** - ₹299 (was ₹499)
    - Seller: BookWorld
    - Stock: 200
    - Rating: 4.5/5 (10 reviews)

16. **To Kill a Mockingbird** - ₹399 (was ₹599)
    - Seller: BookWorld
    - Stock: 150
    - Rating: 4.5/5 (10 reviews)

17. **1984 by George Orwell** - ₹349 (was ₹449)
    - Seller: BookWorld
    - Stock: 180
    - Rating: 4.5/5 (10 reviews)

18. **The Alchemist** - ₹299 (was ₹399)
    - Seller: BookWorld
    - Stock: 220
    - Rating: 4.5/5 (10 reviews)

### Home & Kitchen (5 products)
19. **Coffee Maker** - ₹3,999 (was ₹4,999)
    - Seller: TechStore
    - Stock: 40
    - Rating: 4.5/5 (10 reviews)

20. **Air Fryer** - ₹6,999 (was ₹8,999)
    - Seller: TechStore
    - Stock: 35
    - Rating: 4.5/5 (10 reviews)

21. **Microwave Oven** - ₹5,999 (was ₹6,999)
    - Seller: TechStore
    - Stock: 30
    - Rating: 4.5/5 (10 reviews)

22. **Blender** - ₹2,999 (was ₹3,999)
    - Seller: TechStore
    - Stock: 45
    - Rating: 4.5/5 (10 reviews)

23. **Dining Table Set** - ₹24,999 (was ₹29,999)
    - Seller: TechStore
    - Stock: 15
    - Rating: 4.5/5 (10 reviews)

---

## 🧪 Complete UI Testing Flow

### Step 1: Start the Application

1. **Start Backend:**
   ```bash
   cd C:\Users\vamsh\OneDrive\Desktop\aws\Ecommerce
   mvn spring-boot:run
   ```
   - Wait for: "Started EcommerceApplication"
   - Check console for: "DataInitializer" creating products

2. **Start Frontend:**
   ```bash
   cd frontend
   npm start
   ```
   - Opens at: `http://localhost:3000`

---

### Step 2: Test Customer Dashboard (Home Page)

1. **Open Browser:** Navigate to `http://localhost:3000`

2. **Verify Home Page:**
   - ✅ Should see "Featured Products" heading
   - ✅ Should see Categories section
   - ✅ Should see product grid with 20 products (first page)
   - ✅ Each product card should show:
     - Product image (or placeholder)
     - Product name
     - Brand (if available)
     - Rating (⭐ 4.5)
     - Price (₹ with discount)
     - Stock status

3. **Test Product Display:**
   - ✅ Click on a product card
   - ✅ Should navigate to product details page
   - ✅ Verify product details are shown correctly

4. **Test Pagination:**
   - ✅ Scroll down
   - ✅ Click "Load More" button
   - ✅ Should load next 20 products (if available)

5. **Test Categories:**
   - ✅ Click on a category
   - ✅ Should filter products by category

---

### Step 3: Test Customer Login & Shopping

1. **Login as Customer:**
   - Click "Login" in navbar
   - Email: `customer1@ecommerce.com`
   - Password: `customer123`
   - Click "Login"

2. **Verify Login:**
   - ✅ Should redirect to home page
   - ✅ Navbar should show user name
   - ✅ Should see "Logout" option

3. **Browse Products:**
   - ✅ Navigate through products
   - ✅ Click on product to see details
   - ✅ Verify all product information is displayed

4. **Add to Cart:**
   - ✅ Click "Add to Cart" on a product
   - ✅ Cart icon should update with count
   - ✅ Go to Cart page
   - ✅ Verify product is in cart

5. **Test Wishlist:**
   - ✅ Click "Add to Wishlist" on a product
   - ✅ Go to Wishlist page
   - ✅ Verify product is in wishlist

---

### Step 4: Test Seller Dashboard

1. **Login as Seller:**
   - Logout if logged in
   - Login with: `seller1@ecommerce.com` / `seller123`

2. **Verify Seller Dashboard:**
   - ✅ Should see seller dashboard
   - ✅ Should see "My Products" section
   - ✅ Should see products created by this seller

3. **View Products:**
   - ✅ Click "My Products"
   - ✅ Should see list of products
   - ✅ Verify product count matches

4. **Add New Product:**
   - ✅ Click "Add Product"
   - ✅ Fill form with:
     - Name: "Test Product"
     - Description: "Test description"
     - Price: 1000
     - Discount Price: 899
     - Stock: 50
     - Category: Select from dropdown
     - SubCategory: Select from dropdown
   - ✅ Upload image (optional)
   - ✅ Click "Create Product"
   - ✅ Should see success message
   - ✅ Product should appear in "My Products"

---

### Step 5: Test Admin Dashboard

1. **Login as Admin:**
   - Logout if logged in
   - Login with: `admin@ecommerce.com` / `admin123`

2. **Verify Admin Dashboard:**
   - ✅ Should see admin dashboard
   - ✅ Should see statistics:
     - Total Users
     - Total Products
     - Total Orders
     - Total Revenue

3. **Manage Products:**
   - ✅ Go to "Manage Products"
   - ✅ Should see all products from all sellers
   - ✅ Can approve/reject products
   - ✅ Can view product details

4. **Manage Users:**
   - ✅ Go to "Manage Users"
   - ✅ Should see all users
   - ✅ Can block/unblock users
   - ✅ Can approve sellers

---

### Step 6: Test Product Visibility

1. **Check Product Approval:**
   - All products created by DataInitializer are:
     - ✅ `isActive = true`
     - ✅ `isApproved = true`
   - These should be visible on customer dashboard

2. **Test New Product Approval:**
   - Login as seller
   - Create a new product
   - Product should have `isApproved = false` initially
   - Login as admin
   - Approve the product
   - Login as customer
   - Product should now be visible

---

## 🐛 Troubleshooting

### Products Not Showing?

1. **Check Backend Logs:**
   - Look for "DataInitializer" messages
   - Verify products are being created
   - Check for any errors

2. **Verify Product Status:**
   - Products must have:
     - `isActive = true`
     - `isApproved = true`
   - Check database or use admin panel

3. **Check API Response:**
   - Open browser DevTools (F12)
   - Go to Network tab
   - Check `/api/products/public` request
   - Verify response contains products

4. **Check Console Errors:**
   - Open browser DevTools (F12)
   - Go to Console tab
   - Look for JavaScript errors
   - Fix any errors found

### Images Not Loading?

1. **Image URLs:**
   - Products use placeholder image service
   - Images show product name if image file doesn't exist
   - This is expected behavior for test data

2. **To Add Real Images:**
   - Upload images through seller dashboard
   - Images will be stored in `/uploads/products/`
   - Update product with image paths

---

## ✅ Expected Results

After following this guide, you should see:

1. ✅ **25 products** on the home page
2. ✅ **4 categories** in the categories section
3. ✅ **Product cards** with all information displayed
4. ✅ **Working navigation** between pages
5. ✅ **Functional cart** and wishlist
6. ✅ **Seller dashboard** showing products
7. ✅ **Admin dashboard** with statistics

---

## 📝 Notes

- All products are **pre-approved** for testing
- Images use **placeholder service** (shows product name)
- All sellers are **pre-approved**
- All test accounts are **active and unblocked**

---

## 🎉 Success Criteria

✅ Customer can see products on home page
✅ Products display correctly with all information
✅ Navigation works between pages
✅ Cart and wishlist function properly
✅ Seller can manage products
✅ Admin can manage system

If all criteria are met, the UI testing is **SUCCESSFUL**! 🎊



