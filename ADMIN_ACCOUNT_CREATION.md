# Create Admin Account - Complete Guide

## 🎯 API Endpoint

```
POST http://localhost:8080/api/auth/signup
```

---

## 📋 JSON Request Body

### Complete Example:
```json
{
  "email": "admin@ecommerce.com",
  "password": "admin123",
  "firstName": "Admin",
  "lastName": "User",
  "phoneNumber": "1234567890",
  "role": "ROLE_ADMIN"
}
```

### Minimal (Required Fields Only):
```json
{
  "email": "admin@ecommerce.com",
  "password": "admin123",
  "firstName": "Admin",
  "lastName": "User",
  "role": "ROLE_ADMIN"
}
```

---

## 🔧 Using Postman/Thunder Client

### Request Setup:
1. **Method:** `POST`
2. **URL:** `http://localhost:8080/api/auth/signup`
3. **Headers:**
   ```
   Content-Type: application/json
   ```
4. **Body (raw JSON):**
   ```json
   {
     "email": "admin@ecommerce.com",
     "password": "admin123",
     "firstName": "Admin",
     "lastName": "User",
     "phoneNumber": "1234567890",
     "role": "ROLE_ADMIN"
   }
   ```

---

## 📝 Using cURL

```bash
curl -X POST http://localhost:8080/api/auth/signup \
  -H "Content-Type: application/json" \
  -d '{
    "email": "admin@ecommerce.com",
    "password": "admin123",
    "firstName": "Admin",
    "lastName": "User",
    "phoneNumber": "1234567890",
    "role": "ROLE_ADMIN"
  }'
```

---

## ✅ Expected Response

### Success (200 OK):
```json
{
  "success": true,
  "message": "User registered successfully",
  "data": {
    "id": 1,
    "email": "admin@ecommerce.com",
    "firstName": "Admin",
    "lastName": "User",
    "phoneNumber": "1234567890",
    "role": "ROLE_ADMIN",
    "isActive": true,
    "isApproved": true,
    "isBlocked": false
  }
}
```

### Error (400 Bad Request):
```json
{
  "success": false,
  "message": "Email is already in use!",
  "data": null
}
```

---

## 📊 Field Details

| Field | Required | Type | Description | Example |
|-------|----------|------|-------------|---------|
| `email` | ✅ Yes | String | Valid unique email | `"admin@ecommerce.com"` |
| `password` | ✅ Yes | String | Min 6 characters | `"admin123"` |
| `firstName` | ✅ Yes | String | First name | `"Admin"` |
| `lastName` | ✅ Yes | String | Last name | `"User"` |
| `phoneNumber` | ❌ No | String | Phone number | `"1234567890"` |
| `role` | ❌ No | String | `ROLE_ADMIN` | `"ROLE_ADMIN"` |

---

## 🎯 Role Values

Valid values for `role`:
- `ROLE_ADMIN` - Administrator (auto-approved)
- `ROLE_SELLER` - Seller (requires approval)
- `ROLE_CUSTOMER` - Customer (default)

**Note:** If `role` is not provided, defaults to `ROLE_CUSTOMER`.

---

## 🔐 Admin Account Properties

When you create a user with `ROLE_ADMIN`:
- ✅ `isActive` = `true` (automatically)
- ✅ `isApproved` = `true` (automatically - admins are auto-approved)
- ✅ `isBlocked` = `false` (automatically)
- ✅ Full access to admin dashboard
- ✅ Can approve/reject sellers and products
- ✅ Can manage all users

---

## 🧪 Test Examples

### Example 1: Create New Admin
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
  "email": "superadmin@ecommerce.com",
  "password": "SuperAdmin123",
  "firstName": "Super",
  "lastName": "Admin",
  "phoneNumber": "9876543210",
  "role": "ROLE_ADMIN"
}
```

---

## 🚀 Quick Test Scripts

### JavaScript (Browser Console):
```javascript
fetch('http://localhost:8080/api/auth/signup', {
  method: 'POST',
  headers: {
    'Content-Type': 'application/json'
  },
  body: JSON.stringify({
    email: 'admin@ecommerce.com',
    password: 'admin123',
    firstName: 'Admin',
    lastName: 'User',
    role: 'ROLE_ADMIN'
  })
})
.then(response => response.json())
.then(data => console.log('Success:', data))
.catch(error => console.error('Error:', error));
```

### PowerShell:
```powershell
$body = @{
    email = "admin@ecommerce.com"
    password = "admin123"
    firstName = "Admin"
    lastName = "User"
    role = "ROLE_ADMIN"
} | ConvertTo-Json

Invoke-RestMethod -Uri "http://localhost:8080/api/auth/signup" `
  -Method Post `
  -Body $body `
  -ContentType "application/json"
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
   - No manual approval needed
   - Can access admin dashboard immediately

4. **Default Admin Exists:**
   - Default admin: `admin@ecommerce.com` / `admin123`
   - You can create additional admins using this API

---

## 🔍 Verify Admin Creation

### Step 1: Login with New Admin
```
POST http://localhost:8080/api/auth/signin
```

**Request:**
```json
{
  "email": "admin@ecommerce.com",
  "password": "admin123"
}
```

**Response:**
```json
{
  "success": true,
  "message": "Login successful",
  "data": {
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "type": "Bearer",
    "id": 1,
    "email": "admin@ecommerce.com",
    "firstName": "Admin",
    "lastName": "User",
    "role": "ROLE_ADMIN"
  }
}
```

### Step 2: Access Admin Dashboard
```
GET http://localhost:8080/api/admin/dashboard
Headers: Authorization: Bearer {token}
```

---

## 📝 Complete Workflow

1. **Create Admin:**
   ```bash
   POST /api/auth/signup
   Body: { "email": "...", "password": "...", "firstName": "...", "lastName": "...", "role": "ROLE_ADMIN" }
   ```

2. **Login:**
   ```bash
   POST /api/auth/signin
   Body: { "email": "...", "password": "..." }
   Response: { "token": "..." }
   ```

3. **Use Admin Features:**
   ```bash
   GET /api/admin/dashboard
   Headers: Authorization: Bearer {token}
   ```

---

## ✅ Ready to Use!

Copy the JSON and use it with Postman, cURL, or any HTTP client to create your admin account! 🚀

**Default Admin (Already Exists):**
- Email: `admin@ecommerce.com`
- Password: `admin123`

**Create Additional Admins:**
- Use the API above with different email addresses



