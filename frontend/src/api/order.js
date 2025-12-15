import axios from 'axios';

const API_URL = 'http://localhost:8080/api';

const getAuthHeaders = () => {
  const token = sessionStorage.getItem('token');
  return {
    'Authorization': `Bearer ${token}`,
    'Content-Type': 'application/json'
  };
};

export const getUserOrders = async () => {
  return axios.get(`${API_URL}/orders`, { headers: getAuthHeaders() });
};

export const getOrderById = async (id) => {
  return axios.get(`${API_URL}/orders/${id}`, { headers: getAuthHeaders() });
};

export const createOrder = async (addressId, razorpayOrderId, razorpayPaymentId, razorpaySignature) => {
  return axios.post(
    `${API_URL}/orders?addressId=${addressId}&razorpayOrderId=${razorpayOrderId}&razorpayPaymentId=${razorpayPaymentId}&razorpaySignature=${razorpaySignature}`,
    {},
    { headers: getAuthHeaders() }
  );
};

export const updateOrderStatus = async (id, status) => {
  return axios.put(`${API_URL}/orders/${id}/status?status=${status}`, {}, {
    headers: getAuthHeaders()
  });
};

export const checkout = async (checkoutRequest) => {
  return axios.post(`${API_URL}/orders/checkout`, checkoutRequest, {
    headers: getAuthHeaders()
  });
};

