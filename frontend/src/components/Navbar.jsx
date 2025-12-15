import React from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import { useCart } from '../context/CartContext';
import './Navbar.css';

const Navbar = () => {
  const { user, logout, isAuthenticated, hasRole } = useAuth();
  const { cartCount } = useCart();
  const navigate = useNavigate();

  const handleLogout = () => {
    logout();
    navigate('/');
  };

  return (
    <nav className="navbar">
      <div className="navbar-container">
        <Link to="/" className="navbar-logo">
          VYNTRA
        </Link>

        <div className="navbar-menu">
          {isAuthenticated() ? (
            <>
              {hasRole('ROLE_CUSTOMER') && (
                <>
                  <Link to="/cart" className="navbar-link">
                    Cart ({cartCount})
                  </Link>
                  <Link to="/wishlist" className="navbar-link">
                    Wishlist
                  </Link>
                  <Link to="/orders" className="navbar-link">
                    Orders
                  </Link>
                </>
              )}

              {hasRole('ROLE_SELLER') && (
                <Link to="/seller/dashboard" className="navbar-link">
                  Seller Dashboard
                </Link>
              )}

              {hasRole('ROLE_ADMIN') && (
                <Link to="/admin/dashboard" className="navbar-link">
                  Admin Dashboard
                </Link>
              )}

              <Link to="/profile" className="navbar-link">
                {user?.firstName} {user?.lastName}
              </Link>
              <button onClick={handleLogout} className="navbar-button">
                Logout
              </button>
            </>
          ) : (
            <>
              <Link to="/login" className="navbar-link">
                Login
              </Link>
              <Link to="/signup" className="navbar-link">
                Sign Up
              </Link>
            </>
          )}
        </div>
      </div>
    </nav>
  );
};

export default Navbar;

