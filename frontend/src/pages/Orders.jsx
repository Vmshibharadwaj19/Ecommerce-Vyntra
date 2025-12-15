import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import * as orderAPI from '../api/order';
import './Orders.css';

const Orders = () => {
  const [orders, setOrders] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    fetchOrders();
  }, []);

  const fetchOrders = async () => {
    try {
      const response = await orderAPI.getUserOrders();
      setOrders(response.data.data);
      setLoading(false);
    } catch (error) {
      console.error('Error fetching orders:', error);
      setLoading(false);
    }
  };

  if (loading) {
    return <div className="loading">Loading orders...</div>;
  }

  return (
    <div className="orders-page">
      <div className="orders-container">
        <h2>My Orders</h2>
        {orders.length === 0 ? (
          <p>No orders found</p>
        ) : (
          <div className="orders-list">
            {orders.map(order => (
              <Link key={order.id} to={`/orders/${order.id}`} className="order-card">
                <div className="order-header">
                  <span>Order #{order.orderNumber}</span>
                  <span className={`status ${order.status.toLowerCase()}`}>{order.status}</span>
                </div>
                <div className="order-details">
                  <p>Total: ₹{order.finalAmount}</p>
                  <p>Date: {new Date(order.createdAt).toLocaleDateString()}</p>
                </div>
              </Link>
            ))}
          </div>
        )}
      </div>
    </div>
  );
};

export default Orders;



