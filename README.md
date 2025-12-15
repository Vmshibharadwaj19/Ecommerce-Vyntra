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

