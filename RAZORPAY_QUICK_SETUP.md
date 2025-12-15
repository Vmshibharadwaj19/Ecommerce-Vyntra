# Razorpay Quick Setup - 5 Minutes

## ⚡ Quick Steps

### 1. Get Razorpay Test Keys (2 minutes)

1. **Go to:** https://dashboard.razorpay.com/
2. **Sign up/Login** (free account)
3. **Go to:** Settings → API Keys
4. **Click:** "Generate Test Keys"
5. **Copy:**
   - Key ID (starts with `rzp_test_`)
   - Key Secret

---

### 2. Update application.properties (1 minute)

**File:** `src/main/resources/application.properties`

**Find this section:**
```properties
# Razorpay Configuration
razorpay.key.id=your_razorpay_key_id
razorpay.key.secret=your_razorpay_key_secret
```

**Replace with your keys:**
```properties
# Razorpay Configuration
razorpay.key.id=rzp_test_YOUR_ACTUAL_KEY_ID_HERE
razorpay.key.secret=YOUR_ACTUAL_KEY_SECRET_HERE
```

**Example:**
```properties
razorpay.key.id=rzp_test_1234567890abcdef
razorpay.key.secret=abcdef1234567890abcdef1234567890
```

---

### 3. Restart Application (1 minute)

```bash
# Stop current application (Ctrl+C)
# Then restart:
mvn spring-boot:run
```

---

### 4. Test Payment (1 minute)

1. Login as customer
2. Add product to cart
3. Go to checkout
4. Click "Pay with Razorpay"
5. Should work now! ✅

---

## 🎯 What You Need

- ✅ Razorpay account (free)
- ✅ Test API keys
- ✅ Update `application.properties`
- ✅ Restart application

**Total Time: ~5 minutes**

---

## 📝 Example Keys Format

**Test Keys:**
```
Key ID: rzp_test_xxxxxxxxxxxxx
Key Secret: xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
```

**Where to Get:**
- Dashboard: https://dashboard.razorpay.com/app/keys
- Click "Generate Test Keys"

---

## ⚠️ Important

- ✅ Use **Test Keys** for development
- ✅ Keys start with `rzp_test_` for test mode
- ✅ No real money in test mode
- ✅ Free to use

---

## 🐛 If Still Getting Error

1. **Check keys are correct:**
   - No extra spaces
   - No quotes around values
   - Keys copied completely

2. **Verify in Razorpay Dashboard:**
   - Keys are active
   - Test keys (not live keys)

3. **Restart application:**
   - After updating properties
   - Changes only apply after restart

---

## ✅ Success!

Once configured, you'll see:
- ✅ Payment orders created successfully
- ✅ Razorpay checkout opens
- ✅ Test payments work
- ✅ Orders created after payment

**See `RAZORPAY_SETUP.md` for detailed instructions!**



