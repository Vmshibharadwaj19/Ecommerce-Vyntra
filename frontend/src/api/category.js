import axios from 'axios';

const API_URL = 'http://localhost:8080/api';

const getAuthHeaders = () => {
  const token = sessionStorage.getItem('token');
  return {
    'Authorization': `Bearer ${token}`,
    'Content-Type': 'application/json'
  };
};

export const getAllCategories = async () => {
  return axios.get(`${API_URL}/categories`);
};

export const getCategoryById = async (id) => {
  return axios.get(`${API_URL}/categories/${id}`);
};

export const getSubCategories = async (categoryId) => {
  return axios.get(`${API_URL}/categories/${categoryId}/subcategories`);
};

// Admin functions
export const createCategory = async (categoryData) => {
  return axios.post(`${API_URL}/categories`, categoryData, {
    headers: getAuthHeaders()
  });
};

export const updateCategory = async (id, categoryData) => {
  return axios.put(`${API_URL}/categories/${id}`, categoryData, {
    headers: getAuthHeaders()
  });
};

export const deleteCategory = async (id) => {
  return axios.delete(`${API_URL}/categories/${id}`, {
    headers: getAuthHeaders()
  });
};

export const createSubCategory = async (subCategoryData) => {
  return axios.post(`${API_URL}/categories/subcategories`, subCategoryData, {
    headers: getAuthHeaders()
  });
};

export const updateSubCategory = async (id, subCategoryData) => {
  return axios.put(`${API_URL}/categories/subcategories/${id}`, subCategoryData, {
    headers: getAuthHeaders()
  });
};

export const deleteSubCategory = async (id) => {
  return axios.delete(`${API_URL}/categories/subcategories/${id}`, {
    headers: getAuthHeaders()
  });
};



