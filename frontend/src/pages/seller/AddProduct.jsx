import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import * as productAPI from '../../api/product';
import * as categoryAPI from '../../api/category';
import './AddProduct.css';

const AddProduct = () => {
  const navigate = useNavigate();
  const [formData, setFormData] = useState({
    name: '',
    description: '',
    price: '',
    discountPrice: '',
    stockQuantity: '',
    categoryId: '',
    subCategoryId: '',
    brand: '',
    color: '',
    size: ''
  });
  const [images, setImages] = useState([]);
  const [loading, setLoading] = useState(false);
  const [categories, setCategories] = useState([]);
  const [subCategories, setSubCategories] = useState([]);
  const [loadingCategories, setLoadingCategories] = useState(true);
  const [errors, setErrors] = useState({});

  useEffect(() => {
    fetchCategories();
  }, []);

  useEffect(() => {
    if (formData.categoryId) {
      fetchSubCategories(formData.categoryId);
    } else {
      setSubCategories([]);
      setFormData(prev => ({ ...prev, subCategoryId: '' }));
    }
  }, [formData.categoryId]);

  const fetchCategories = async () => {
    try {
      const response = await categoryAPI.getAllCategories();
      setCategories(response.data.data);
      setLoadingCategories(false);
    } catch (error) {
      console.error('Error fetching categories:', error);
      setLoadingCategories(false);
    }
  };

  const fetchSubCategories = async (categoryId) => {
    try {
      const response = await categoryAPI.getSubCategories(categoryId);
      setSubCategories(response.data.data);
    } catch (error) {
      console.error('Error fetching subcategories:', error);
      setSubCategories([]);
    }
  };

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData({
      ...formData,
      [name]: value
    });
    
    // Clear error for this field when user starts typing
    if (errors[name]) {
      setErrors({
        ...errors,
        [name]: ''
      });
    }
    
    // Real-time validation for price comparison
    if (name === 'price' || name === 'discountPrice') {
      const price = parseFloat(name === 'price' ? value : formData.price);
      const discountPrice = parseFloat(name === 'discountPrice' ? value : formData.discountPrice);
      
      if (!isNaN(price) && !isNaN(discountPrice) && discountPrice > price) {
        setErrors({
          ...errors,
          discountPrice: 'Discount price cannot be greater than regular price'
        });
      } else if (errors.discountPrice && errors.discountPrice.includes('cannot be greater')) {
        setErrors({
          ...errors,
          discountPrice: ''
        });
      }
    }
  };

  const handleImageChange = (e) => {
    setImages(Array.from(e.target.files));
  };

  const validateForm = () => {
    const newErrors = {};

    // Validate name
    if (!formData.name || formData.name.trim().length < 3) {
      newErrors.name = 'Product name must be at least 3 characters';
    }

    // Validate description
    if (!formData.description || formData.description.trim().length === 0) {
      newErrors.description = 'Description is required';
    }

    // Validate price
    const price = parseFloat(formData.price);
    if (!formData.price || isNaN(price) || price <= 0) {
      newErrors.price = 'Price must be a valid number greater than 0';
    }

    // Validate discount price
    const discountPrice = parseFloat(formData.discountPrice);
    if (!formData.discountPrice || isNaN(discountPrice) || discountPrice <= 0) {
      newErrors.discountPrice = 'Discount price must be a valid number greater than 0';
    }

    // Validate discount price is less than or equal to price
    if (!newErrors.price && !newErrors.discountPrice && discountPrice > price) {
      newErrors.discountPrice = 'Discount price cannot be greater than regular price';
    }

    // Validate stock quantity
    const stockQuantity = parseInt(formData.stockQuantity);
    if (!formData.stockQuantity || isNaN(stockQuantity) || stockQuantity < 0) {
      newErrors.stockQuantity = 'Stock quantity must be a valid number (0 or greater)';
    }

    // Validate category
    if (!formData.categoryId) {
      newErrors.categoryId = 'Category is required';
    }

    setErrors(newErrors);
    return Object.keys(newErrors).length === 0;
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    
    // Validate form before submission
    if (!validateForm()) {
      return;
    }

    setLoading(true);
    setErrors({});
    
    try {
      // Convert string values to numbers where needed
      const price = parseFloat(formData.price);
      const discountPrice = parseFloat(formData.discountPrice);
      const stockQuantity = parseInt(formData.stockQuantity);
      const categoryId = parseInt(formData.categoryId);

      // Final validation
      if (isNaN(price) || price <= 0) {
        setErrors({ price: 'Invalid price value' });
        setLoading(false);
        return;
      }
      if (isNaN(discountPrice) || discountPrice <= 0) {
        setErrors({ discountPrice: 'Invalid discount price value' });
        setLoading(false);
        return;
      }
      if (isNaN(stockQuantity) || stockQuantity < 0) {
        setErrors({ stockQuantity: 'Invalid stock quantity value' });
        setLoading(false);
        return;
      }
      if (isNaN(categoryId)) {
        setErrors({ categoryId: 'Invalid category selected' });
        setLoading(false);
        return;
      }

      const productData = {
        name: formData.name.trim(),
        description: formData.description.trim(),
        price: price,
        discountPrice: discountPrice,
        stockQuantity: stockQuantity,
        categoryId: categoryId,
        subCategoryId: formData.subCategoryId ? parseInt(formData.subCategoryId) : null,
        brand: formData.brand?.trim() || null,
        color: formData.color?.trim() || null,
        size: formData.size?.trim() || null
      };

      await productAPI.createProduct(productData, images);
      navigate('/seller/products');
    } catch (error) {
      console.error('Error creating product:', error);
      let errorMessage = 'Error creating product';
      
      if (error.response?.data?.message) {
        errorMessage = error.response.data.message;
      } else if (error.response?.data?.data) {
        errorMessage = error.response.data.data;
      } else if (error.message) {
        errorMessage = error.message;
      }
      
      setErrors({ submit: errorMessage });
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="add-product-page">
      <div className="add-product-container">
        <h2>Add Product</h2>
        <form onSubmit={handleSubmit}>
          {errors.submit && (
            <div className="error-message">{errors.submit}</div>
          )}
          
          <div className="form-group">
            <label>Product Name *</label>
            <input
              type="text"
              name="name"
              value={formData.name}
              onChange={handleChange}
              required
              minLength={3}
              maxLength={200}
              className={errors.name ? 'error' : ''}
            />
            {errors.name && <span className="field-error">{errors.name}</span>}
          </div>
          
          <div className="form-group">
            <label>Description *</label>
            <textarea
              name="description"
              value={formData.description}
              onChange={handleChange}
              rows="4"
              required
              maxLength={2000}
              className={errors.description ? 'error' : ''}
            />
            {errors.description && <span className="field-error">{errors.description}</span>}
          </div>
          
          <div className="form-row">
            <div className="form-group">
              <label>Price (₹) *</label>
              <input
                type="number"
                name="price"
                value={formData.price}
                onChange={handleChange}
                required
                step="0.01"
                min="0.01"
                className={errors.price ? 'error' : ''}
              />
              {errors.price && <span className="field-error">{errors.price}</span>}
            </div>
            <div className="form-group">
              <label>Discount Price (₹) *</label>
              <input
                type="number"
                name="discountPrice"
                value={formData.discountPrice}
                onChange={handleChange}
                required
                step="0.01"
                min="0.01"
                className={errors.discountPrice ? 'error' : ''}
              />
              {errors.discountPrice && <span className="field-error">{errors.discountPrice}</span>}
            </div>
          </div>
          
          <div className="form-group">
            <label>Stock Quantity *</label>
            <input
              type="number"
              name="stockQuantity"
              value={formData.stockQuantity}
              onChange={handleChange}
              required
              min="0"
              className={errors.stockQuantity ? 'error' : ''}
            />
            {errors.stockQuantity && <span className="field-error">{errors.stockQuantity}</span>}
          </div>
          <div className="form-group">
            <label>Category *</label>
            {loadingCategories ? (
              <div>Loading categories...</div>
            ) : (
              <>
                <select
                  name="categoryId"
                  value={formData.categoryId}
                  onChange={handleChange}
                  required
                  className={errors.categoryId ? 'error' : ''}
                >
                  <option value="">Select a category</option>
                  {categories.map(category => (
                    <option key={category.id} value={category.id}>
                      {category.name}
                    </option>
                  ))}
                </select>
                {errors.categoryId && <span className="field-error">{errors.categoryId}</span>}
              </>
            )}
          </div>
          {formData.categoryId && (
            <div className="form-group">
              <label>Sub Category (Optional)</label>
              <select
                name="subCategoryId"
                value={formData.subCategoryId}
                onChange={handleChange}
              >
                <option value="">None</option>
                {subCategories.map(subCategory => (
                  <option key={subCategory.id} value={subCategory.id}>
                    {subCategory.name}
                  </option>
                ))}
              </select>
            </div>
          )}
          <div className="form-group">
            <label>Brand</label>
            <input
              type="text"
              name="brand"
              value={formData.brand}
              onChange={handleChange}
            />
          </div>
          <div className="form-row">
            <div className="form-group">
              <label>Color</label>
              <input
                type="text"
                name="color"
                value={formData.color}
                onChange={handleChange}
              />
            </div>
            <div className="form-group">
              <label>Size</label>
              <input
                type="text"
                name="size"
                value={formData.size}
                onChange={handleChange}
                placeholder="e.g., M, L, XL, 16 inch"
              />
            </div>
          </div>
          <div className="form-group">
            <label>Images</label>
            <input
              type="file"
              multiple
              accept="image/*"
              onChange={handleImageChange}
            />
          </div>
          <button type="submit" disabled={loading} className="submit-button">
            {loading ? 'Creating...' : 'Create Product'}
          </button>
        </form>
      </div>
    </div>
  );
};

export default AddProduct;

