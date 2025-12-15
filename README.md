# Amazon-Style E-Commerce Application

A complete full-stack e-commerce application built with Spring Boot (Backend) and React (Frontend), featuring multiple user roles, Razorpay payment integration, and comprehensive product management.

## Tech Stack

### Backend
- **Spring Boot 3.2.0** (Java 17)
- **Spring Data JPA** - Database operations
- **Spring Security** - JWT Authentication & Authorization
- **MySQL** - Database
- **Razorpay** - Payment gateway
- **Lombok** - Boilerplate code reduction
- **ModelMapper** - DTO mapping
- **Hibernate** - ORM

### Frontend
- **React 18.2.0**
- **React Router 6.20.0** - Routing
- **Axios** - HTTP client
- **Context API** - State management
- **CSS Modules** - Styling

## Features

### Customer Features
- User registration and login
- Browse products with search and filters
- Add products to cart
- Wishlist functionality
- Checkout with Razorpay payment
- Order history and tracking
- Product reviews and ratings
- Address management

### Seller Features
- Seller registration (requires admin approval)
- Add/Edit/Delete products
- Upload multiple product images
- Manage inventory
- View and manage orders
- Order status updates (Shipped/Delivered)

### Admin Features
- User management (Block/Unblock)
- Seller approval/rejection
- Product approval/rejection
- Category and subcategory management
- View all orders system-wide
- Dashboard with statistics (Revenue, Orders, Users, Products)

## Project Structure

```
Ecommerce/
├── src/main/java/com/ecommerce/
│   ├── config/          # Configuration classes
│   ├── controllers/     # REST controllers
│   ├── dto/             # Data Transfer Objects
│   ├── entities/        # JPA entities
│   ├── exceptions/      # Exception handlers
│   ├── repositories/    # Data repositories
│   ├── security/        # Security configuration
│   ├── services/        # Business logic
│   └── utils/           # Utility classes
├── frontend/
│   ├── src/
│   │   ├── api/         # API service files
│   │   ├── components/  # Reusable components
│   │   ├── context/     # Context providers
│   │   ├── pages/       # Page components
│   │   └── Router.jsx   # Routing configuration
│   └── public/
└── README.md
```

## Setup Instructions

### Prerequisites
- Java 17 or higher
- Maven 3.6+
- MySQL 8.0+
- Node.js 16+ and npm
- Razorpay account (for payment integration)

### Backend Setup

1. **Clone the repository**
   ```bash
   cd Ecommerce
   ```

2. **Configure MySQL Database**
   - Create a MySQL database named `ecommerce_db`
   - Update `src/main/resources/application.properties` with your database credentials:
     ```properties
     spring.datasource.username=your_username
     spring.datasource.password=your_password
     ```

3. **Configure Razorpay**
   - Get your Razorpay Key ID and Key Secret from Razorpay Dashboard
   - Update `src/main/resources/application.properties`:
     ```properties
     razorpay.key.id=your_razorpay_key_id
     razorpay.key.secret=your_razorpay_key_secret
     ```

4. **Configure JWT Secret**
   - Update the JWT secret in `application.properties`:
     ```properties
     jwt.secret=YourSecretKeyForJWTTokenGenerationThatShouldBeAtLeast256BitsLong
     ```

5. **Build and Run**
   ```bash
   mvn clean install
   mvn spring-boot:run
   ```
   The backend will start on `http://localhost:8080`

### Frontend Setup

1. **Navigate to frontend directory**
   ```bash
   cd frontend
   ```

2. **Install dependencies**
   ```bash
   npm install
   ```

3. **Start the development server**
   ```bash
   npm start
   ```
   The frontend will start on `http://localhost:3000`

## API Endpoints

### Authentication
- `POST /api/auth/signin` - User login
- `POST /api/auth/signup` - User registration
- `GET /api/auth/me` - Get current user

### Products
- `GET /api/products/public` - Get all products (public)
- `GET /api/products/public/{id}` - Get product by ID
- `POST /api/products/search` - Search products
- `POST /api/products` - Create product (Seller/Admin)
- `PUT /api/products/{id}` - Update product (Seller/Admin)
- `DELETE /api/products/{id}` - Delete product (Seller/Admin)

### Cart
- `GET /api/cart` - Get user cart
- `POST /api/cart/add` - Add item to cart
- `PUT /api/cart/items/{id}` - Update cart item
- `DELETE /api/cart/items/{id}` - Remove item from cart
- `DELETE /api/cart/clear` - Clear cart

### Orders
- `GET /api/orders` - Get user orders
- `GET /api/orders/{id}` - Get order details
- `POST /api/orders` - Create order

### Payment
- `POST /api/payment/create-order` - Create Razorpay order
- `POST /api/payment/verify` - Verify payment

### Admin
- `GET /api/admin/dashboard` - Get dashboard stats
- `GET /api/admin/users` - Get all users
- `PUT /api/admin/users/{id}/block` - Block user
- `PUT /api/admin/sellers/{id}/approve` - Approve seller
- `GET /api/admin/products/pending` - Get pending products
- `PUT /api/admin/products/{id}/approve` - Approve product

## Default Roles

The application supports three roles:
- **ROLE_CUSTOMER** - Regular customers
- **ROLE_SELLER** - Product sellers (requires admin approval)
- **ROLE_ADMIN** - System administrators

## Creating Admin User

To create an admin user, you can either:
1. Register with role `ROLE_ADMIN` (if allowed)
2. Manually update the database to set `role = 'ROLE_ADMIN'` and `is_approved = true`

## File Upload

Product images are stored in the `uploads/products/` directory. Make sure this directory exists or is created automatically.

## Security

- JWT tokens are used for authentication
- Passwords are encrypted using BCrypt
- Role-based access control (RBAC) is implemented
- CORS is configured for frontend origin

## Testing

### Backend Testing
```bash
mvn test
```

### Frontend Testing
```bash
cd frontend
npm test
```

## Production Deployment

### Backend
1. Build the JAR file:
   ```bash
   mvn clean package
   ```
2. Run the JAR:
   ```bash
   java -jar target/ecommerce-backend-1.0.0.jar
   ```

### Frontend
1. Build for production:
   ```bash
   cd frontend
   npm run build
   ```
2. Serve the `build` folder using a web server (nginx, Apache, etc.)

## Troubleshooting

### Common Issues

1. **Database Connection Error**
   - Verify MySQL is running
   - Check database credentials in `application.properties`
   - Ensure database `ecommerce_db` exists

2. **JWT Token Issues**
   - Verify JWT secret is set correctly
   - Check token expiration settings

3. **Razorpay Payment Issues**
   - Verify Razorpay credentials
   - Check Razorpay dashboard for test/live mode

4. **CORS Errors**
   - Ensure frontend URL is added to CORS configuration
   - Check `SecurityConfig.java` for allowed origins

## Contributing

1. Fork the repository
2. Create a feature branch
3. Commit your changes
4. Push to the branch
5. Create a Pull Request

## License

This project is licensed under the MIT License.

## Support

For issues and questions, please create an issue in the repository.

---

**Note**: This is a production-ready application template. Make sure to:
- Change default JWT secret in production
- Use environment variables for sensitive data
- Enable HTTPS in production
- Configure proper database backups
- Set up monitoring and logging



