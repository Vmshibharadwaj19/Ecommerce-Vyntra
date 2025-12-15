import axios from 'axios';

const API_URL = 'http://localhost:8080/api';

const getAuthHeaders = () => {
  const token = sessionStorage.getItem('token');
  return {
    'Authorization': `Bearer ${token}`,
    'Content-Type': 'application/json'
  };
};

export const createRazorpayOrder = async (orderRequest) => {
  return axios.post(`${API_URL}/payment/create-order`, orderRequest, {
    headers: getAuthHeaders()
  });
};

export const verifyPayment = async (verificationRequest) => {
  return axios.post(`${API_URL}/payment/verify`, verificationRequest, {
    headers: getAuthHeaders()
  });
};

// Enterprise Payment APIs
export const getPaymentById = async (paymentId) => {
  return axios.get(`${API_URL}/payment/${paymentId}`, {
    headers: getAuthHeaders()
  });
};

export const getPaymentByOrderId = async (orderId) => {
  return axios.get(`${API_URL}/payment/order/${orderId}`, {
    headers: getAuthHeaders()
  });
};

export const getPaymentHistory = async () => {
  return axios.get(`${API_URL}/payment/history`, {
    headers: getAuthHeaders()
  });
};

export const getAllPayments = async (page = 0, size = 20) => {
  return axios.get(`${API_URL}/payment/all?page=${page}&size=${size}`, {
    headers: getAuthHeaders()
  });
};

export const processRefund = async (refundRequest) => {
  return axios.post(`${API_URL}/payment/refund`, refundRequest, {
    headers: getAuthHeaders()
  });
};

export const retryPayment = async (paymentId) => {
  return axios.post(`${API_URL}/payment/${paymentId}/retry`, {}, {
    headers: getAuthHeaders()
  });
};

export const getPaymentAnalytics = async () => {
  return axios.get(`${API_URL}/payment/analytics`, {
    headers: getAuthHeaders()
  });
};

export const getPaymentAnalyticsByDateRange = async (startDate, endDate) => {
  return axios.get(`${API_URL}/payment/analytics/range?startDate=${startDate}&endDate=${endDate}`, {
    headers: getAuthHeaders()
  });
};

