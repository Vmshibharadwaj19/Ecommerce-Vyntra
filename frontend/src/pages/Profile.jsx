import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import * as addressAPI from '../api/address';
import './Profile.css';

const Profile = () => {
  const { user } = useAuth();
  const [addresses, setAddresses] = useState([]);
  const [showAddForm, setShowAddForm] = useState(false);
  const [formData, setFormData] = useState({
    street: '',
    city: '',
    state: '',
    zipCode: '',
    country: 'India',
    landmark: '',
    addressType: 'HOME',
    isDefault: false
  });

  useEffect(() => {
    fetchAddresses();
  }, []);

  const fetchAddresses = async () => {
    try {
      const response = await addressAPI.getUserAddresses();
      setAddresses(response.data.data);
    } catch (error) {
      console.error('Error fetching addresses:', error);
    }
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      await addressAPI.createAddress(formData);
      fetchAddresses();
      setShowAddForm(false);
      setFormData({
        street: '',
        city: '',
        state: '',
        zipCode: '',
        country: 'India',
        landmark: '',
        addressType: 'HOME',
        isDefault: false
      });
    } catch (error) {
      console.error('Error creating address:', error);
    }
  };

  return (
    <div className="profile-page">
      <div className="profile-container">
        <h2>Profile</h2>
        <div className="profile-info">
          <p><strong>Name:</strong> {user?.firstName} {user?.lastName}</p>
          <p><strong>Email:</strong> {user?.email}</p>
          <p><strong>Phone:</strong> {user?.phoneNumber || 'N/A'}</p>
          <p><strong>Role:</strong> {user?.role}</p>
        </div>

        {user?.role === 'ROLE_CUSTOMER' && (
          <div className="quick-links">
            <Link to="/orders" className="quick-link">My Orders</Link>
            <Link to="/payments" className="quick-link">Payment History</Link>
            <Link to="/wishlist" className="quick-link">Wishlist</Link>
          </div>
        )}

        <div className="addresses-section">
          <div className="section-header">
            <h3>Addresses</h3>
            <button onClick={() => setShowAddForm(!showAddForm)}>
              {showAddForm ? 'Cancel' : 'Add Address'}
            </button>
          </div>

          {showAddForm && (
            <form onSubmit={handleSubmit} className="address-form">
              <input
                type="text"
                placeholder="Street"
                value={formData.street}
                onChange={(e) => setFormData({ ...formData, street: e.target.value })}
                required
              />
              <input
                type="text"
                placeholder="City"
                value={formData.city}
                onChange={(e) => setFormData({ ...formData, city: e.target.value })}
                required
              />
              <input
                type="text"
                placeholder="State"
                value={formData.state}
                onChange={(e) => setFormData({ ...formData, state: e.target.value })}
                required
              />
              <input
                type="text"
                placeholder="Zip Code"
                value={formData.zipCode}
                onChange={(e) => setFormData({ ...formData, zipCode: e.target.value })}
                required
              />
              <button type="submit">Save Address</button>
            </form>
          )}

          <div className="addresses-list">
            {addresses.map(address => (
              <div key={address.id} className="address-card">
                <p><strong>{address.street}</strong></p>
                <p>{address.city}, {address.state} - {address.zipCode}</p>
                <p>{address.country}</p>
                {address.isDefault && <span className="default-badge">Default</span>}
              </div>
            ))}
          </div>
        </div>
      </div>
    </div>
  );
};

export default Profile;

