# Create Admin Account - Quick Reference

## 🚀 API Endpoint
```
POST http://localhost:8080/api/auth/signup
```

## 📋 JSON Body
```json
{
  "email": "admin@ecommerce.com",
  "password": "admin123",
  "firstName": "Admin",
  "lastName": "User",
  "role": "ROLE_ADMIN"
}
```

## ✅ Success Response
```json
{
  "success": true,
  "message": "User registered successfully",
  "data": {
    "id": 1,
    "email": "admin@ecommerce.com",
    "role": "ROLE_ADMIN",
    "isApproved": true
  }
}
```

## 🔧 Postman Setup
- **Method:** POST
- **URL:** `http://localhost:8080/api/auth/signup`
- **Headers:** `Content-Type: application/json`
- **Body:** Copy JSON above

## ⚠️ Notes
- Email must be unique
- Password minimum 6 characters
- Admin is auto-approved
- Default admin exists: `admin@ecommerce.com` / `admin123`



