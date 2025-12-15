import React, { useState, useEffect } from 'react';
import { useAuth } from '../context/AuthContext';
import * as paymentAPI from '../api/payment';
import './PaymentHistory.css';

const PaymentHistory = () => {
  const { isAuthenticated } = useAuth();
  const [payments, setPayments] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    if (isAuthenticated()) {
      fetchPaymentHistory();
    }
  }, []);

  const fetchPaymentHistory = async () => {
    try {
      const response = await paymentAPI.getPaymentHistory();
      setPayments(response.data.data);
      setLoading(false);
    } catch (error) {
      console.error('Error fetching payment history:', error);
      setLoading(false);
    }
  };

  if (loading) {
    return <div className="loading">Loading payment history...</div>;
  }

  return (
    <div className="payment-history">
      <div className="container">
        <h2>Payment History</h2>
        
        {payments.length === 0 ? (
          <div className="no-payments">
            <p>No payment history found.</p>
          </div>
        ) : (
          <div className="payments-list">
            {payments.map(payment => (
              <div key={payment.id} className="payment-card">
                <div className="payment-header">
                  <div>
                    <h3>Order #{payment.orderNumber}</h3>
                    <p className="payment-id">Payment ID: {payment.paymentId}</p>
                  </div>
                  <span className={`status-badge status-${payment.status?.toLowerCase()}`}>
                    {payment.status}
                  </span>
                </div>
                <div className="payment-details">
                  <div className="detail-item">
                    <span className="label">Amount:</span>
                    <span className="value">₹{payment.amount?.toLocaleString('en-IN')}</span>
                  </div>
                  <div className="detail-item">
                    <span className="label">Payment Method:</span>
                    <span className="value">{payment.paymentMethod}</span>
                  </div>
                  <div className="detail-item">
                    <span className="label">Date:</span>
                    <span className="value">{new Date(payment.createdAt).toLocaleString()}</span>
                  </div>
                  {payment.refundedAmount > 0 && (
                    <div className="detail-item refund-info">
                      <span className="label">Refunded:</span>
                      <span className="value refund-amount">
                        ₹{payment.refundedAmount?.toLocaleString('en-IN')}
                      </span>
                    </div>
                  )}
                </div>
                <div className="payment-actions">
                  <a href={`/orders/${payment.orderId}`} className="view-order-link">
                    View Order Details
                  </a>
                </div>
              </div>
            ))}
          </div>
        )}
      </div>
    </div>
  );
};

export default PaymentHistory;



