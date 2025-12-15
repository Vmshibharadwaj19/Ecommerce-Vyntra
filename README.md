# 🛒 Amazon-Style E-Commerce Application (Vyntra)

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

markdown
Copy code

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
- Razorpay account (for payment integration)

---

## 🔧 Backend Setup

1. **Navigate to project**
```bash
cd Ecommerce
Create MySQL database

sql
Copy code
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
