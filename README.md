# 🛒 Amazon-Style E-Commerce Application (Vyntra)

A **production-ready full-stack e-commerce application** built using **Spring Boot (Backend)** and **React (Frontend)**.  
The system supports **Customer, Seller, and Admin roles**, integrates **Razorpay payments**, and implements **real-world approval workflows** similar to Amazon/Flipkart.

---

## 🚀 Tech Stack

### Backend
- **Spring Boot 3.2.0** (Java 17)
- **Spring Data JPA / Hibernate**
- **Spring Security** (JWT Authentication & RBAC)
- **MySQL**
- **Razorpay Payment Gateway**
- **Lombok**
- **ModelMapper**

### Frontend
- **React 18.2.0**
- **React Router v6**
- **Axios**
- **Context API**
- **CSS Modules**

---

## 👥 User Roles

- **Customer** – Shopping, orders, payments
- **Seller** – Product & inventory management (admin approval required)
- **Admin** – Platform control, approvals, monitoring

---

## ✨ Features

### 👤 Customer Features
- Registration & login
- Product browsing with search & filters
- Cart & wishlist
- Razorpay checkout
- Order history & tracking
- Reviews & ratings
- Address management

### 🧑‍💼 Seller Features
- Seller onboarding (admin approval)
- Add / edit / delete products
- Upload multiple product images
- Inventory management
- Order status updates

### 🛡️ Admin Features
- User block / unblock
- Seller approval / rejection
- Product approval workflow
- Category & sub-category management
- Global order visibility
- Revenue & usage dashboard

---

## 📂 Project Structure

Ecommerce/
├── src/main/java/com/ecommerce/
│ ├── config/
│ ├── controllers/
│ ├── dto/
│ ├── entities/
│ ├── exceptions/
│ ├── repositories/
│ ├── security/
│ ├── services/
│ └── utils/
├── frontend/
│ ├── src/
│ │ ├── api/
│ │ ├── components/
│ │ ├── context/
│ │ ├── pages/
│ │ └── Router.jsx
│ └── public/
├── uploads/
└── README.md

markdown
Copy code

---

## 📘 Feature & Process Documentation

### 🔐 Authentication & Email
- [Gmail App Password Setup](./GMAIL_APP_PASSWORD_SETUP.md)
- [Order Email Notifications](./ORDER_EMAIL_NOTIFICATIONS.md)

### 🛍️ Product Approval & Visibility
- [Product Approval Guide](./PRODUCT_APPROVAL_GUIDE.md)
- [Product Approval Process](./PRODUCT_APPROVAL_PROCESS.md)
- [How to Approve Products](./HOW_TO_APPROVE_PRODUCTS.md)
- [Product Approval Fix](./PRODUCT_APPROVAL_FIX.md)
- [Product Visibility Fix](./PRODUCT_VISIBILITY_FIX.md)
- [Quick Fix – Products Not Showing](./QUICK_FIX_PRODUCTS.md)

### 💳 Payments
- [Payment Options Guide](./PAYMENT_OPTIONS_GUIDE.md)
- [Fix Payment Null Issue](./FIX_PAYMENT_NULL_ISSUE.md)

### 🧪 Testing & Validation
- [Operation Test Guide](./OPERATION_TEST_GUIDE.md)

---

## ⚙️ Setup Instructions

### Prerequisites
- Java 17+
- Maven 3.6+
- MySQL 8+
- Node.js 16+
- Razorpay account

---

### 🔧 Backend Setup

1. **Navigate to project**
   ```bash
   cd Ecommerce
Create MySQL database

sql
Copy code
CREATE DATABASE ecommerce_db;
Update application.properties

properties
Copy code
spring.datasource.username=your_username
spring.datasource.password=your_password
Configure Razorpay

properties
Copy code
razorpay.key.id=your_key
razorpay.key.secret=your_secret
JWT Configuration

properties
Copy code
jwt.secret=YourStrong256BitSecretKey
Run Backend

bash
Copy code
mvn clean install
mvn spring-boot:run
Backend runs on:
👉 http://localhost:8080

🎨 Frontend Setup
bash
Copy code
cd frontend
npm install
npm start
Frontend runs on:
👉 http://localhost:3000

🔗 API Overview
Authentication
POST /api/auth/signup

POST /api/auth/signin

GET /api/auth/me

Products
GET /api/products/public

POST /api/products

PUT /api/products/{id}

DELETE /api/products/{id}

Cart
GET /api/cart

POST /api/cart/add

DELETE /api/cart/clear

Orders
POST /api/orders

GET /api/orders

Payments
POST /api/payment/create-order

POST /api/payment/verify

Admin
GET /api/admin/dashboard

PUT /api/admin/products/{id}/approve

🔐 Security Highlights
JWT-based authentication

BCrypt password encryption

Role-based access control

CORS configured for frontend

Secure payment verification

🧪 Testing
bash
Copy code
mvn test
cd frontend && npm test
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
Deploy build folder using Nginx / Apache.

🧠 Why This Project Matters
Real-world admin approval workflows

Clean REST API design

Strong Spring Security + JWT

Production-ready architecture

Suitable for startup & product company interviews

👨‍💻 Author
Vamshi Prasad Goteti
Full Stack Java Developer
(Spring Boot | React | JPA | SQL)
