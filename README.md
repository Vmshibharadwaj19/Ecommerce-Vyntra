# 🛒 Amazon-Style E-Commerce Application (Vyntra)
[![Java version](https://img.shields.io/badge/Java-17+-blue)](https://www.oracle.com/java/)  [![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-green)](https://spring.io/projects/spring-boot)  [![React](https://img.shields.io/badge/React-18+-cyan)](https://reactjs.org/)  [![MySQL](https://img.shields.io/badge/MySQL-8+-blue)](https://www.mysql.com/)  
## 📚 Documentation & Guides  

A **complete full-stack e-commerce application** built using **Spring Boot (Backend)** and **React (Frontend)**.  
The application supports **multiple user roles**, **Razorpay payment integration**, and **real-world product approval workflows** similar to Amazon / Flipkart.

This project is designed to be **production-ready** and demonstrates strong backend ownership, security, and business logic implementation.

---

## 🚀 Tech Stack

### Backend
- **Spring Boot 3.2.0** (Java 17)
- **Spring Data JPA** – Database operations
- **Hibernate** – ORM
- **Spring Security** – JWT Authentication & Authorization
- **MySQL** – Relational database
- **Razorpay** – Payment gateway
- **Lombok** – Boilerplate reduction
- **ModelMapper** – DTO mapping

### Frontend
- **React 18.2.0**
- **React Router 6.20.0** – Routing
- **Axios** – HTTP client
- **Context API** – State management
- **CSS Modules** – Styling

---

## 👥 User Roles

- **ROLE_CUSTOMER** – End users purchasing products
- **ROLE_SELLER** – Product sellers (requires admin approval)
- **ROLE_ADMIN** – Platform administrators

---

## ✨ Features

### 👤 Customer Features
- User registration and login  
- Browse products with search and filters  
- Add products to cart  
- Wishlist functionality  
- Checkout with Razorpay payment  
- Order history and tracking  
- Product reviews and ratings  
- Address management  

### 🧑‍💼 Seller Features
- Seller registration (requires admin approval)  
- Add / Edit / Delete products  
- Upload multiple product images  
- Inventory management  
- View and manage received orders  
- Order status updates (Shipped / Delivered)  

### 🛡️ Admin Features
- User management (Block / Unblock)  
- Seller approval and rejection  
- Product approval and rejection  
- Category and sub-category management  
- View all orders across the system  
- Dashboard with statistics (Revenue, Orders, Users, Products)  

---
```
## 📂 Project Structure

Ecommerce/
├── src/main/java/com/ecommerce/
│ ├── config/ # Configuration classes
│ ├── controllers/ # REST controllers
│ ├── dto/ # Data Transfer Objects
│ ├── entities/ # JPA entities
│ ├── exceptions/ # Global exception handling
│ ├── repositories/ # JPA repositories
│ ├── security/ # JWT & Spring Security configuration
│ ├── services/ # Business logic
│ └── utils/ # Utility classes
├── frontend/
│ ├── src/
│ │ ├── api/ # API service files
│ │ ├── components/ # Reusable UI components
│ │ ├── context/ # Context providers
│ │ ├── pages/ # Page components
│ │ └── Router.jsx # Routing configuration
│ └── public/
├── uploads/ # Product images
├── README.md
└── *.md # Feature documentation files
```
markdown


---

## 📘 Feature & Process Documentation

### 🔐 Email & Authentication
- [GMAIL_APP_PASSWORD_SETUP.md](./GMAIL_APP_PASSWORD_SETUP.md)
- [ORDER_EMAIL_NOTIFICATIONS.md](./ORDER_EMAIL_NOTIFICATIONS.md)

### 🛍️ Product Approval & Visibility
- [PRODUCT_APPROVAL_GUIDE.md](./PRODUCT_APPROVAL_GUIDE.md)
- [PRODUCT_APPROVAL_PROCESS.md](./PRODUCT_APPROVAL_PROCESS.md)
- [HOW_TO_APPROVE_PRODUCTS.md](./HOW_TO_APPROVE_PRODUCTS.md)
- [PRODUCT_APPROVAL_FIX.md](./PRODUCT_APPROVAL_FIX.md)
- [PRODUCT_VISIBILITY_FIX.md](./PRODUCT_VISIBILITY_FIX.md)
- [QUICK_FIX_PRODUCTS.md](./QUICK_FIX_PRODUCTS.md)

### 💳 Payments
- [PAYMENT_OPTIONS_GUIDE.md](./PAYMENT_OPTIONS_GUIDE.md)
- [FIX_PAYMENT_NULL_ISSUE.md](./FIX_PAYMENT_NULL_ISSUE.md)

### 🧪 Testing & Validation
- [OPERATION_TEST_GUIDE.md](./OPERATION_TEST_GUIDE.md)

---

## ⚙️ Setup Instructions

### Prerequisites
- Java 17 or higher  
- Maven 3.6+  
- MySQL 8.0+  
- Node.js 16+ and npm  
- Razorpay account  

---

## 🔧 Backend Setup

### Create MySQL Database
```sql
CREATE DATABASE ecommerce_db;
Configure Database
properties
Copy code
spring.datasource.username=your_username
spring.datasource.password=your_password
Configure Razorpay
properties
Copy code
razorpay.key.id=your_razorpay_key_id
razorpay.key.secret=your_razorpay_key_secret
Configure JWT
properties
Copy code
jwt.secret=YourSecretKeyForJWTTokenGenerationThatShouldBeAtLeast256BitsLong
Run Backend
bash
Copy code
mvn clean install
mvn spring-boot:run
Backend runs on:

arduino
Copy code
http://localhost:8080
🎨 Frontend Setup
bash
Copy code
cd frontend
npm install
npm start
Frontend runs on:

arduino
Copy code
http://localhost:3000
🔗 API Endpoints
Authentication
POST /api/auth/signin

POST /api/auth/signup

GET /api/auth/me

Products
GET /api/products/public

GET /api/products/public/{id}

POST /api/products/search

POST /api/products

PUT /api/products/{id}

DELETE /api/products/{id}

Cart
GET /api/cart

POST /api/cart/add

PUT /api/cart/items/{id}

DELETE /api/cart/items/{id}

DELETE /api/cart/clear

Orders
POST /api/orders

GET /api/orders

GET /api/orders/{id}

Payment
POST /api/payment/create-order

POST /api/payment/verify

Admin
GET /api/admin/dashboard

PUT /api/admin/products/{id}/approve

PUT /api/admin/users/{id}/block

🔐 Security
JWT-based authentication

BCrypt password encryption

Role-based access control (RBAC)

Secure payment verification

CORS configured for frontend

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

🧪 Testing
bash
Copy code
mvn test
cd frontend
npm test
🚀 Production Deployment
Backend
bash
Copy code
mvn clean package
java -jar target/ecommerce-backend-1.0.0.jar
Frontend
bash
Copy code
npm run build
Deploy the build/ folder using Nginx or Apache.

🧠 Why This Project Matters
Real-world admin approval workflows

Clean REST API design

Strong Spring Security + JWT

Proper separation of concerns

Suitable for startup and product-based company interviews

👨‍💻 Author
Vamshi Prasad Goteti
Full Stack Java Developer
(Spring Boot | React | JPA | SQL)

markdown
Copy code
