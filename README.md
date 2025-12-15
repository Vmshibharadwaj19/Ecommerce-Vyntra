# 🛒 Vyntra — Amazon-Style E-Commerce Platform

[![Java](https://img.shields.io/badge/Java-17+-blue)](https://www.oracle.com/java/)[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-brightgreen)](https://spring.io/projects/spring-boot)[![React](https://img.shields.io/badge/React-18+-cyan)](https://reactjs.org/)[![MySQL](https://img.shields.io/badge/MySQL-8+-blue)](https://www.mysql.com/)[![Razorpay](https://img.shields.io/badge/Razorpay-Payments-purple)](https://razorpay.com/)

**Vyntra** is a **full-stack e-commerce platform** inspired by real-world systems like **Amazon and Flipkart**, built with **Spring Boot** and **React**.  
It supports **Customer, Seller, and Admin roles**, implements **secure JWT authentication**, **product approval workflows**, **email notifications**, and **Razorpay payment integration**.

The project is designed with **production-grade architecture**, clear separation of concerns, and realistic business logic suitable for **startup and product-based company interviews**.

---

## 📚 Documentation & Guides

| Document | Description |
|--------|-------------|
| [PRODUCT_APPROVAL_GUIDE.md](./PRODUCT_APPROVAL_GUIDE.md) | Complete guide for admin-based product approval |
| [PRODUCT_APPROVAL_PROCESS.md](./PRODUCT_APPROVAL_PROCESS.md) | End-to-end product approval workflow |
| [HOW_TO_APPROVE_PRODUCTS.md](./HOW_TO_APPROVE_PRODUCTS.md) | Step-by-step admin approval instructions |
| [PRODUCT_APPROVAL_FIX.md](./PRODUCT_APPROVAL_FIX.md) | Fixes related to product approval issues |
| [PRODUCT_VISIBILITY_FIX.md](./PRODUCT_VISIBILITY_FIX.md) | Resolving product visibility problems |
| [QUICK_FIX_PRODUCTS.md](./QUICK_FIX_PRODUCTS.md) | Common quick fixes for product issues |
| [PAYMENT_OPTIONS_GUIDE.md](./PAYMENT_OPTIONS_GUIDE.md) | Supported payment options and logic |
| [FIX_PAYMENT_NULL_ISSUE.md](./FIX_PAYMENT_NULL_ISSUE.md) | Razorpay payment null issue resolution |
| [ORDER_EMAIL_NOTIFICATIONS.md](./ORDER_EMAIL_NOTIFICATIONS.md) | Order lifecycle email notifications |
| [GMAIL_APP_PASSWORD_SETUP.md](./GMAIL_APP_PASSWORD_SETUP.md) | Gmail SMTP configuration guide |
| [OPERATION_TEST_GUIDE.md](./OPERATION_TEST_GUIDE.md) | API testing & validation scenarios |

---

## 🚀 Tech Stack

### Backend
- Spring Boot 3.2.x (Java 17)
- Spring Data JPA / Hibernate
- Spring Security (JWT & RBAC)
- MySQL 8.x
- Razorpay Payment Gateway
- Lombok
- ModelMapper

### Frontend
- React 18
- React Router DOM v6
- Axios
- Context API
- CSS Modules

### Other
- RESTful APIs
- Email notifications (SMTP)
- Role-based access control

---

## 👥 User Roles & Access

- **Customer**
  - Browse products
  - Cart & checkout
  - Payments & order tracking
- **Seller**
  - Product & inventory management
  - Order fulfillment
  - Requires admin approval
- **Admin**
  - Seller & product approvals
  - User management
  - System-wide monitoring

---

## 🎯 Key Features

### 🛍️ Customer
- Registration & login
- Product search and filters
- Cart & wishlist
- Razorpay checkout
- Order history & tracking
- Reviews & ratings
- Address management

### 🧑‍💼 Seller
- Seller onboarding (admin approval)
- Product CRUD operations
- Multiple image uploads
- Inventory management
- Order status updates

### 🛡️ Admin
- Seller approval / rejection
- Product approval workflow
- User block / unblock
- Category management
- Dashboard analytics (orders, revenue, users)

---

## 📂 Project Structure
```
Vyntra/
├── backend/ # Spring Boot application
│ ├── controllers/
│ ├── services/
│ ├── repositories/
│ ├── entities/
│ ├── dto/
│ ├── security/
│ └── config/
├── frontend/ # React application
│ ├── components/
│ ├── context/
│ ├── services/
│ └── pages/
├── uploads/ # Product images
├── *.md # Feature documentation
└── README.md
```
yaml
Copy code

---

## 🔐 Security Overview

- JWT-based authentication
- BCrypt password encryption
- Role-based access control (RBAC)
- Secure Razorpay payment verification
- Global exception handling
- CORS configuration for frontend

---

## 📡 API Overview

The system exposes **RESTful APIs** for:
- Authentication & authorization
- Product management
- Cart & wishlist
- Orders & payments
- Seller workflows
- Admin approvals & dashboards

A complete API list is documented in the repository and follows REST best practices.
## 📡 API Documentation

**Base URL:**  
/api

yaml
Copy code

All APIs are secured using **JWT-based authentication** and **Role-Based Access Control (RBAC)** unless marked as public.

---

## 🔐 Authentication & Authorization

- `POST /api/auth/signup`  
  Register a new user (Customer / Seller)

- `POST /api/auth/signin`  
  Authenticate user and return JWT token

- `GET /api/auth/me`  
  Get currently logged-in user details

---

## 👤 User Profile

- `GET /api/users/profile`  
  Fetch user profile details

- `PUT /api/users/profile`  
  Update user profile

- `PUT /api/users/change-password`  
  Change account password

---

## 🛍️ Products

### Public APIs
- `GET /api/products/public`  
  Get all approved products

- `GET /api/products/public/{productId}`  
  Get product details by ID

- `POST /api/products/search`  
  Search products using filters and keywords

- `GET /api/products/category/{categoryId}`  
  Get products by category

---

### Seller APIs
- `POST /api/products`  
  Add a new product (Seller / Admin)

- `PUT /api/products/{productId}`  
  Update product details

- `DELETE /api/products/{productId}`  
  Delete a product

- `GET /api/products/seller`  
  Get all products added by the seller

---

### Admin APIs
- `GET /api/admin/products/pending`  
  Get products pending approval

- `PUT /api/admin/products/{productId}/approve`  
  Approve product

- `PUT /api/admin/products/{productId}/reject`  
  Reject product

---

## 🛒 Cart

- `GET /api/cart`  
  Get current user cart

- `POST /api/cart/add`  
  Add product to cart

- `PUT /api/cart/items/{cartItemId}`  
  Update cart item quantity

- `DELETE /api/cart/items/{cartItemId}`  
  Remove item from cart

- `DELETE /api/cart/clear`  
  Clear entire cart

---

## ❤️ Wishlist

- `GET /api/wishlist`  
  Get wishlist items

- `POST /api/wishlist/add/{productId}`  
  Add product to wishlist

- `DELETE /api/wishlist/remove/{productId}`  
  Remove product from wishlist

---

## 📦 Orders

### Customer APIs
- `POST /api/orders`  
  Place an order

- `GET /api/orders`  
  Get all orders of the logged-in user

- `GET /api/orders/{orderId}`  
  Get order details

- `PUT /api/orders/{orderId}/cancel`  
  Cancel order

---

### Seller APIs
- `GET /api/seller/orders`  
  Get orders for seller products

- `PUT /api/seller/orders/{orderId}/ship`  
  Mark order as shipped

- `PUT /api/seller/orders/{orderId}/deliver`  
  Mark order as delivered

---

## 💳 Payments (Razorpay)

- `POST /api/payment/create-order`  
  Create Razorpay payment order

- `POST /api/payment/verify`  
  Verify Razorpay payment signature

- `GET /api/payment/status/{orderId}`  
  Fetch payment status

---

## 🏠 Address Management

- `GET /api/addresses`  
  Get saved addresses

- `POST /api/addresses`  
  Add new address

- `PUT /api/addresses/{addressId}`  
  Update address

- `DELETE /api/addresses/{addressId}`  
  Delete address

---

## ⭐ Reviews & Ratings

- `POST /api/reviews/{productId}`  
  Add product review

- `GET /api/reviews/{productId}`  
  Get all reviews for a product

- `DELETE /api/reviews/{reviewId}`  
  Delete review

---

## 🧑‍💼 Seller Management (Admin)

- `GET /api/admin/sellers`  
  Get all sellers

- `PUT /api/admin/sellers/{sellerId}/approve`  
  Approve seller

- `PUT /api/admin/sellers/{sellerId}/reject`  
  Reject seller

- `PUT /api/admin/sellers/{sellerId}/block`  
  Block seller

---

## 👮 User Management (Admin)

- `GET /api/admin/users`  
  Get all users

- `PUT /api/admin/users/{userId}/block`  
  Block user

- `PUT /api/admin/users/{userId}/unblock`  
  Unblock user

---

## 📊 Admin Dashboard

- `GET /api/admin/dashboard`  
  Fetch dashboard statistics:
  - Total users
  - Total sellers
  - Total products
  - Total orders
  - Total revenue

---

## 📧 Email Notifications (Automated)

Triggered automatically for:
- Order placed
- Order shipped
- Order delivered
- Order cancelled
- Product approval / rejection

Detailed implementation is available in:  
- [ORDER_EMAIL_NOTIFICATIONS.md](./ORDER_EMAIL_NOTIFICATIONS.md)

---

## ✅ Interview Summary

> “This application exposes **RESTful APIs** for authentication, product management, cart, orders, Razorpay payments, seller workflows, and admin approvals, secured using **JWT-based authentication and role-based access control**.”
---

## 🧪 Testing & Validation

- API validation using Postman
- Role-based access testing
- Edge-case handling (payments, approvals)
- Business-rule enforcement at service layer

Refer to:
- [OPERATION_TEST_GUIDE.md](./OPERATION_TEST_GUIDE.md)

---

## 🚀 Production Readiness Notes

- Environment-based configuration recommended
- Secrets should be externalized (env variables)
- HTTPS required in production
- Database backups & monitoring advised
- Logging & error tracking can be integrated

---

## 🧠 Why This Project Matters

- Mirrors **real e-commerce business workflows**
- Demonstrates **backend ownership**
- Clean Spring Boot architecture
- Realistic admin & seller approval flows
- Suitable for **startup / product company evaluation**

---

## 👤 Author

**Vamshi Prasad Goteti**  
Full-Stack Java Developer  
Spring Boot • React • JPA • SQL

---

