# Fix Payment Null Constraint Issue

## Problem
The database column `razorpay_order_id` has a NOT NULL constraint, but COD payments don't have Razorpay order IDs.

## Solution
Instead of setting `null` for COD payments, we now set placeholder values:
- `razorpayOrderId`: `COD_ORDER_<timestamp>`
- `razorpayPaymentId`: `COD_PAYMENT_<timestamp>`
- `razorpaySignature`: `""` (empty string)

## Alternative: Update Database Schema

If you want to make the columns truly nullable, you can run this SQL:

```sql
ALTER TABLE payments MODIFY COLUMN razorpay_order_id VARCHAR(255) NULL;
ALTER TABLE payments MODIFY COLUMN razorpay_payment_id VARCHAR(255) NULL;
ALTER TABLE payments MODIFY COLUMN razorpay_signature VARCHAR(255) NULL;
```

Or drop and recreate the database (Hibernate will recreate with correct schema):
1. Drop database: `DROP DATABASE ecommerce_db;`
2. Restart application (Hibernate will create it with correct schema)

## Current Fix
The code now sets placeholder values for COD payments, so the NOT NULL constraint is satisfied while still indicating it's a COD payment.



