import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import * as sellerAPI from '../../api/seller';
import './SellerOrders.css';

const SellerOrders = () => {
  const [orders, setOrders] = useState([]);
  const [loading, setLoading] = useState(true);
  const [filter, setFilter] = useState('ALL');
  const [searchTerm, setSearchTerm] = useState('');

  useEffect(() => {
    fetchOrders();
  }, []);

  const fetchOrders = async () => {
    try {
      const response = await sellerAPI.getSellerOrders();
      setOrders(response.data.data || []);
      setLoading(false);
    } catch (error) {
      console.error('Error fetching orders:', error);
      setLoading(false);
    }
  };

  const handleStatusUpdate = async (orderId, status) => {
    try {
      const response = await sellerAPI.updateSellerOrderStatus(orderId, status);
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

  // Get seller-specific order items (only items from seller's products)
  const getSellerOrderItems = (order) => {
    if (!order.orderItems) return [];
    // Filter items that belong to this seller
    // Note: Backend should filter this, but we'll show all items for now
    return order.orderItems;
  };

  const calculateSellerTotal = (order) => {
    const sellerItems = getSellerOrderItems(order);
    return sellerItems.reduce((sum, item) => {
      return sum + (parseFloat(item.totalPrice) || 0);
    }, 0);
  };

  if (loading) {
    return <div className="seller-orders-loading">Loading orders...</div>;
  }

  return (
    <div className="seller-orders">
      <div className="seller-orders-container">
        <div className="seller-orders-header">
          <h2>My Orders</h2>
          <div className="seller-orders-filters">
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

        <div className="orders-list">
          {filteredOrders.length === 0 ? (
            <div className="no-orders">No orders found</div>
          ) : (
            filteredOrders.map(order => {
              const sellerItems = getSellerOrderItems(order);
              const sellerTotal = calculateSellerTotal(order);
              
              return (
                <div key={order.id} className="order-card">
                  <div className="order-card-header">
                    <div>
                      <Link to={`/orders/${order.id}`} className="order-number">
                        Order #{order.orderNumber}
                      </Link>
                      <p className="order-date">{formatDate(order.createdAt)}</p>
                    </div>
                    <span className={`status-badge ${getStatusColor(order.status)}`}>
                      {order.status}
                    </span>
                  </div>

                  <div className="order-items">
                    <h4>Items ({sellerItems.length})</h4>
                    {sellerItems.map((item, index) => (
                      <div key={index} className="order-item">
                        <img 
                          src={item.productImage || '/placeholder.png'} 
                          alt={item.productName}
                          className="item-image"
                        />
                        <div className="item-details">
                          <p className="item-name">{item.productName}</p>
                          <p className="item-quantity">Qty: {item.quantity}</p>
                          <p className="item-price">₹{item.totalPrice?.toFixed(2)}</p>
                        </div>
                      </div>
                    ))}
                  </div>

                  <div className="order-card-footer">
                    <div className="order-total">
                      <strong>Total: ₹{sellerTotal.toFixed(2)}</strong>
                    </div>
                    <div className="order-actions">
                      {order.status === 'CONFIRMED' && (
                        <button
                          onClick={() => handleStatusUpdate(order.id, 'SHIPPED')}
                          className="action-btn shipped-btn"
                        >
                          Mark as Shipped
                        </button>
                      )}
                      {order.status === 'SHIPPED' && (
                        <button
                          onClick={() => handleStatusUpdate(order.id, 'DELIVERED')}
                          className="action-btn delivered-btn"
                        >
                          Mark as Delivered
                        </button>
                      )}
                      <Link to={`/orders/${order.id}`} className="action-btn view-btn">
                        View Details
                      </Link>
                    </div>
                  </div>
                </div>
              );
            })
          )}
        </div>
      </div>
    </div>
  );
};

export default SellerOrders;
