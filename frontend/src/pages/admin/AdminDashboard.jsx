import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import * as adminAPI from '../../api/admin';
import './AdminDashboard.css';

const AdminDashboard = () => {
  const [stats, setStats] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    fetchDashboardStats();
  }, []);

  const fetchDashboardStats = async () => {
    try {
      const response = await adminAPI.getDashboardStats();
      setStats(response.data.data);
      setLoading(false);
    } catch (error) {
      console.error('Error fetching dashboard stats:', error);
      setLoading(false);
    }
  };

  if (loading) {
    return <div className="admin-dashboard-loading">Loading dashboard...</div>;
  }

  return (
    <div className="admin-dashboard">
      <div className="dashboard-container">
        <h2>Admin Dashboard</h2>
        
        {stats && (
          <div className="dashboard-stats">
            <div className="stat-card revenue">
              <h3>Total Revenue</h3>
              <p className="stat-value">₹{stats.totalRevenue?.toFixed(2) || '0.00'}</p>
            </div>
            <div className="stat-card">
              <h3>Total Orders</h3>
              <p className="stat-value">{stats.totalOrders || 0}</p>
            </div>
            <div className="stat-card">
              <h3>Total Customers</h3>
              <p className="stat-value">{stats.totalCustomers || 0}</p>
            </div>
            <div className="stat-card">
              <h3>Total Sellers</h3>
              <p className="stat-value">{stats.totalSellers || 0}</p>
            </div>
            <div className="stat-card">
              <h3>Total Products</h3>
              <p className="stat-value">{stats.totalProducts || 0}</p>
            </div>
            <div className="stat-card warning">
              <h3>Pending Sellers</h3>
              <p className="stat-value">{stats.pendingSellers || 0}</p>
            </div>
            <div className="stat-card warning">
              <h3>Pending Products</h3>
              <p className="stat-value">{stats.pendingProducts || 0}</p>
            </div>
          </div>
        )}

        <div className="dashboard-links">
          <Link to="/admin/users" className="dashboard-card">
            <div className="card-icon">👥</div>
            <h3>Manage Users</h3>
            <p>View and manage all users</p>
          </Link>
          <Link to="/admin/sellers" className="dashboard-card">
            <div className="card-icon">🏪</div>
            <h3>Manage Sellers</h3>
            <p>Approve/reject sellers</p>
            {stats?.pendingSellers > 0 && (
              <span className="badge">{stats.pendingSellers}</span>
            )}
          </Link>
          <Link to="/admin/products" className="dashboard-card">
            <div className="card-icon">📦</div>
            <h3>Manage Products</h3>
            <p>Approve/reject products</p>
            {stats?.pendingProducts > 0 && (
              <span className="badge">{stats.pendingProducts}</span>
            )}
          </Link>
          <Link to="/admin/categories" className="dashboard-card">
            <div className="card-icon">📂</div>
            <h3>Manage Categories</h3>
            <p>Manage categories and subcategories</p>
          </Link>
          <Link to="/admin/orders" className="dashboard-card">
            <div className="card-icon">📋</div>
            <h3>All Orders</h3>
            <p>View all system orders</p>
          </Link>
          <Link to="/admin/payments" className="dashboard-card">
            <div className="card-icon">💳</div>
            <h3>Payment Management</h3>
            <p>Manage payments, refunds, and analytics</p>
          </Link>
        </div>
      </div>
    </div>
  );
};

export default AdminDashboard;
