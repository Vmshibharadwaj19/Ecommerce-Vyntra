import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import * as adminAPI from '../../api/admin';
import './AdminOrders.css';

const AdminOrders = () => {
  const [orders, setOrders] = useState([]);
  const [loading, setLoading] = useState(true);
  const [filter, setFilter] = useState('ALL');
  const [searchTerm, setSearchTerm] = useState('');

  useEffect(() => {
    fetchOrders();
  }, []);

  const fetchOrders = async () => {
    try {
      const response = await adminAPI.getAllOrders();
      setOrders(response.data.data || []);
      setLoading(false);
    } catch (error) {
      console.error('Error fetching orders:', error);
      setLoading(false);
    }
  };

  const handleStatusUpdate = async (orderId, status) => {
    try {
      const response = await adminAPI.updateOrderStatus(orderId, status);
      if (response.data.success) {
        fetchOrders();
        alert('Order status updated successfully');
      }
    } catch (error) {
      console.error('Error updating order status:', error);
      alert('Error updating order status: ' + (error.response?.data?.message || error.message));
    }
  };

  const filteredOrders = orders.filter(order => {
    const matchesFilter = filter === 'ALL' || order.status === filter;
    const matchesSearch = searchTerm === '' || 
      order.orderNumber?.toLowerCase().includes(searchTerm.toLowerCase()) ||
      order.userName?.toLowerCase().includes(searchTerm.toLowerCase());
    return matchesFilter && matchesSearch;
  });

  const getStatusColor = (status) => {
    switch (status) {
      case 'CONFIRMED': return 'status-confirmed';
      case 'SHIPPED': return 'status-shipped';
      case 'DELIVERED': return 'status-delivered';
      case 'CANCELLED': return 'status-cancelled';
      default: return 'status-pending';
    }
  };

  const formatDate = (dateString) => {
    if (!dateString) return 'N/A';
    return new Date(dateString).toLocaleDateString('en-IN', {
      year: 'numeric',
      month: 'short',
      day: 'numeric',
      hour: '2-digit',
      minute: '2-digit'
    });
  };

  if (loading) {
    return <div className="admin-orders-loading">Loading orders...</div>;
  }

  return (
    <div className="admin-orders">
      <div className="admin-orders-container">
        <div className="admin-orders-header">
          <h2>All Orders</h2>
          <div className="admin-orders-filters">
            <input
              type="text"
              placeholder="Search by order number or customer name..."
              value={searchTerm}
              onChange={(e) => setSearchTerm(e.target.value)}
              className="search-input"
            />
            <select value={filter} onChange={(e) => setFilter(e.target.value)} className="filter-select">
              <option value="ALL">All Status</option>
              <option value="CONFIRMED">Confirmed</option>
              <option value="SHIPPED">Shipped</option>
              <option value="DELIVERED">Delivered</option>
              <option value="CANCELLED">Cancelled</option>
            </select>
          </div>
        </div>

        <div className="orders-stats">
          <div className="stat-card">
            <h3>{orders.length}</h3>
            <p>Total Orders</p>
          </div>
          <div className="stat-card">
            <h3>{orders.filter(o => o.status === 'CONFIRMED').length}</h3>
            <p>Confirmed</p>
          </div>
          <div className="stat-card">
            <h3>{orders.filter(o => o.status === 'SHIPPED').length}</h3>
            <p>Shipped</p>
          </div>
          <div className="stat-card">
            <h3>{orders.filter(o => o.status === 'DELIVERED').length}</h3>
            <p>Delivered</p>
          </div>
        </div>

        <div className="orders-table-container">
          <table className="orders-table">
            <thead>
              <tr>
                <th>Order Number</th>
                <th>Customer</th>
                <th>Date</th>
                <th>Items</th>
                <th>Amount</th>
                <th>Status</th>
                <th>Payment</th>
                <th>Actions</th>
              </tr>
            </thead>
            <tbody>
              {filteredOrders.length === 0 ? (
                <tr>
                  <td colSpan="8" className="no-orders">No orders found</td>
                </tr>
              ) : (
                filteredOrders.map(order => (
                  <tr key={order.id}>
                    <td>
                      <Link to={`/orders/${order.id}`} className="order-link">
                        {order.orderNumber}
                      </Link>
                    </td>
                    <td>{order.userName || 'N/A'}</td>
                    <td>{formatDate(order.createdAt)}</td>
                    <td>{order.orderItems?.length || 0} item(s)</td>
                    <td>₹{order.finalAmount?.toFixed(2) || '0.00'}</td>
                    <td>
                      <span className={`status-badge ${getStatusColor(order.status)}`}>
                        {order.status}
                      </span>
                    </td>
                    <td>
                      <span className={`payment-badge ${order.paymentStatus === 'PAID' ? 'paid' : 'pending'}`}>
                        {order.paymentStatus}
                      </span>
                    </td>
                    <td>
                      <div className="order-actions">
                        {order.status === 'CONFIRMED' && (
                          <button
                            onClick={() => handleStatusUpdate(order.id, 'SHIPPED')}
                            className="action-btn shipped-btn"
                          >
                            Ship
                          </button>
                        )}
                        {order.status === 'SHIPPED' && (
                          <button
                            onClick={() => handleStatusUpdate(order.id, 'DELIVERED')}
                            className="action-btn delivered-btn"
                          >
                            Deliver
                          </button>
                        )}
                        {order.status !== 'CANCELLED' && order.status !== 'DELIVERED' && (
                          <button
                            onClick={() => handleStatusUpdate(order.id, 'CANCELLED')}
                            className="action-btn cancel-btn"
                          >
                            Cancel
                          </button>
                        )}
                        <Link to={`/orders/${order.id}`} className="action-btn view-btn">
                          View
                        </Link>
                      </div>
                    </td>
                  </tr>
                ))
              )}
            </tbody>
          </table>
        </div>
      </div>
    </div>
  );
};

export default AdminOrders;
