import React, { useState, useEffect } from 'react';
import * as adminAPI from '../../api/admin';
import './ManageProducts.css';

const ManageProducts = () => {
  const [pendingProducts, setPendingProducts] = useState([]);
  const [loading, setLoading] = useState(true);
  const [message, setMessage] = useState('');

  useEffect(() => {
    fetchPendingProducts();
  }, []);

  const fetchPendingProducts = async () => {
    try {
      setLoading(true);
      const response = await adminAPI.getPendingProducts();
      setPendingProducts(response.data.data);
      setLoading(false);
    } catch (error) {
      console.error('Error fetching pending products:', error);
      setMessage('Error fetching pending products');
      setLoading(false);
    }
  };

  const handleApprove = async (productId) => {
    try {
      await adminAPI.approveProduct(productId);
      setMessage('Product approved successfully!');
      fetchPendingProducts(); // Refresh list
    } catch (error) {
      console.error('Error approving product:', error);
      setMessage('Error approving product');
    }
  };

  const handleReject = async (productId) => {
    if (!window.confirm('Are you sure you want to reject this product?')) {
      return;
    }
    try {
      await adminAPI.rejectProduct(productId);
      setMessage('Product rejected successfully!');
      fetchPendingProducts(); // Refresh list
    } catch (error) {
      console.error('Error rejecting product:', error);
      setMessage('Error rejecting product');
    }
  };

  const handleApproveAll = async () => {
    if (!window.confirm(`Are you sure you want to approve all ${pendingProducts.length} pending products?`)) {
      return;
    }
    try {
      setLoading(true);
      const response = await fetch('http://localhost:8080/api/admin/products/approve-all', {
        method: 'PUT',
        headers: {
          'Authorization': `Bearer ${sessionStorage.getItem('token')}`,
          'Content-Type': 'application/json'
        }
      });
      const data = await response.json();
      setMessage(data.message || 'All products approved successfully!');
      fetchPendingProducts(); // Refresh list
      setLoading(false);
    } catch (error) {
      console.error('Error approving all products:', error);
      setMessage('Error approving all products');
      setLoading(false);
    }
  };

  if (loading) {
    return <div className="loading">Loading pending products...</div>;
  }

  return (
    <div className="manage-products">
      <div className="container">
        <h2>Manage Products - Pending Approval</h2>
        
        {message && (
          <div className={`message ${message.includes('Error') ? 'error' : 'success'}`}>
            {message}
          </div>
        )}

        {pendingProducts.length === 0 ? (
          <div className="no-products">
            <p>No pending products for approval.</p>
            <p>All products are approved!</p>
          </div>
        ) : (
          <div className="products-list">
            <table className="products-table">
              <thead>
                <tr>
                  <th>ID</th>
                  <th>Name</th>
                  <th>Seller</th>
                  <th>Category</th>
                  <th>Price</th>
                  <th>Stock</th>
                  <th>Actions</th>
                </tr>
              </thead>
              <tbody>
                {pendingProducts.map(product => (
                  <tr key={product.id}>
                    <td>{product.id}</td>
                    <td>
                      <strong>{product.name}</strong>
                      {product.brand && <div className="brand">{product.brand}</div>}
                    </td>
                    <td>{product.sellerName || 'N/A'}</td>
                    <td>{product.categoryName || 'N/A'}</td>
                    <td>
                      ₹{product.discountPrice?.toLocaleString('en-IN')}
                      {product.price > product.discountPrice && (
                        <span className="original-price"> (was ₹{product.price?.toLocaleString('en-IN')})</span>
                      )}
                    </td>
                    <td>{product.stockQuantity || 0}</td>
                    <td>
                      <div className="action-buttons">
                        <button 
                          onClick={() => handleApprove(product.id)}
                          className="btn-approve"
                        >
                          Approve
                        </button>
                        <button 
                          onClick={() => handleReject(product.id)}
                          className="btn-reject"
                        >
                          Reject
                        </button>
                      </div>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        )}

        {pendingProducts.length > 0 && (
          <div className="bulk-actions">
            <button 
              onClick={handleApproveAll}
              className="btn-approve-all"
            >
              Approve All Pending Products
            </button>
          </div>
        )}

        <div className="info-box">
          <h3>ℹ️ Information</h3>
          <p><strong>All products created by sellers require admin approval</strong> before they appear on the customer dashboard.</p>
          <p>Review each product and click "Approve" to make it visible to customers, or "Reject" to decline it.</p>
          <p><strong>Bulk Action:</strong> Click "Approve All" above to approve all pending products at once.</p>
        </div>
      </div>
    </div>
  );
};

export default ManageProducts;
