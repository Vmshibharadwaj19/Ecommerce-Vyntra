import React from 'react';
import { Link } from 'react-router-dom';
import './ProductCard.css';

const ProductCard = ({ product }) => {
  // Handle image URL - check if it's a full URL or relative path
  const getImageUrl = () => {
    if (product.images && product.images.length > 0) {
      const firstImage = product.images[0];
      // If it's already a full URL, use it as is
      if (firstImage.startsWith('http://') || firstImage.startsWith('https://')) {
        return firstImage;
      }
      // If it's a relative path, prepend the backend URL
      if (firstImage.startsWith('/')) {
        return `http://localhost:8080${firstImage}`;
      }
      return `http://localhost:8080/uploads/${firstImage}`;
    }
    // Use placeholder image service with product name
    return `https://via.placeholder.com/300x300?text=${encodeURIComponent(product.name)}`;
  };

  const imageUrl = getImageUrl();
  
  // Calculate discount percentage
  const discountPercent = product.price && product.discountPrice 
    ? Math.round(((product.price - product.discountPrice) / product.price) * 100)
    : 0;

  return (
    <div className="product-card">
      <Link to={`/products/${product.id}`} className="product-link">
        <div className="product-image-container">
          {discountPercent > 0 && (
            <span className="discount-badge">{discountPercent}% OFF</span>
          )}
          <div className="product-image">
            <img 
              src={imageUrl} 
              alt={product.name}
              onError={(e) => {
                // Fallback to placeholder if image fails to load
                e.target.src = `https://via.placeholder.com/300x300?text=${encodeURIComponent(product.name)}`;
              }}
            />
          </div>
        </div>
        <div className="product-info">
          <h3 className="product-name" title={product.name}>
            {product.name}
          </h3>
          {product.brand && (
            <p className="product-brand">{product.brand}</p>
          )}
          <div className="product-rating">
            <span className="stars">{"⭐".repeat(Math.floor(product.averageRating || 0))}</span>
            <span className="rating-value">{product.averageRating?.toFixed(1) || '0.0'}</span>
            <span className="reviews-count">({product.totalReviews || 0})</span>
          </div>
          <div className="product-price">
            <span className="current-price">₹{product.discountPrice?.toLocaleString('en-IN') || product.price?.toLocaleString('en-IN')}</span>
            {product.price > product.discountPrice && (
              <>
                <span className="original-price">₹{product.price?.toLocaleString('en-IN')}</span>
                {discountPercent > 0 && (
                  <span className="savings">Save ₹{(product.price - product.discountPrice).toLocaleString('en-IN')}</span>
                )}
              </>
            )}
          </div>
          {product.stockQuantity !== undefined && (
            <div className="product-stock">
              {product.stockQuantity > 0 ? (
                <span className="in-stock">✓ In Stock</span>
              ) : (
                <span className="out-of-stock">✗ Out of Stock</span>
              )}
            </div>
          )}
          {product.sellerName && (
            <p className="seller-name">Sold by {product.sellerName}</p>
          )}
        </div>
      </Link>
    </div>
  );
};

export default ProductCard;
