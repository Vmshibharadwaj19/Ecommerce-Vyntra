import React, { useState, useEffect } from 'react';
import * as adminAPI from '../../api/admin';
import './ManageSellers.css';

const ManageSellers = () => {
  const [pendingSellers, setPendingSellers] = useState([]);
  const [allSellers, setAllSellers] = useState([]);
  const [loading, setLoading] = useState(true);
  const [message, setMessage] = useState('');
  const [activeTab, setActiveTab] = useState('pending'); // 'pending' or 'all'

  useEffect(() => {
    fetchSellers();
  }, [activeTab]);

  const fetchSellers = async () => {
    try {
      setLoading(true);
      if (activeTab === 'pending') {
        const response = await adminAPI.getPendingSellers();
        setPendingSellers(response.data.data || []);
      } else {
        // Fetch all sellers (we'll need to add this endpoint or filter from all users)
        const response = await adminAPI.getAllUsers();
        const sellers = response.data.data?.filter(user => user.role === 'ROLE_SELLER') || [];
        setAllSellers(sellers);
      }
      setLoading(false);
    } catch (error) {
      console.error('Error fetching sellers:', error);
      setMessage('Error fetching sellers');
      setLoading(false);
    }
  };

  const handleApprove = async (sellerId) => {
    try {
      await adminAPI.approveSeller(sellerId);
      setMessage('Seller approved successfully! They will be notified via email.');
      fetchSellers(); // Refresh list
      setTimeout(() => setMessage(''), 3000);
    } catch (error) {
      console.error('Error approving seller:', error);
      setMessage('Error approving seller: ' + (error.response?.data?.message || error.message));
    }
  };

  const handleReject = async (sellerId) => {
    if (!window.confirm('Are you sure you want to reject this seller? They will be notified via email.')) {
      return;
    }
    try {
      await adminAPI.rejectSeller(sellerId);
      setMessage('Seller rejected. They will be notified via email.');
      fetchSellers(); // Refresh list
      setTimeout(() => setMessage(''), 3000);
    } catch (error) {
      console.error('Error rejecting seller:', error);
      setMessage('Error rejecting seller: ' + (error.response?.data?.message || error.message));
    }
  };

  const formatDate = (dateString) => {
    if (!dateString) return 'N/A';
    return new Date(dateString).toLocaleDateString('en-IN', {
      year: 'numeric',
      month: 'short',
      day: 'numeric'
    });
  };

  if (loading) {
    return <div className="manage-sellers-loading">Loading sellers...</div>;
  }

  const sellersToShow = activeTab === 'pending' ? pendingSellers : allSellers;

  return (
    <div className="manage-sellers">
      <div className="container">
        <h2>Manage Sellers</h2>
        
        {message && (
          <div className={`message ${message.includes('Error') ? 'error' : 'success'}`}>
            {message}
          </div>
        )}

        <div className="tabs">
          <button 
            className={`tab ${activeTab === 'pending' ? 'active' : ''}`}
            onClick={() => setActiveTab('pending')}
          >
            Pending Approval ({pendingSellers.length})
          </button>
          <button 
            className={`tab ${activeTab === 'all' ? 'active' : ''}`}
            onClick={() => setActiveTab('all')}
          >
            All Sellers ({allSellers.length})
          </button>
        </div>

        {activeTab === 'pending' && pendingSellers.length === 0 ? (
          <div className="no-sellers">
            <p>✅ No pending sellers for approval.</p>
            <p>All sellers are approved!</p>
          </div>
        ) : sellersToShow.length === 0 ? (
          <div className="no-sellers">
            <p>No sellers found.</p>
          </div>
        ) : (
          <div className="sellers-list">
            <table className="sellers-table">
              <thead>
                <tr>
                  <th>ID</th>
                  <th>Name</th>
                  <th>Email</th>
                  <th>Business Name</th>
                  <th>GST Number</th>
                  <th>PAN Number</th>
                  <th>Phone</th>
                  <th>Registered</th>
                  <th>Status</th>
                  <th>Actions</th>
                </tr>
              </thead>
              <tbody>
                {sellersToShow.map(seller => (
                  <tr key={seller.id}>
                    <td>{seller.id}</td>
                    <td>
                      <strong>{seller.firstName} {seller.lastName}</strong>
                    </td>
                    <td>{seller.email}</td>
                    <td>{seller.businessName || 'N/A'}</td>
                    <td>{seller.gstNumber || 'N/A'}</td>
                    <td>{seller.panNumber || 'N/A'}</td>
                    <td>{seller.phoneNumber || 'N/A'}</td>
                    <td>{formatDate(seller.createdAt)}</td>
                    <td>
                      {seller.isApproved ? (
                        <span className="status-badge approved">✅ Approved</span>
                      ) : (
                        <span className="status-badge pending">⏳ Pending</span>
                      )}
                    </td>
                    <td>
                      {!seller.isApproved ? (
                        <div className="action-buttons">
                          <button 
                            onClick={() => handleApprove(seller.id)}
                            className="btn-approve"
                          >
                            Approve
                          </button>
                          <button 
                            onClick={() => handleReject(seller.id)}
                            className="btn-reject"
                          >
                            Reject
                          </button>
                        </div>
                      ) : (
                        <span className="no-action">-</span>
                      )}
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        )}

        <div className="info-box">
          <h3>ℹ️ Seller Approval Process</h3>
          <ol>
            <li><strong>Seller Registration:</strong> New sellers register with business details (GST, PAN, Business Name)</li>
            <li><strong>Pending Status:</strong> Sellers start with <span className="status-badge pending">⏳ Pending</span> status</li>
            <li><strong>Admin Review:</strong> Admin reviews seller details and business information</li>
            <li><strong>Admin Approval:</strong> Admin clicks "Approve" → Seller receives approval notification</li>
            <li><strong>Seller Can Add Products:</strong> After approval, seller can add products (products still need admin approval)</li>
          </ol>
          <p><strong>Note:</strong> Approved sellers can add products, but each product still requires admin approval before appearing on customer dashboard.</p>
        </div>
      </div>
    </div>
  );
};

export default ManageSellers;
