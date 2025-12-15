import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import * as productAPI from '../api/product';
import * as categoryAPI from '../api/category';
import ProductCard from '../components/ProductCard';
import './Home.css';

const Home = () => {
  const [products, setProducts] = useState([]);
  const [featuredProducts, setFeaturedProducts] = useState([]);
  const [categories, setCategories] = useState([]);
  const [loading, setLoading] = useState(true);
  const [page, setPage] = useState(0);
  const [hasMore, setHasMore] = useState(true);

  useEffect(() => {
    fetchProducts();
    fetchCategories();
    fetchFeaturedProducts();
  }, [page]);

  const fetchProducts = async () => {
    try {
      const response = await productAPI.getAllProducts(page, 20);
      if (page === 0) {
        setProducts(response.data.data.content || response.data.data);
      } else {
        setProducts(prev => [...prev, ...(response.data.data.content || response.data.data)]);
      }
      setHasMore(response.data.data.last !== false);
      setLoading(false);
    } catch (error) {
      console.error('Error fetching products:', error);
      setLoading(false);
    }
  };

  const fetchFeaturedProducts = async () => {
    try {
      const response = await productAPI.getAllProducts(0, 8);
      setFeaturedProducts(response.data.data.content || response.data.data.slice(0, 8));
    } catch (error) {
      console.error('Error fetching featured products:', error);
    }
  };

  const fetchCategories = async () => {
    try {
      const response = await categoryAPI.getAllCategories();
      setCategories(response.data.data);
    } catch (error) {
      console.error('Error fetching categories:', error);
    }
  };

  const loadMore = () => {
    if (hasMore && !loading) {
      setPage(prev => prev + 1);
    }
  };

  if (loading && page === 0) {
    return <div className="loading">Loading products...</div>;
  }

  return (
    <div className="home">
      {/* Hero Banner */}
      <div className="hero-banner">
        <div className="hero-content">
          <h1>Welcome to VYNTRA</h1>
          <p>Shop the latest products with amazing deals</p>
          <Link to="/products" className="shop-now-btn">Shop Now</Link>
        </div>
      </div>

      {/* Categories Section */}
      {categories.length > 0 && (
        <div className="categories-section">
          <h2 className="section-title">Shop by Category</h2>
          <div className="categories-grid">
            {categories.map(category => (
              <Link key={category.id} to={`/products?category=${category.id}`} className="category-card">
                <div className="category-icon">📦</div>
                <h3>{category.name}</h3>
                <p>{category.description}</p>
              </Link>
            ))}
          </div>
        </div>
      )}

      {/* Deals Section */}
      <div className="deals-section">
        <h2 className="section-title">Today's Deals</h2>
        <div className="deals-banner">
          <div className="deal-item">
            <span className="deal-badge">UP TO 50% OFF</span>
            <p>Electronics</p>
          </div>
          <div className="deal-item">
            <span className="deal-badge">UP TO 40% OFF</span>
            <p>Fashion</p>
          </div>
          <div className="deal-item">
            <span className="deal-badge">UP TO 30% OFF</span>
            <p>Home & Kitchen</p>
          </div>
          <div className="deal-item">
            <span className="deal-badge">UP TO 25% OFF</span>
            <p>Books</p>
          </div>
        </div>
      </div>

      {/* Featured Products */}
      {featuredProducts.length > 0 && (
        <div className="featured-section">
          <h2 className="section-title">Featured Products</h2>
          <div className="products-grid">
            {featuredProducts.map(product => (
              <ProductCard key={product.id} product={product} />
            ))}
          </div>
        </div>
      )}

      {/* All Products */}
      <div className="products-section">
        <h2 className="section-title">All Products</h2>
        <div className="products-grid">
          {products.map(product => (
            <ProductCard key={product.id} product={product} />
          ))}
        </div>

        {hasMore && (
          <div className="load-more-container">
            <button onClick={loadMore} className="load-more-button" disabled={loading}>
              {loading ? 'Loading...' : 'Load More Products'}
            </button>
          </div>
        )}
      </div>
    </div>
  );
};

export default Home;
