import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import * as wishlistAPI from '../api/wishlist';
import ProductCard from '../components/ProductCard';
import './Wishlist.css';

const Wishlist = () => {
  const [products, setProducts] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    fetchWishlist();
  }, []);

  const fetchWishlist = async () => {
    try {
      const response = await wishlistAPI.getUserWishlist();
      setProducts(response.data.data);
      setLoading(false);
    } catch (error) {
      console.error('Error fetching wishlist:', error);
      setLoading(false);
    }
  };

  if (loading) {
    return <div className="loading">Loading wishlist...</div>;
  }

  return (
    <div className="wishlist-page">
      <div className="wishlist-container">
        <h2>My Wishlist</h2>
        {products.length === 0 ? (
          <p>Your wishlist is empty</p>
        ) : (
          <div className="wishlist-grid">
            {products.map(product => (
              <ProductCard key={product.id} product={product} />
            ))}
          </div>
        )}
      </div>
    </div>
  );
};

export default Wishlist;



