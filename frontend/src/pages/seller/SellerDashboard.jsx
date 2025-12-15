import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import { useAuth } from '../../context/AuthContext';
import './SellerDashboard.css';

const SellerDashboard = () => {
  const { user, refreshUser } = useAuth();
  const [stats, setStats] = useState(null);
  const [loading, setLoading] = useState(true);
  const [currentUser, setCurrentUser] = useState(user);

  useEffect(() => {
    // Refresh user data to get latest approval status
    const fetchUserData = async () => {
      const freshUser = await refreshUser();
      if (freshUser) {
        setCurrentUser(freshUser);
      }
      setLoading(false);
    };
    fetchUserData();
    
    // Also refresh every 30 seconds to check for approval
    const interval = setInterval(fetchUserData, 30000);
    return () => clearInterval(interval);
  }, [refreshUser]);

  if (loading) {
    return <div className="seller-dashboard-loading">Loading dashboard...</div>;
  }

  const isApproved = currentUser?.isApproved === true;

  return (
    <div className="seller-dashboard">
      <div className="dashboard-container">
        <h2>Seller Dashboard</h2>
        
        {!isApproved && (
          <div className="approval-notice">
            <div className="notice-icon">⏳</div>
            <div className="notice-content">
              <h3>Account Pending Approval</h3>
              <p>Your seller account is currently pending admin approval.</p>
              <p>You will receive an email notification once your account is approved.</p>
              <p><strong>Once approved, you can:</strong></p>
              <ul>
                <li>Add products to VYNTRA</li>
                <li>Manage your product inventory</li>
                <li>View and fulfill orders</li>
              </ul>
              <p className="notice-note">
                <strong>Note:</strong> Even after approval, each product you add will require 
                admin approval before appearing on the customer dashboard.
              </p>
            </div>
          </div>
        )}

        {isApproved && (
          <div className="approval-success">
            <div className="success-icon">✅</div>
            <div className="success-content">
              <h3>Account Approved!</h3>
              <p>Your seller account has been approved. You can now add products and start selling on VYNTRA.</p>
              <p className="success-note">
                <strong>Remember:</strong> Each product you add will require admin approval 
                before it appears on the customer dashboard.
              </p>
            </div>
          </div>
        )}

        <div className="dashboard-links">
          <Link to="/seller/products" className="dashboard-card">
            <div className="card-icon">📦</div>
            <h3>My Products</h3>
            <p>Manage your products</p>
            {!isApproved && <span className="disabled-badge">Requires Approval</span>}
          </Link>
          <Link 
            to="/seller/products/add" 
            className={`dashboard-card ${!isApproved ? 'disabled' : ''}`}
            onClick={(e) => {
              if (!isApproved) {
                e.preventDefault();
                alert('Please wait for admin approval before adding products.');
              }
            }}
          >
            <div className="card-icon">➕</div>
            <h3>Add Product</h3>
            <p>Add a new product</p>
            {!isApproved && <span className="disabled-badge">Requires Approval</span>}
          </Link>
          <Link to="/seller/orders" className="dashboard-card">
            <div className="card-icon">📋</div>
            <h3>Orders</h3>
            <p>View and manage orders</p>
            {!isApproved && <span className="disabled-badge">Requires Approval</span>}
          </Link>
        </div>
      </div>
    </div>
  );
};

export default SellerDashboard;
