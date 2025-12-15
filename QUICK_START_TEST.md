# Quick Start Testing Guide

## 🚀 Quick Test Steps

### 1. Restart Backend
```bash
# Stop current backend (Ctrl+C)
# Then restart:
mvn spring-boot:run
```

**Wait for:**
- ✅ "Started EcommerceApplication"
- ✅ Check console for "DataInitializer" messages
- ✅ Should see products being created

### 2. Open Frontend
```bash
cd frontend
npm start
```

**Opens at:** `http://localhost:3000`

### 3. Test Customer View (No Login Required)

1. **Open Home Page:**
   - Navigate to `http://localhost:3000`
   - Should see **"Featured Products"** heading

2. **Verify Products Display:**
   - ✅ Should see **20 products** on first page
   - ✅ Each product shows:
     - Image (or placeholder with product name)
     - Product name
     - Brand (if available)
     - ⭐ Rating (4.5)
     - Price (₹ with discount)
     - Stock status

3. **Test Product Click:**
   - Click any product card
   - Should navigate to product details page
   - Verify all product information is shown

### 4. Test Login & Shopping

**Login as Customer:**
- Email: `customer1@ecommerce.com`
- Password: `customer123`

**After Login:**
- ✅ Should see user name in navbar
- ✅ Can add products to cart
- ✅ Can add products to wishlist
- ✅ Can view cart and wishlist

### 5. Test Seller Dashboard

**Login as Seller:**
- Email: `seller1@ecommerce.com`
- Password: `seller123`

**Verify:**
- ✅ Should see seller dashboard
- ✅ "My Products" shows products created by this seller
- ✅ Can add new products
- ✅ Can edit/delete own products

### 6. Test Admin Dashboard

**Login as Admin:**
- Email: `admin@ecommerce.com`
- Password: `admin123`

**Verify:**
- ✅ Should see admin dashboard with statistics
- ✅ Can view all products
- ✅ Can approve/reject products
- ✅ Can manage users
- ✅ Can block/unblock users

---

## 📊 Expected Results

### Products Created:
- ✅ **25 products** total
- ✅ **4 categories**: Electronics, Clothing, Books, Home & Kitchen
- ✅ **4 sellers** with products
- ✅ All products are **approved and active**

### Product Distribution:
- **Electronics (Smartphones):** 4 products
- **Electronics (Laptops):** 4 products
- **Clothing (Men's):** 3 products
- **Clothing (Women's):** 3 products
- **Books:** 4 products
- **Home & Kitchen:** 5 products

---

## 🐛 Troubleshooting

### Products Not Showing?

1. **Check Backend Console:**
   - Look for "DataInitializer" messages
   - Should see products being created
   - Check for any errors

2. **Check Product Status:**
   - Products must be `isActive = true` AND `isApproved = true`
   - All test products are pre-approved

3. **Check Browser Console (F12):**
   - Look for JavaScript errors
   - Check Network tab
   - Verify API calls are successful

4. **Check API Response:**
   - Open DevTools → Network tab
   - Find `/api/products/public` request
   - Check response - should contain products array

### Images Not Showing?

- **Expected:** Products use placeholder images
- Images show product name if file doesn't exist
- This is normal for test data
- Real images can be uploaded through seller dashboard

---

## ✅ Success Checklist

- [ ] Backend starts without errors
- [ ] Frontend loads at `http://localhost:3000`
- [ ] Home page shows products
- [ ] Product cards display correctly
- [ ] Can click products to see details
- [ ] Customer login works
- [ ] Cart functionality works
- [ ] Seller dashboard shows products
- [ ] Admin dashboard shows statistics
- [ ] All navigation works

---

## 🎯 Quick Test Commands

### Check Products via API:
```bash
# Open browser and go to:
http://localhost:8080/api/products/public?page=0&size=20

# Should return JSON with products array
```

### Check Categories:
```bash
http://localhost:8080/api/categories

# Should return all categories
```

---

## 📝 Notes

- All test accounts are **pre-configured**
- All products are **pre-approved**
- All sellers are **pre-approved**
- Images use **placeholder service** (shows product name)

---

## 🎉 If Everything Works:

✅ **25 products** visible on home page
✅ **Product cards** display correctly
✅ **Navigation** works between pages
✅ **Login** works for all roles
✅ **Cart & Wishlist** function properly

**You're all set! The application is working correctly.** 🚀



