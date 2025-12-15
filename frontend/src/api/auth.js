import axios from 'axios';

const API_URL = 'http://localhost:8080/api';

const getAuthHeaders = () => {
  const token = sessionStorage.getItem('token');
  return {
    'Authorization': `Bearer ${token}`,
    'Content-Type': 'application/json'
  };
};

export const login = async (email, password) => {
  return axios.post(`${API_URL}/auth/signin`, { email, password });
};

export const signup = async (userData) => {
  return axios.post(`${API_URL}/auth/signup`, userData);
};

export const getCurrentUser = async () => {
  return axios.get(`${API_URL}/auth/me`, { headers: getAuthHeaders() });
};

export const forgotPassword = async (email) => {
  return axios.post(`${API_URL}/auth/forgot-password`, { email });
};

export const verifyOtp = async (email, otp) => {
  return axios.post(`${API_URL}/auth/verify-otp`, { email, otp });
};

export const resetPassword = async (email, otp, newPassword) => {
  return axios.post(`${API_URL}/auth/reset-password`, { email, otp, newPassword });
};



