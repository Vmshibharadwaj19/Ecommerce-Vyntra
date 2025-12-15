import axios from 'axios';

const API_URL = 'http://localhost:8080/api';

const getAuthHeaders = () => {
  const token = sessionStorage.getItem('token');
  return {
    'Authorization': `Bearer ${token}`,
    'Content-Type': 'application/json'
  };
};

export const getProductReviews = async (productId) => {
  return axios.get(`${API_URL}/reviews/product/${productId}`);
};

export const createReview = async (reviewData) => {
  return axios.post(`${API_URL}/reviews`, reviewData, {
    headers: getAuthHeaders()
  });
};

export const updateReview = async (id, reviewData) => {
  return axios.put(`${API_URL}/reviews/${id}`, reviewData, {
    headers: getAuthHeaders()
  });
};

export const deleteReview = async (id) => {
  return axios.delete(`${API_URL}/reviews/${id}`, {
    headers: getAuthHeaders()
  });
};



