import axios from 'axios';

const API_URL = 'http://localhost:8080/api';

const getAuthHeaders = () => {
  const token = sessionStorage.getItem('token');
  return {
    'Authorization': `Bearer ${token}`,
    'Content-Type': 'application/json'
  };
};

export const getDashboardStats = async () => {
  return axios.get(`${API_URL}/admin/dashboard`, {
    headers: getAuthHeaders()
  });
};

export const getAllUsers = async () => {
  return axios.get(`${API_URL}/admin/users`, {
    headers: getAuthHeaders()
  });
};

export const blockUser = async (userId) => {
  return axios.put(`${API_URL}/admin/users/${userId}/block`, {}, {
    headers: getAuthHeaders()
  });
};

export const unblockUser = async (userId) => {
  return axios.put(`${API_URL}/admin/users/${userId}/unblock`, {}, {
    headers: getAuthHeaders()
  });
};

export const getPendingSellers = async () => {
  return axios.get(`${API_URL}/admin/sellers/pending`, {
    headers: getAuthHeaders()
  });
};

export const approveSeller = async (sellerId) => {
  return axios.put(`${API_URL}/admin/sellers/${sellerId}/approve`, {}, {
    headers: getAuthHeaders()
  });
};

export const rejectSeller = async (sellerId) => {
  return axios.put(`${API_URL}/admin/sellers/${sellerId}/reject`, {}, {
    headers: getAuthHeaders()
  });
};

export const getPendingProducts = async () => {
  return axios.get(`${API_URL}/admin/products/pending`, {
    headers: getAuthHeaders()
  });
};

export const approveProduct = async (productId) => {
  return axios.put(`${API_URL}/admin/products/${productId}/approve`, {}, {
    headers: getAuthHeaders()
  });
};

export const rejectProduct = async (productId) => {
  return axios.put(`${API_URL}/admin/products/${productId}/reject`, {}, {
    headers: getAuthHeaders()
  });
};

export const getAllOrders = async () => {
  return axios.get(`${API_URL}/admin/orders`, {
    headers: getAuthHeaders()
  });
};

export const updateOrderStatus = async (orderId, status) => {
  return axios.put(`${API_URL}/orders/${orderId}/status?status=${status}`, {}, {
    headers: getAuthHeaders()
  });
};

