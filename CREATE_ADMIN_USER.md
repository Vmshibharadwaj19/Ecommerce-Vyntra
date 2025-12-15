# Create Admin User - API & JSON Guide

## 🎯 API Endpoint

```
POST http://localhost:8080/api/auth/signup
```

---

## 📋 JSON Request Body

### Minimal Required Fields:
```json
{
  "email": "admin@example.com",
  "password": "admin123",
  "firstName": "Admin",
  "lastName": "User",
  "role": "ROLE_ADMIN"
}
```

### Complete Example (All Fields):
```json
{
  "email": "admin@example.com",
  "password": "admin123",
  "firstName": "Admin",
  "lastName": "User",
  "phoneNumber": "1234567890",
  "role": "ROLE_ADMIN"
}
```

---

## 🔧 Using cURL

```bash
curl -X POST http://localhost:8080/api/auth/signup \
  -H "Content-Type: application/json" \
  -d '{
    "email": "admin@example.com",
    "password": "admin123",
    "firstName": "Admin",
    "lastName": "User",
    "phoneNumber": "1234567890",
    "role": "ROLE_ADMIN"
  }'
```

---

## 📝 Using Postman/Thunder Client

### Request Details:
- **Method:** `POST`
- **URL:** `http://localhost:8080/api/auth/signup`
- **Headers:**
  ```
  Content-Type: application/json
  ```
- **Body (raw JSON):**
  ```json
  {
    "email": "admin@example.com",
    "password": "admin123",
    "firstName": "Admin",
    "lastName": "User",
    "phoneNumber": "1234567890",
    "role": "ROLE_ADMIN"
  }
  ```

---

## ✅ Expected Response

### Success Response (200 OK):
```json
{
  "success": true,
  "message": "User registered successfully",
  "data": {
    "id": 1,
    "email": "admin@example.com",
    "firstName": "Admin",
    "lastName": "User",
    "phoneNumber": "1234567890",
    "role": "ROLE_ADMIN",
    "isActive": true,
    "isBlocked": false,
    "isApproved": true
  }
}
```

### Error Response (400 Bad Request):
```json
{
  "success": false,
  "message": "Email is already in use!",
  "data": null
}
```

---

## 📊 Field Details

| Field | Type | Required | Description |
|-------|------|----------|-------------|
| `email` | String | ✅ Yes | Valid email address |
| `password` | String | ✅ Yes | Minimum 6 characters |
| `firstName` | String | ✅ Yes | User's first name |
| `lastName` | String | ✅ Yes | User's last name |
| `phoneNumber` | String | ❌ No | User's phone number |
| `role` | String | ❌ No | User role: `ROLE_ADMIN`, `ROLE_SELLER`, or `ROLE_CUSTOMER` |

---

## 🎯 Role Values

Valid role values:
- `ROLE_ADMIN` - Administrator (auto-approved)
- `ROLE_SELLER` - Seller (requires approval)
- `ROLE_CUSTOMER` - Customer (default, no approval needed)

**Note:** If `role` is not provided, defaults to `ROLE_CUSTOMER`.

---

## 🔐 Admin User Properties

When you create a user with `ROLE_ADMIN`:
- ✅ `isActive` = `true` (automatically set)
- ✅ `isApproved` = `true` (automatically set for admins)
- ✅ `isBlocked` = `false` (automatically set)
- ✅ Full system access

---

## 🧪 Test Examples

### Example 1: Create Admin User
```json
{
  "email": "newadmin@ecommerce.com",
  "password": "admin123",
  "firstName": "New",
  "lastName": "Admin",
  "role": "ROLE_ADMIN"
}
```

### Example 2: Create Admin with Phone
```json
{
  "email": "admin2@ecommerce.com",
  "password": "admin123",
  "firstName": "Super",
  "lastName": "Admin",
  "phoneNumber": "9876543210",
  "role": "ROLE_ADMIN"
}
```

---

## 🚀 Quick Test

### Using JavaScript (Browser Console):
```javascript
fetch('http://localhost:8080/api/auth/signup', {
  method: 'POST',
  headers: {
    'Content-Type': 'application/json'
  },
  body: JSON.stringify({
    email: 'admin@example.com',
    password: 'admin123',
    firstName: 'Admin',
    lastName: 'User',
    role: 'ROLE_ADMIN'
  })
})
.then(response => response.json())
.then(data => console.log(data))
.catch(error => console.error('Error:', error));
```

### Using PowerShell:
```powershell
$body = @{
    email = "admin@example.com"
    password = "admin123"
    firstName = "Admin"
    lastName = "User"
    role = "ROLE_ADMIN"
} | ConvertTo-Json

Invoke-RestMethod -Uri "http://localhost:8080/api/auth/signup" -Method Post -Body $body -ContentType "application/json"
```

---

## ⚠️ Important Notes

1. **Email Must Be Unique:**
   - Cannot use an email that already exists
   - Error: "Email is already in use!"

2. **Password Requirements:**
   - Minimum 6 characters
   - Error if less than 6 characters

3. **Admin Auto-Approved:**
   - Admin users are automatically approved
   - No need for manual approval
   - Can access admin dashboard immediately

4. **Existing Admin:**
   - Default admin already exists: `admin@ecommerce.com` / `admin123`
   - You can create additional admin users using this API

---

## 🔍 Verify Admin Creation

After creating admin, login to verify:

### Login API:
```
POST http://localhost:8080/api/auth/signin
```

### Login JSON:
```json
{
  "email": "admin@example.com",
  "password": "admin123"
}
```

### Expected Response:
```json
{
  "success": true,
  "message": "Login successful",
  "data": {
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "type": "Bearer",
    "id": 1,
    "email": "admin@example.com",
    "firstName": "Admin",
    "lastName": "User",
    "role": "ROLE_ADMIN"
  }
}
```

---

## 📝 Complete Example Workflow

### Step 1: Create Admin User
```bash
POST http://localhost:8080/api/auth/signup
Content-Type: application/json

{
  "email": "myadmin@ecommerce.com",
  "password": "securepassword123",
  "firstName": "My",
  "lastName": "Admin",
  "phoneNumber": "1234567890",
  "role": "ROLE_ADMIN"
}
```

### Step 2: Login with Admin
```bash
POST http://localhost:8080/api/auth/signin
Content-Type: application/json

{
  "email": "myadmin@ecommerce.com",
  "password": "securepassword123"
}
```

### Step 3: Use Admin Token
```bash
GET http://localhost:8080/api/admin/dashboard
Authorization: Bearer {token_from_step2}
```

---

## ✅ Summary

**API Endpoint:**
```
POST http://localhost:8080/api/auth/signup
```

**Minimal JSON:**
```json
{
  "email": "admin@example.com",
  "password": "admin123",
  "firstName": "Admin",
  "lastName": "User",
  "role": "ROLE_ADMIN"
}
```

**Result:**
- ✅ Admin user created
- ✅ Automatically approved
- ✅ Can access admin dashboard
- ✅ Full system permissions

---

## 🎉 Ready to Use!

Copy the JSON above and use it with Postman, cURL, or any HTTP client to create your admin user! 🚀



