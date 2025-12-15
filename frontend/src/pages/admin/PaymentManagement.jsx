import React, { useState, useEffect } from 'react';
import * as paymentAPI from '../../api/payment';
import './PaymentManagement.css';

const PaymentManagement = () => {
  const [payments, setPayments] = useState([]);
  const [analytics, setAnalytics] = useState(null);
  const [loading, setLoading] = useState(true);
  const [selectedPayment, setSelectedPayment] = useState(null);
  const [refundAmount, setRefundAmount] = useState('');
  const [refundReason, setRefundReason] = useState('');

  useEffect(() => {
    fetchPayments();
    fetchAnalytics();
  }, []);

  const fetchPayments = async () => {
    try {
      const response = await paymentAPI.getAllPayments(0, 50);
      setPayments(response.data.data.content || response.data.data);
      setLoading(false);
    } catch (error) {
      console.error('Error fetching payments:', error);
      setLoading(false);
    }
  };

  const fetchAnalytics = async () => {
    try {
      const response = await paymentAPI.getPaymentAnalytics();
      setAnalytics(response.data.data);
    } catch (error) {
      console.error('Error fetching analytics:', error);
    }
  };

  const handleRefund = async (paymentId, fullRefund = false) => {
    if (!fullRefund && (!refundAmount || parseFloat(refundAmount) <= 0)) {
      alert('Please enter a valid refund amount');
      return;
    }

    try {
      const refundRequest = {
        paymentId: paymentId,
        amount: fullRefund ? null : parseFloat(refundAmount),
        reason: refundReason || 'Customer request',
        notes: `Refund processed by admin`
      };

      await paymentAPI.processRefund(refundRequest);
      alert('Refund processed successfully!');
      setSelectedPayment(null);
      setRefundAmount('');
      setRefundReason('');
      fetchPayments();
      fetchAnalytics();
    } catch (error) {
      console.error('Error processing refund:', error);
      alert('Error processing refund: ' + (error.response?.data?.message || error.message));
    }
  };

  const handleRetryPayment = async (paymentId) => {
    try {
      await paymentAPI.retryPayment(paymentId);
      alert('Payment retry initiated successfully!');
      fetchPayments();
    } catch (error) {
      console.error('Error retrying payment:', error);
      alert('Error retrying payment: ' + (error.response?.data?.message || error.message));
    }
  };

  if (loading) {
    return <div className="loading">Loading payments...</div>;
  }

  return (
    <div className="payment-management">
      <div className="container">
        <h2>Payment Management</h2>

        {/* Analytics Dashboard */}
        {analytics && (
          <div className="analytics-dashboard">
            <div className="analytics-card">
              <h3>Total Revenue</h3>
              <p className="amount">₹{analytics.totalRevenue?.toLocaleString('en-IN') || '0'}</p>
            </div>
            <div className="analytics-card">
              <h3>Today's Revenue</h3>
              <p className="amount">₹{analytics.todayRevenue?.toLocaleString('en-IN') || '0'}</p>
            </div>
            <div className="analytics-card">
              <h3>This Month</h3>
              <p className="amount">₹{analytics.thisMonthRevenue?.toLocaleString('en-IN') || '0'}</p>
            </div>
            <div className="analytics-card">
              <h3>Total Transactions</h3>
              <p className="count">{analytics.totalTransactions || 0}</p>
              <p className="sub-text">
                Success: {analytics.successfulTransactions || 0} | 
                Failed: {analytics.failedTransactions || 0} | 
                Refunded: {analytics.refundedTransactions || 0}
              </p>
            </div>
          </div>
        )}

        {/* Payments Table */}
        <div className="payments-table-container">
          <h3>All Payments</h3>
          <table className="payments-table">
            <thead>
              <tr>
                <th>Payment ID</th>
                <th>Order Number</th>
                <th>Amount</th>
                <th>Status</th>
                <th>Method</th>
                <th>Date</th>
                <th>Actions</th>
              </tr>
            </thead>
            <tbody>
              {payments.map(payment => (
                <tr key={payment.id}>
                  <td>{payment.paymentId}</td>
                  <td>{payment.orderNumber}</td>
                  <td>₹{payment.amount?.toLocaleString('en-IN')}</td>
                  <td>
                    <span className={`status-badge status-${payment.status?.toLowerCase()}`}>
                      {payment.status}
                    </span>
                  </td>
                  <td>{payment.paymentMethod}</td>
                  <td>{new Date(payment.createdAt).toLocaleDateString()}</td>
                  <td>
                    <div className="action-buttons">
                      {payment.status === 'SUCCESS' && payment.isRefundable && (
                        <button 
                          onClick={() => setSelectedPayment(payment)}
                          className="btn-refund"
                        >
                          Refund
                        </button>
                      )}
                      {payment.status === 'FAILED' && (
                        <button 
                          onClick={() => handleRetryPayment(payment.id)}
                          className="btn-retry"
                        >
                          Retry
                        </button>
                      )}
                      <button 
                        onClick={() => window.location.href = `/orders/${payment.orderId}`}
                        className="btn-view"
                      >
                        View Order
                      </button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>

        {/* Refund Modal */}
        {selectedPayment && (
          <div className="modal-overlay" onClick={() => setSelectedPayment(null)}>
            <div className="modal-content" onClick={(e) => e.stopPropagation()}>
              <h3>Process Refund</h3>
              <div className="refund-info">
                <p><strong>Payment ID:</strong> {selectedPayment.paymentId}</p>
                <p><strong>Order Number:</strong> {selectedPayment.orderNumber}</p>
                <p><strong>Amount Paid:</strong> ₹{selectedPayment.amount?.toLocaleString('en-IN')}</p>
                {selectedPayment.refundedAmount > 0 && (
                  <p><strong>Already Refunded:</strong> ₹{selectedPayment.refundedAmount?.toLocaleString('en-IN')}</p>
                )}
              </div>
              <div className="refund-form">
                <button 
                  onClick={() => handleRefund(selectedPayment.id, true)}
                  className="btn-full-refund"
                >
                  Full Refund (₹{selectedPayment.amount?.toLocaleString('en-IN')})
                </button>
                <div className="divider">OR</div>
                <label>Partial Refund Amount:</label>
                <input
                  type="number"
                  value={refundAmount}
                  onChange={(e) => setRefundAmount(e.target.value)}
                  placeholder="Enter amount"
                  max={selectedPayment.amount}
                />
                <label>Reason:</label>
                <textarea
                  value={refundReason}
                  onChange={(e) => setRefundReason(e.target.value)}
                  placeholder="Refund reason..."
                  rows="3"
                />
                <button 
                  onClick={() => handleRefund(selectedPayment.id, false)}
                  className="btn-partial-refund"
                  disabled={!refundAmount || parseFloat(refundAmount) <= 0}
                >
                  Process Partial Refund
                </button>
              </div>
              <button onClick={() => setSelectedPayment(null)} className="btn-cancel">
                Cancel
              </button>
            </div>
          </div>
        )}
      </div>
    </div>
  );
};

export default PaymentManagement;



