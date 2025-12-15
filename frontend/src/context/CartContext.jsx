import React, { createContext, useContext, useState, useEffect } from 'react';
import { useAuth } from './AuthContext';
import * as cartAPI from '../api/cart';

const CartContext = createContext();

export const useCart = () => {
  const context = useContext(CartContext);
  if (!context) {
    throw new Error('useCart must be used within CartProvider');
  }
  return context;
};

export const CartProvider = ({ children }) => {
  const { isAuthenticated } = useAuth();
  const [cartCount, setCartCount] = useState(0);
  const [loading, setLoading] = useState(false);

  const fetchCartCount = async () => {
    if (!isAuthenticated()) {
      setCartCount(0);
      return;
    }
    try {
      const response = await cartAPI.getCartItemCount();
      setCartCount(response.data.data || 0);
    } catch (error) {
      console.error('Error fetching cart count:', error);
      setCartCount(0);
    }
  };

  useEffect(() => {
    if (isAuthenticated()) {
      fetchCartCount();
    } else {
      setCartCount(0);
    }
  }, [isAuthenticated]);

  const updateCartCount = async () => {
    await fetchCartCount();
  };

  const incrementCartCount = () => {
    setCartCount(prev => prev + 1);
  };

  const decrementCartCount = () => {
    setCartCount(prev => Math.max(0, prev - 1));
  };

  const resetCartCount = () => {
    setCartCount(0);
  };

  return (
    <CartContext.Provider value={{
      cartCount,
      updateCartCount,
      incrementCartCount,
      decrementCartCount,
      resetCartCount,
      loading
    }}>
      {children}
    </CartContext.Provider>
  );
};

