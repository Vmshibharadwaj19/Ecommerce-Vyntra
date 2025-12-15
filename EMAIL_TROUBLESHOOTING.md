# Email Troubleshooting Guide

## 🔍 Common Issues and Solutions

### Issue 1: Email Not Being Received

**Check these in order:**

#### 1. **Email is Disabled**
```properties
# In application.properties
mail.enabled=false  ❌ Change to: mail.enabled=true
```

#### 2. **MailSender is NULL**
This means Spring couldn't create the JavaMailSender bean. Check:
- Is `spring-boot-starter-mail` in `pom.xml`? ✅
- Are email properties configured correctly?
- Check application logs for: `MailSender Available: false`

#### 3. **Invalid Email Credentials**
```properties
# These are PLACEHOLDERS - you must change them!
spring.mail.username=your-email@gmail.com  ❌
spring.mail.password=your-app-password    ❌
```

**Fix:**
1. Use your real Gmail address
2. Generate Gmail App Password: https://myaccount.google.com/apppasswords
3. Use the app password (not your regular password)

#### 4. **Wrong From Address**
```properties
# This should match your Gmail username
mail.from.address=noreply@vyntra.com  ❌ Should be: your-email@gmail.com
```

#### 5. **SMTP Authentication Failed**
- Check Gmail security settings
- Make sure "Less secure app access" is enabled (if using regular password)
- OR use App Password (recommended)

---

## 🧪 Testing Email Configuration

### Step 1: Check Application Logs

When you trigger an email, look for these log messages:

```
🔍 Email Debug - To: user@example.com, Enabled: true, MailSender: true
✅ HTML email sent successfully to: user@example.com
```

**If you see:**
- `MailSender: false` → Email configuration is wrong
- `Enabled: false` → Set `mail.enabled=true`
- `❌ Failed to send` → Check error details below

### Step 2: Check Error Messages

**Common errors:**

1. **"Authentication failed"**
   - Wrong password
   - Need to use App Password, not regular password
   - Gmail security blocking access

2. **"Could not connect to SMTP host"**
   - Wrong host/port
   - Firewall blocking port 587
   - Check: `spring.mail.host=smtp.gmail.com`
   - Check: `spring.mail.port=587`

3. **"MailSender is NULL"**
   - Email dependency not loaded
   - Rebuild project: `mvn clean install`
   - Check `pom.xml` has `spring-boot-starter-mail`

---

## ✅ Correct Email Configuration

```properties
# Enable email
mail.enabled=true

# Gmail SMTP (for testing)
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=your-actual-email@gmail.com
spring.mail.password=your-16-char-app-password

# SMTP Settings
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true
spring.mail.properties.mail.smtp.starttls.required=true

# From Address (should match username)
mail.from.address=your-actual-email@gmail.com
mail.from.name=VYNTRA E-Commerce
```

---

## 🔑 Getting Gmail App Password

1. Go to: https://myaccount.google.com/apppasswords
2. Sign in with your Gmail account
3. Select "Mail" as the app
4. Select "Other (Custom name)" as device
5. Enter "VYNTRA E-Commerce"
6. Click "Generate"
7. Copy the 16-character password
8. Use it in `spring.mail.password`

**Note:** If you don't see "App passwords" option:
- Enable 2-Step Verification first
- Then App passwords will appear

---

## 📧 Testing Without Real Email

If you want to test without sending real emails:

```properties
mail.enabled=false
```

Emails will be **logged** to console instead of sent. Check logs for:
```
📧 Email content (logged only):
Subject: Order Confirmed...
To: user@example.com
Content: <html>...
```

---

## 🚨 Quick Fix Checklist

- [ ] `mail.enabled=true` in application.properties
- [ ] `spring.mail.username` = your real Gmail
- [ ] `spring.mail.password` = Gmail App Password (16 chars)
- [ ] `mail.from.address` = same as username
- [ ] Rebuilt project after changes
- [ ] Restarted application
- [ ] Checked spam folder
- [ ] Verified recipient email is correct

---

## 📝 Debug Logs to Check

When email is triggered, you should see:

```
========================================
📧 ORDER CONFIRMATION EMAIL
========================================
To: customer@example.com
Email Enabled: true
MailSender Available: true
From Email: your-email@gmail.com
========================================
🔍 Email Debug - To: customer@example.com, Enabled: true, MailSender: true
✅ HTML email sent successfully to: customer@example.com
```

If you see errors, check the error message for details!

---

## 🔧 Still Not Working?

1. **Check application logs** - Look for error messages
2. **Verify email address** - Make sure user's email is correct
3. **Test with simple email** - Try sending a test email manually
4. **Check Gmail** - Make sure account is not locked/restricted
5. **Try different email provider** - Gmail might be blocking

---

## 💡 Alternative: Use Email Testing Service

For development, consider using:
- **Mailtrap** (free tier available)
- **MailHog** (local testing)
- **SendGrid** (free tier)

Just change the SMTP settings in `application.properties`!

