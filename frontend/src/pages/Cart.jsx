import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import { useCart } from '../context/CartContext';
import * as cartAPI from '../api/cart';
import './Cart.css';

const Cart = () => {
  const { isAuthenticated } = useAuth();
  const { updateCartCount } = useCart();
  const navigate = useNavigate();
  const [cart, setCart] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    if (!isAuthenticated()) {
      navigate('/login');
      return;
    }
    fetchCart();
  }, []);

  const fetchCart = async () => {
    try {
      const response = await cartAPI.getCart();
      setCart(response.data.data);
      setLoading(false);
    } catch (error) {
      console.error('Error fetching cart:', error);
      setLoading(false);
    }
  };

  const handleUpdateQuantity = async (cartItemId, newQuantity) => {
    try {
      const response = await cartAPI.updateCartItem(cartItemId, newQuantity);
      setCart(response.data.data);
      await updateCartCount(); // Update cart count in navbar
    } catch (error) {
      console.error('Error updating cart:', error);
      alert('Error updating cart: ' + (error.response?.data?.message || error.message));
    }
  };

  const handleRemoveItem = async (cartItemId) => {
    try {
      const response = await cartAPI.removeFromCart(cartItemId);
      setCart(response.data.data);
      await updateCartCount(); // Update cart count in navbar
    } catch (error) {
      console.error('Error removing item:', error);
      alert('Error removing item: ' + (error.response?.data?.message || error.message));
    }
  };

  const handleCheckout = () => {
    navigate('/checkout');
  };

  if (loading) {
    return <div className="loading">Loading cart...</div>;
  }

  if (!cart || cart.cartItems.length === 0) {
    return (
      <div className="cart-page">
        <div className="cart-container">
          <h2>Your Cart is Empty</h2>
          <button onClick={() => navigate('/')} className="continue-shopping">
            Continue Shopping
          </button>
        </div>
      </div>
    );
  }

  return (
    <div className="cart-page">
      <div className="cart-container">
        <h2>Shopping Cart</h2>
        <div className="cart-items">
          {cart.cartItems.map(item => (
            <div key={item.id} className="cart-item">
              <img 
                src={item.productImage ? `http://localhost:8080${item.productImage}` : 'https://via.placeholder.com/100'} 
                alt={item.productName}
                className="cart-item-image"
              />
              <div className="cart-item-details">
                <h3>{item.productName}</h3>
                <p>₹{item.price}</p>
              </div>
              <div className="cart-item-quantity">
                <button onClick={() => handleUpdateQuantity(item.id, item.quantity - 1)}>-</button>
                <span>{item.quantity}</span>
                <button onClick={() => handleUpdateQuantity(item.id, item.quantity + 1)}>+</button>
              </div>
              <div className="cart-item-total">
                ₹{item.totalPrice}
              </div>
              <button onClick={() => handleRemoveItem(item.id)} className="remove-button">
                Remove
              </button>
            </div>
          ))}
        </div>
        <div className="cart-summary">
          <h3>Total: ₹{cart.totalAmount}</h3>
          <button onClick={handleCheckout} className="checkout-button">
            Proceed to Checkout
          </button>
        </div>
      </div>
    </div>
  );
};

export default Cart;



