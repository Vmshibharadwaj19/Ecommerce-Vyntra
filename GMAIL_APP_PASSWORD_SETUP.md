# Gmail App Password Setup Guide


## Why App Password?

Gmail no longer accepts regular passwords for SMTP. You **MUST** use an **App Password**.

## Steps to Generate Gmail App Password:

### Step 1: Enable 2-Step Verification (if not already enabled)
1. Go to: https://myaccount.google.com/security
2. Under "Signing in to Google", click **2-Step Verification**
3. Follow the prompts to enable it (if not already enabled)

### Step 2: Generate App Password
1. Go to: https://myaccount.google.com/apppasswords
2. Select **Mail** as the app
3. Select **Other (Custom name)** as the device
4. Enter name: "VYNTRA E-Commerce"
5. Click **Generate**
6. **Copy the 16-character password** (it will look like: `abcd efgh ijkl mnop`)

### Step 3: Update application.properties
1. Open `src/main/resources/application.properties`
2. Find: `spring.mail.password=V@mshisunny19`
3. Replace with your App Password (remove spaces):
   ```properties
   spring.mail.password=abcdefghijklmnop
   ```

### Step 4: Restart Application
Restart your Spring Boot application for the changes to take effect.

## Important Notes:
- ⚠️ **Never share your App Password**
- ⚠️ **App Passwords are 16 characters** (no spaces when entering)
- ⚠️ **Regular Gmail password will NOT work** - you must use App Password
- ✅ App Passwords are safer than regular passwords for applications

## Troubleshooting:

If you still get authentication errors:
1. Verify 2-Step Verification is enabled
2. Make sure you copied the App Password correctly (no spaces)
3. Try generating a new App Password
4. Check that `spring.mail.username` matches your Gmail address exactly

