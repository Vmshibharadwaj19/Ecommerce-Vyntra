import axios from 'axios';

const API_URL = 'http://localhost:8080/api';

const getAuthHeaders = () => {
  const token = sessionStorage.getItem('token');
  return {
    'Authorization': `Bearer ${token}`,
    'Content-Type': 'application/json'
  };
};

export const getSellerOrders = async () => {
  return axios.get(`${API_URL}/seller/orders`, {
    headers: getAuthHeaders()
  });
};

export const updateSellerOrderStatus = async (orderId, status) => {
  return axios.put(`${API_URL}/seller/orders/${orderId}/status?status=${status}`, {}, {
    headers: getAuthHeaders()
  });
};



