import axios from 'axios';

const API_URL = 'http://localhost:8080/api';

const getAuthHeaders = () => {
  const token = sessionStorage.getItem('token');
  return {
    'Authorization': `Bearer ${token}`,
    'Content-Type': 'application/json'
  };
};

export const getUserWishlist = async () => {
  return axios.get(`${API_URL}/wishlist`, { headers: getAuthHeaders() });
};

export const addToWishlist = async (productId) => {
  return axios.post(`${API_URL}/wishlist/add?productId=${productId}`, {}, {
    headers: getAuthHeaders()
  });
};

export const removeFromWishlist = async (productId) => {
  return axios.delete(`${API_URL}/wishlist/remove?productId=${productId}`, {
    headers: getAuthHeaders()
  });
};

export const isInWishlist = async (productId) => {
  return axios.get(`${API_URL}/wishlist/check?productId=${productId}`, {
    headers: getAuthHeaders()
  });
};



