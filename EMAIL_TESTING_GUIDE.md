# Email Testing Guide - VYNTRA E-Commerce

## 📧 Email Triggers and How to Test Them

### Prerequisites: Enable Email First

1. **Update `application.properties`:**
   ```properties
   # Enable email
   mail.enabled=true
   
   # Configure Gmail SMTP (for testing)
   spring.mail.host=smtp.gmail.com
   spring.mail.port=587
   spring.mail.username=your-email@gmail.com
   spring.mail.password=your-app-password
   mail.from.address=your-email@gmail.com
   mail.from.name=VYNTRA E-Commerce
   ```

2. **Get Gmail App Password:**
   - Go to: https://myaccount.google.com/apppasswords
   - Generate an app password for "Mail"
   - Use that password in `spring.mail.password`

3. **Rebuild and Restart** the application

---

## 🧪 Email Test Scenarios

### 1. **Seller Approval Email** ✅
**When:** Admin approves a seller account

**How to Test:**
1. Login as **Admin** (or create a seller account)
2. Go to **Admin Dashboard** → **Manage Sellers**
3. Find a seller with status "Pending"
4. Click **"Approve"** button
5. **Email sent to:** Seller's email address

**Expected Email:**
- Subject: "Your Seller Account Has Been Approved! - VYNTRA"
- Contains: Approval message, welcome text, next steps

---

### 2. **Seller Rejection Email** ❌
**When:** Admin rejects a seller account

**How to Test:**
1. Login as **Admin**
2. Go to **Admin Dashboard** → **Manage Sellers**
3. Find a seller with status "Pending"
4. Click **"Reject"** button
5. **Email sent to:** Seller's email address

**Expected Email:**
- Subject: "Seller Account Application Status - VYNTRA"
- Contains: Rejection message, reason, contact info

---

### 3. **Order Confirmation Email** 📦
**When:** Customer places an order (any payment method)

**How to Test:**
1. Login as **Customer**
2. Add products to cart
3. Go to **Checkout**
4. Select payment method (COD, TEST, or Razorpay)
5. Complete the order
6. **Email sent to:** Customer's email address

**Expected Email:**
- Subject: "Order Confirmed - ORD-XXXXX - VYNTRA"
- Contains: Order details, items, total, shipping address

---

### 4. **Order Shipped Email** 🚚
**When:** Admin/Seller updates order status to "SHIPPED"

**How to Test:**
1. Login as **Admin** or **Seller**
2. Go to **Orders** page
3. Find an order with status "CONFIRMED"
4. Change status to **"SHIPPED"**
5. Click **"Update Status"**
6. **Email sent to:** Customer's email address

**Expected Email:**
- Subject: "Your Order Has Been Shipped - ORD-XXXXX - VYNTRA"
- Contains: Shipping confirmation, tracking info, estimated delivery

---

### 5. **Order Delivered Email** ✅
**When:** Admin/Seller updates order status to "DELIVERED"

**How to Test:**
1. Login as **Admin** or **Seller**
2. Go to **Orders** page
3. Find an order with status "SHIPPED"
4. Change status to **"DELIVERED"**
5. Click **"Update Status"**
6. **Email sent to:** Customer's email address

**Expected Email:**
- Subject: "Your Order Has Been Delivered - ORD-XXXXX - VYNTRA"
- Contains: Delivery confirmation, thank you message

---

### 6. **Order Cancelled Email** ❌
**When:** Admin/Seller cancels an order

**How to Test:**
1. Login as **Admin** or **Seller**
2. Go to **Orders** page
3. Find any order
4. Change status to **"CANCELLED"**
5. Click **"Update Status"**
6. **Email sent to:** Customer's email address

**Expected Email:**
- Subject: "Order Cancelled - ORD-XXXXX - VYNTRA"
- Contains: Cancellation notice, refund info (if applicable)

---

### 7. **Order Status Update Email** 📋
**When:** Order status changes to any other status (not shipped/delivered/cancelled)

**How to Test:**
1. Login as **Admin** or **Seller**
2. Go to **Orders** page
3. Update order status to any other status (e.g., "PROCESSING", "PENDING")
4. Click **"Update Status"**
5. **Email sent to:** Customer's email address

**Expected Email:**
- Subject: "Order Status Updated - ORD-XXXXX - VYNTRA"
- Contains: Status update, current status, next steps

---

## 🔍 Quick Test Checklist

### Easy Tests (No setup needed):
- ✅ **Order Confirmation** - Just place an order
- ✅ **Order Shipped** - Update order status to SHIPPED
- ✅ **Order Delivered** - Update order status to DELIVERED
- ✅ **Order Cancelled** - Update order status to CANCELLED

### Requires Seller Account:
- ✅ **Seller Approval** - Approve a seller in Admin Dashboard
- ✅ **Seller Rejection** - Reject a seller in Admin Dashboard

---

## 🛠️ Troubleshooting

### Email Not Sending?

1. **Check `mail.enabled=true`** in `application.properties`
2. **Verify Gmail credentials** are correct
3. **Check application logs** for email errors
4. **Verify recipient email** exists and is correct
5. **Check spam folder** - emails might be filtered

### Email Sending but Not Received?

1. Check **spam/junk folder**
2. Verify **email address** is correct in user profile
3. Check **Gmail security settings** - might need to allow "Less secure apps"
4. Check **application logs** for SMTP errors

### Testing Without Real Email?

Set `mail.enabled=false` - emails will be **logged** instead of sent:
- Check console logs for email content
- Subject and body will be printed

---

## 📝 Email Content Preview

All emails include:
- ✅ Professional HTML formatting
- ✅ VYNTRA branding
- ✅ Order/user details
- ✅ Action buttons/links
- ✅ Contact information
- ✅ Responsive design

---

## 🚀 Quick Start Testing

**Fastest way to test email:**

1. **Place an order** (as customer) → Triggers Order Confirmation email
2. **Update order to SHIPPED** (as admin) → Triggers Shipped email
3. **Update order to DELIVERED** (as admin) → Triggers Delivered email

All emails go to the **customer's email address** from their profile!

---

## 📧 Email Configuration Summary

```properties
# Enable/Disable
mail.enabled=true

# Gmail SMTP
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=your-email@gmail.com
spring.mail.password=your-app-password

# From Address
mail.from.address=your-email@gmail.com
mail.from.name=VYNTRA E-Commerce
```

**Remember:** After changing email settings, **restart the application**!

