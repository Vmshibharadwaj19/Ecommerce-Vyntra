import axios from 'axios';

const API_URL = 'http://localhost:8080/api';

const getAuthHeaders = () => {
  const token = sessionStorage.getItem('token');
  return {
    'Authorization': `Bearer ${token}`,
    'Content-Type': 'application/json'
  };
};

export const getUserAddresses = async () => {
  return axios.get(`${API_URL}/addresses`, { headers: getAuthHeaders() });
};

export const getAddressById = async (id) => {
  return axios.get(`${API_URL}/addresses/${id}`, { headers: getAuthHeaders() });
};

export const createAddress = async (addressData) => {
  return axios.post(`${API_URL}/addresses`, addressData, {
    headers: getAuthHeaders()
  });
};

export const updateAddress = async (id, addressData) => {
  return axios.put(`${API_URL}/addresses/${id}`, addressData, {
    headers: getAuthHeaders()
  });
};

export const deleteAddress = async (id) => {
  return axios.delete(`${API_URL}/addresses/${id}`, {
    headers: getAuthHeaders()
  });
};



