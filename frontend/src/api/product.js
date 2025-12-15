import axios from 'axios';

const API_URL = 'http://localhost:8080/api';

const getAuthHeaders = () => {
  const token = sessionStorage.getItem('token');
  return {
    'Authorization': `Bearer ${token}`,
    'Content-Type': 'application/json'
  };
};

export const getAllProducts = async (page = 0, size = 20) => {
  return axios.get(`${API_URL}/products/public?page=${page}&size=${size}`);
};

export const getProductById = async (id) => {
  return axios.get(`${API_URL}/products/public/${id}`);
};

export const searchProducts = async (searchRequest) => {
  return axios.post(`${API_URL}/products/search`, searchRequest);
};

export const getProductsByCategory = async (categoryId, page = 0, size = 20) => {
  return axios.get(`${API_URL}/products/public/category/${categoryId}?page=${page}&size=${size}`);
};

export const createProduct = async (productData, images) => {
  const formData = new FormData();
  // Create a File-like object from the JSON string
  const productBlob = new Blob([JSON.stringify(productData)], { type: 'application/json' });
  formData.append('product', productBlob, 'product.json');
  
  if (images && images.length > 0) {
    images.forEach((image) => {
      formData.append('images', image);
    });
  }

  return axios.post(`${API_URL}/products`, formData, {
    headers: {
      'Authorization': `Bearer ${sessionStorage.getItem('token')}`,
      'Content-Type': 'multipart/form-data'
    }
  });
};

// Alternative method for creating products without images (for testing)
export const createProductSimple = async (productData) => {
  return axios.post(`${API_URL}/products/simple`, productData, {
    headers: {
      'Authorization': `Bearer ${sessionStorage.getItem('token')}`,
      'Content-Type': 'application/json'
    }
  });
};

export const updateProduct = async (id, productData, images) => {
  const formData = new FormData();
  const productBlob = new Blob([JSON.stringify(productData)], { type: 'application/json' });
  formData.append('product', productBlob, 'product.json');
  
  if (images && images.length > 0) {
    images.forEach((image) => {
      formData.append('images', image);
    });
  }

  return axios.put(`${API_URL}/products/${id}`, formData, {
    headers: {
      'Authorization': `Bearer ${sessionStorage.getItem('token')}`,
      'Content-Type': 'multipart/form-data'
    }
  });
};

export const deleteProduct = async (id) => {
  return axios.delete(`${API_URL}/products/${id}`, { headers: getAuthHeaders() });
};

export const getMyProducts = async (page = 0, size = 20) => {
  return axios.get(`${API_URL}/products/seller/my-products?page=${page}&size=${size}`, {
    headers: getAuthHeaders()
  });
};

