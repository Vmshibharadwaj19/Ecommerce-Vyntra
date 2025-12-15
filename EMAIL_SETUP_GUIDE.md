# Email Setup Guide - VYNTRA

## 📧 Email Features Implemented

### 1. Seller Approval Notification
- Sends HTML email when admin approves seller
- Includes business details and dashboard link

### 2. Seller Rejection Notification
- Sends HTML email when admin rejects seller
- Professional rejection message

### 3. Email Configuration
- Supports Gmail SMTP (default)
- Can be configured for any SMTP server
- HTML email templates with styling
- Fallback to plain text if HTML fails

---

## 🔧 Setup Instructions

### Option 1: Gmail (Recommended for Testing)

1. **Enable App Password:**
   - Go to: https://myaccount.google.com/apppasswords
   - Sign in with your Gmail account
   - Select "Mail" and "Other (Custom name)"
   - Enter "VYNTRA" as the app name
   - Click "Generate"
   - Copy the 16-character password

2. **Update `application.properties`:**
   ```properties
   spring.mail.host=smtp.gmail.com
   spring.mail.port=587
   spring.mail.username=your-email@gmail.com
   spring.mail.password=your-16-char-app-password
   ```

3. **Update Email Settings:**
   ```properties
   mail.from.address=your-email@gmail.com
   mail.from.name=VYNTRA E-Commerce
   mail.enabled=true
   ```

### Option 2: Other SMTP Servers

#### Outlook/Hotmail:
```properties
spring.mail.host=smtp-mail.outlook.com
spring.mail.port=587
spring.mail.username=your-email@outlook.com
spring.mail.password=your-password
```

#### Yahoo:
```properties
spring.mail.host=smtp.mail.yahoo.com
spring.mail.port=587
spring.mail.username=your-email@yahoo.com
spring.mail.password=your-app-password
```

#### Custom SMTP:
```properties
spring.mail.host=smtp.yourdomain.com
spring.mail.port=587
spring.mail.username=your-email@yourdomain.com
spring.mail.password=your-password
```

---

## 🧪 Testing Email

### Test Seller Approval Email:
1. Login as admin
2. Go to "Manage Sellers"
3. Click "Approve" on any pending seller
4. Check seller's email inbox
5. Check backend logs for email status

### Test Seller Rejection Email:
1. Login as admin
2. Go to "Manage Sellers"
3. Click "Reject" on any pending seller
4. Check seller's email inbox

---

## 📋 Email Templates

### Seller Approval Email Includes:
- ✅ Professional HTML design
- ✅ Business details
- ✅ Dashboard link
- ✅ Important notes about product approval
- ✅ Branded styling

### Seller Rejection Email Includes:
- ✅ Professional HTML design
- ✅ Business details
- ✅ Support contact information
- ✅ Branded styling

---

## ⚙️ Configuration Options

### Disable Email (Log Only):
```properties
mail.enabled=false
```
When disabled, emails are logged but not sent.

### Enable Debug Mode:
```properties
spring.mail.properties.mail.debug=true
```
Shows detailed SMTP communication in logs.

---

## 🔍 Troubleshooting

### Email Not Sending?

1. **Check Gmail App Password:**
   - Make sure you're using App Password, not regular password
   - App Password is 16 characters, no spaces

2. **Check Firewall:**
   - Port 587 should be open
   - Some networks block SMTP

3. **Check Logs:**
   - Look for "✅ Email sent successfully" or "❌ Failed to send email"
   - Enable debug mode for detailed logs

4. **Test SMTP Connection:**
   - Try sending email from email client with same settings
   - If email client works, SMTP settings are correct

### Common Errors:

**"Authentication failed":**
- Wrong password or username
- Not using App Password for Gmail

**"Connection timeout":**
- Firewall blocking port 587
- Wrong SMTP host

**"Email sent but not received":**
- Check spam folder
- Verify recipient email address
- Check email server logs

---

## 🚀 Production Setup

For production, consider:

1. **Use Email Service Providers:**
   - SendGrid (recommended)
   - AWS SES
   - Mailgun
   - Postmark

2. **Update Configuration:**
   ```properties
   # SendGrid Example
   spring.mail.host=smtp.sendgrid.net
   spring.mail.port=587
   spring.mail.username=apikey
   spring.mail.password=your-sendgrid-api-key
   ```

3. **Add Email Queue:**
   - Use async email sending
   - Queue emails for reliability
   - Retry failed emails

---

## ✅ Current Status

- ✅ Email service implemented
- ✅ HTML email templates
- ✅ Seller approval emails
- ✅ Seller rejection emails
- ✅ Fallback to plain text
- ✅ Error handling and logging
- ✅ Configurable (can be disabled)

---

**Note:** For testing without real email, set `mail.enabled=false` and check logs for email content.

