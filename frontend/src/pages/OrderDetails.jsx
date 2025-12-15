import React, { useState, useEffect } from 'react';
import { useParams } from 'react-router-dom';
import * as orderAPI from '../api/order';
import './OrderDetails.css';

const OrderDetails = () => {
  const { id } = useParams();
  const [order, setOrder] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    fetchOrder();
  }, [id]);

  const fetchOrder = async () => {
    try {
      const response = await orderAPI.getOrderById(id);
      setOrder(response.data.data);
      setLoading(false);
    } catch (error) {
      console.error('Error fetching order:', error);
      setLoading(false);
    }
  };

  if (loading) {
    return <div className="loading">Loading order details...</div>;
  }

  if (!order) {
    return <div>Order not found</div>;
  }

  return (
    <div className="order-details-page">
      <div className="order-details-container">
        <h2>Order Details - #{order.orderNumber}</h2>
        <div className="order-info">
          <p><strong>Status:</strong> {order.status}</p>
          <p><strong>Payment Status:</strong> {order.paymentStatus}</p>
          <p><strong>Total Amount:</strong> ₹{order.finalAmount}</p>
          <p><strong>Order Date:</strong> {new Date(order.createdAt).toLocaleString()}</p>
        </div>
        <div className="order-items">
          <h3>Order Items</h3>
          {order.orderItems.map(item => (
            <div key={item.id} className="order-item">
              <img 
                src={item.productImage ? `http://localhost:8080${item.productImage}` : 'https://via.placeholder.com/100'} 
                alt={item.productName}
              />
              <div>
                <h4>{item.productName}</h4>
                <p>Quantity: {item.quantity}</p>
                <p>Price: ₹{item.price}</p>
                <p>Total: ₹{item.totalPrice}</p>
              </div>
            </div>
          ))}
        </div>
      </div>
    </div>
  );
};

export default OrderDetails;



