import React, { useState, useEffect } from 'react';
import { useParams } from 'react-router-dom';
import * as productAPI from '../api/product';
import * as cartAPI from '../api/cart';
import * as wishlistAPI from '../api/wishlist';
import { useAuth } from '../context/AuthContext';
import { useCart } from '../context/CartContext';
import './ProductDetails.css';

const ProductDetails = () => {
  const { id } = useParams();
  const { isAuthenticated } = useAuth();
  const { updateCartCount } = useCart();
  const [product, setProduct] = useState(null);
  const [quantity, setQuantity] = useState(1);
  const [inWishlist, setInWishlist] = useState(false);

  useEffect(() => {
    fetchProduct();
    if (isAuthenticated()) {
      checkWishlist();
    }
  }, [id]);

  const fetchProduct = async () => {
    try {
      const response = await productAPI.getProductById(id);
      setProduct(response.data.data);
    } catch (error) {
      console.error('Error fetching product:', error);
    }
  };

  const checkWishlist = async () => {
    try {
      const response = await wishlistAPI.isInWishlist(id);
      setInWishlist(response.data.data);
    } catch (error) {
      console.error('Error checking wishlist:', error);
    }
  };

  const handleAddToCart = async () => {
    if (!isAuthenticated()) {
      alert('Please login to add items to cart');
      return;
    }
    try {
      await cartAPI.addToCart(id, quantity);
      await updateCartCount(); // Update cart count in navbar
      alert('Product added to cart');
    } catch (error) {
      console.error('Error adding to cart:', error);
      alert('Error adding to cart: ' + (error.response?.data?.message || error.message));
    }
  };

  const handleWishlist = async () => {
    if (!isAuthenticated()) {
      alert('Please login to add to wishlist');
      return;
    }
    try {
      if (inWishlist) {
        await wishlistAPI.removeFromWishlist(id);
        setInWishlist(false);
      } else {
        await wishlistAPI.addToWishlist(id);
        setInWishlist(true);
      }
    } catch (error) {
      console.error('Error updating wishlist:', error);
    }
  };

  if (!product) {
    return <div className="loading">Loading product...</div>;
  }

  return (
    <div className="product-details-page">
      <div className="product-details-container">
        <div className="product-images">
          {product.images && product.images.length > 0 ? (
            product.images.map((img, idx) => (
              <img key={idx} src={`http://localhost:8080${img}`} alt={product.name} />
            ))
          ) : (
            <img src="https://via.placeholder.com/500" alt={product.name} />
          )}
        </div>
        <div className="product-info">
          <h1>{product.name}</h1>
          <div className="product-rating">
            <span>⭐ {product.averageRating?.toFixed(1) || '0.0'}</span>
            <span>({product.totalReviews || 0} reviews)</span>
          </div>
          <div className="product-price">
            <span className="price">₹{product.discountPrice}</span>
            {product.price > product.discountPrice && (
              <span className="original-price">₹{product.price}</span>
            )}
          </div>
          <p className="product-description">{product.description}</p>
          <div className="product-actions">
            <div className="quantity-selector">
              <label>Quantity:</label>
              <button onClick={() => setQuantity(Math.max(1, quantity - 1))}>-</button>
              <span>{quantity}</span>
              <button onClick={() => setQuantity(quantity + 1)}>+</button>
            </div>
            <button onClick={handleAddToCart} className="add-to-cart-button">
              Add to Cart
            </button>
            <button onClick={handleWishlist} className="wishlist-button">
              {inWishlist ? 'Remove from Wishlist' : 'Add to Wishlist'}
            </button>
          </div>
        </div>
      </div>
    </div>
  );
};

export default ProductDetails;



