# Order Email Notifications - VYNTRA

## ✅ Email Features Implemented

### 1. Order Confirmation Email
- **When:** Order is placed successfully
- **Recipient:** Customer
- **Content:**
  - Order number
  - Order date
  - Payment status
  - List of items with quantities and prices
  - Total amount
  - Link to view order details

### 2. Order Shipped Email
- **When:** Admin/Seller marks order as "SHIPPED"
- **Recipient:** Customer
- **Content:**
  - Order number
  - Shipped date
  - Estimated delivery (3-5 business days)
  - Link to track order

### 3. Order Delivered Email
- **When:** Admin/Seller marks order as "DELIVERED"
- **Recipient:** Customer
- **Content:**
  - Order number
  - Delivered date
  - Request for review/feedback
  - Link to view order and leave review

### 4. Order Cancelled Email
- **When:** Admin/Seller cancels an order
- **Recipient:** Customer
- **Content:**
  - Order number
  - Order amount
  - Refund information (if applicable)
  - Support contact information

### 5. Order Status Update Email
- **When:** Order status changes to any other status
- **Recipient:** Customer
- **Content:**
  - Order number
  - New status
  - Link to view order details

---

## 📧 Email Flow

### Order Placement:
```
Customer places order
    ↓
Order created in database
    ↓
Order confirmation email sent ✅
```

### Order Status Updates:
```
Admin/Seller updates order status
    ↓
Status saved to database
    ↓
Email sent based on status:
    - SHIPPED → Shipped email ✅
    - DELIVERED → Delivered email ✅
    - CANCELLED → Cancelled email ✅
    - Other → Status update email ✅
```

---

## 🎨 Email Templates

All emails include:
- ✅ Professional HTML design
- ✅ VYNTRA branding
- ✅ Responsive layout
- ✅ Order details
- ✅ Action buttons/links
- ✅ Professional styling

### Email Colors:
- **Order Confirmed:** Green gradient
- **Order Shipped:** Blue gradient
- **Order Delivered:** Green gradient
- **Order Cancelled:** Red gradient
- **Status Update:** Gray gradient

---

## 🔧 Configuration

Email notifications use the same configuration as seller notifications:

```properties
# Email Configuration (in application.properties)
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=your-email@gmail.com
spring.mail.password=your-app-password
mail.enabled=true
```

---

## 🧪 Testing

### Test Order Confirmation:
1. Login as customer
2. Add items to cart
3. Checkout and place order
4. **Expected:** Order confirmation email sent

### Test Order Shipped:
1. Login as admin/seller
2. Go to Orders
3. Find an order and click "Ship" or "Mark as Shipped"
4. **Expected:** Shipped email sent to customer

### Test Order Delivered:
1. Login as admin/seller
2. Go to Orders
3. Find a shipped order and click "Deliver" or "Mark as Delivered"
4. **Expected:** Delivered email sent to customer

### Test Order Cancelled:
1. Login as admin
2. Go to Orders
3. Click "Cancel" on an order
4. **Expected:** Cancelled email sent to customer

---

## 📋 Email Triggers

| Action | Email Sent | Recipient |
|--------|-----------|-----------|
| Order placed | Order Confirmation | Customer |
| Status → SHIPPED | Order Shipped | Customer |
| Status → DELIVERED | Order Delivered | Customer |
| Status → CANCELLED | Order Cancelled | Customer |
| Status → Other | Status Update | Customer |

---

## ✅ Status

- ✅ Order confirmation emails
- ✅ Order shipped emails
- ✅ Order delivered emails
- ✅ Order cancelled emails
- ✅ Order status update emails
- ✅ HTML email templates
- ✅ Error handling (emails don't break order processing)
- ✅ Logging for debugging

---

**Note:** If email is disabled (`mail.enabled=false`), emails are logged but not sent. This ensures order processing continues even if email service is unavailable.

