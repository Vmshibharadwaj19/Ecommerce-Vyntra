import axios from 'axios';

const API_URL = 'http://localhost:8080/api';

const getAuthHeaders = () => {
  const token = sessionStorage.getItem('token');
  return {
    'Authorization': `Bearer ${token}`,
    'Content-Type': 'application/json'
  };
};

export const getCart = async () => {
  return axios.get(`${API_URL}/cart`, { headers: getAuthHeaders() });
};

export const addToCart = async (productId, quantity) => {
  return axios.post(`${API_URL}/cart/add?productId=${productId}&quantity=${quantity}`, {}, {
    headers: getAuthHeaders()
  });
};

export const updateCartItem = async (cartItemId, quantity) => {
  return axios.put(`${API_URL}/cart/items/${cartItemId}?quantity=${quantity}`, {}, {
    headers: getAuthHeaders()
  });
};

export const removeFromCart = async (cartItemId) => {
  return axios.delete(`${API_URL}/cart/items/${cartItemId}`, {
    headers: getAuthHeaders()
  });
};

export const clearCart = async () => {
  return axios.delete(`${API_URL}/cart/clear`, { headers: getAuthHeaders() });
};

export const getCartItemCount = async () => {
  return axios.get(`${API_URL}/cart/count`, { headers: getAuthHeaders() });
};



