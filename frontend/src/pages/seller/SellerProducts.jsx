import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import * as productAPI from '../../api/product';
import './SellerProducts.css';

const SellerProducts = () => {
  const [products, setProducts] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    fetchProducts();
  }, []);

  const fetchProducts = async () => {
    try {
      const response = await productAPI.getMyProducts();
      setProducts(response.data.data.content);
      setLoading(false);
    } catch (error) {
      console.error('Error fetching products:', error);
      setLoading(false);
    }
  };

  if (loading) {
    return <div className="loading">Loading products...</div>;
  }

  return (
    <div className="seller-products">
      <div className="products-container">
        <div className="products-header">
          <h2>My Products</h2>
          <Link to="/seller/products/add" className="add-button">
            Add Product
          </Link>
        </div>
        <div className="products-list">
          {products.map(product => (
            <div key={product.id} className="product-item">
              <img 
                src={product.images && product.images.length > 0 
                  ? `http://localhost:8080${product.images[0]}` 
                  : 'https://via.placeholder.com/150'} 
                alt={product.name}
              />
              <div className="product-details">
                <h3>{product.name}</h3>
                <p>₹{product.discountPrice?.toLocaleString('en-IN')}</p>
                <p>Stock: {product.stockQuantity}</p>
                <div className="product-status">
                  <span className={`status-badge ${product.isApproved ? 'approved' : 'pending'}`}>
                    {product.isApproved ? '✓ Approved' : '⏳ Pending Approval'}
                  </span>
                  {!product.isApproved && (
                    <span className="status-note">(Product will be visible to customers after admin approval)</span>
                  )}
                </div>
              </div>
              <Link to={`/seller/products/edit/${product.id}`} className="edit-button">
                Edit
              </Link>
            </div>
          ))}
        </div>
      </div>
    </div>
  );
};

export default SellerProducts;

