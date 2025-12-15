# Razorpay Payment Integration Setup Guide

## 🔑 Getting Razorpay API Keys

### Step 1: Create Razorpay Account

1. **Go to Razorpay Dashboard:**
   - Visit: https://dashboard.razorpay.com/
   - Sign up for a free account (or login if you have one)

2. **Complete Account Setup:**
   - Fill in business details
   - Verify your email
   - Complete KYC (for production, optional for test mode)

---

### Step 2: Get API Keys

1. **Login to Razorpay Dashboard:**
   - Go to: https://dashboard.razorpay.com/app/keys

2. **Generate Test Keys:**
   - Click "Generate Test Keys" (for development)
   - Or use "Live Keys" (for production)

3. **Copy Your Keys:**
   - **Key ID** (starts with `rzp_test_` for test mode)
   - **Key Secret** (keep this secure!)

---

### Step 3: Configure in Application

1. **Open `application.properties`:**
   ```
   src/main/resources/application.properties
   ```

2. **Update Razorpay Configuration:**
   ```properties
   # Razorpay Configuration
   razorpay.key.id=rzp_test_YOUR_KEY_ID_HERE
   razorpay.key.secret=YOUR_KEY_SECRET_HERE
   ```

3. **Replace with Your Keys:**
   - Replace `rzp_test_YOUR_KEY_ID_HERE` with your actual Key ID
   - Replace `YOUR_KEY_SECRET_HERE` with your actual Key Secret

4. **Example:**
   ```properties
   razorpay.key.id=rzp_test_1234567890abcdef
   razorpay.key.secret=abcdef1234567890abcdef1234567890
   ```

---

## 🧪 Test Mode vs Live Mode

### Test Mode (Development):
- **Key ID Format:** `rzp_test_xxxxxxxxxxxxx`
- **Use for:** Development and testing
- **No real money:** Transactions are simulated
- **Free:** No charges

### Live Mode (Production):
- **Key ID Format:** `rzp_live_xxxxxxxxxxxxx`
- **Use for:** Production environment
- **Real money:** Actual transactions
- **Requires:** Business verification and KYC

**For Development:** Use **Test Mode** keys!

---

## 📝 Complete Setup Steps

### 1. Get Test Keys from Razorpay:
```
1. Login to https://dashboard.razorpay.com/
2. Go to Settings → API Keys
3. Generate Test Keys
4. Copy Key ID and Key Secret
```

### 2. Update application.properties:
```properties
razorpay.key.id=rzp_test_YOUR_ACTUAL_KEY_ID
razorpay.key.secret=YOUR_ACTUAL_KEY_SECRET
```

### 3. Restart Application:
```bash
# Stop the application (Ctrl+C)
# Then restart:
mvn spring-boot:run
```

### 4. Test Payment:
- Go to checkout page
- Create order
- Payment should work now!

---

## 🔒 Security Notes

1. **Never Commit Keys to Git:**
   - Add `application.properties` to `.gitignore` (if it contains secrets)
   - Or use environment variables

2. **Use Environment Variables (Recommended):**
   ```properties
   razorpay.key.id=${RAZORPAY_KEY_ID:rzp_test_default}
   razorpay.key.secret=${RAZORPAY_KEY_SECRET:default_secret}
   ```
   
   Then set environment variables:
   ```bash
   export RAZORPAY_KEY_ID=rzp_test_your_key
   export RAZORPAY_KEY_SECRET=your_secret
   ```

3. **Different Keys for Different Environments:**
   - Test keys for development
   - Live keys for production
   - Never mix them!

---

## 🐛 Troubleshooting

### Error: "Authentication failed"

**Causes:**
1. ❌ Keys not set in `application.properties`
2. ❌ Keys are incorrect/invalid
3. ❌ Using test keys in live mode (or vice versa)
4. ❌ Keys have extra spaces or quotes

**Solutions:**
1. ✅ Check `application.properties` has correct keys
2. ✅ Verify keys in Razorpay dashboard
3. ✅ Ensure no extra spaces or quotes
4. ✅ Restart application after updating keys

### Error: "Razorpay API keys are not configured"

**Solution:**
- Update `application.properties` with your Razorpay keys
- See Step 2 above

---

## 📋 Quick Reference

### Test Keys Format:
```
Key ID: rzp_test_xxxxxxxxxxxxx
Key Secret: xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
```

### Configuration File:
```
src/main/resources/application.properties
```

### Properties to Set:
```properties
razorpay.key.id=rzp_test_YOUR_KEY_ID
razorpay.key.secret=YOUR_KEY_SECRET
```

---

## 🎯 Test Payment Flow

1. **Customer adds products to cart**
2. **Goes to checkout**
3. **Clicks "Pay with Razorpay"**
4. **Backend creates Razorpay order** (uses your keys)
5. **Razorpay checkout opens**
6. **Customer pays** (test mode: use test card)
7. **Payment verified**
8. **Order created**

---

## 💳 Test Cards (Test Mode Only)

Use these cards in Razorpay test mode:

**Success Card:**
- Card Number: `4111 1111 1111 1111`
- CVV: Any 3 digits (e.g., `123`)
- Expiry: Any future date (e.g., `12/25`)
- Name: Any name

**Failure Card:**
- Card Number: `4000 0000 0000 0002`
- (Use to test payment failures)

---

## ✅ Verification Checklist

- [ ] Razorpay account created
- [ ] Test keys generated
- [ ] Keys copied to `application.properties`
- [ ] Application restarted
- [ ] Payment endpoint tested
- [ ] Test payment successful

---

## 🚀 After Setup

Once keys are configured:
1. ✅ Payment orders will be created successfully
2. ✅ Razorpay checkout will work
3. ✅ Payments can be verified
4. ✅ Orders will be created after payment

**Your payment integration is ready!** 🎉



