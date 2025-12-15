import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import * as cartAPI from '../api/cart';
import * as addressAPI from '../api/address';
import * as paymentAPI from '../api/payment';
import * as orderAPI from '../api/order';
import './Checkout.css';

const Checkout = () => {
  const { isAuthenticated } = useAuth();
  const navigate = useNavigate();
  const [cart, setCart] = useState(null);
  const [addresses, setAddresses] = useState([]);
  const [selectedAddress, setSelectedAddress] = useState(null);
  const [paymentMethod, setPaymentMethod] = useState('COD'); // COD, RAZORPAY, TEST
  const [loading, setLoading] = useState(true);
  const [processing, setProcessing] = useState(false);

  useEffect(() => {
    if (!isAuthenticated()) {
      navigate('/login');
      return;
    }
    fetchData();
  }, []);

  const fetchData = async () => {
    try {
      const [cartResponse, addressesResponse] = await Promise.all([
        cartAPI.getCart(),
        addressAPI.getUserAddresses()
      ]);
      setCart(cartResponse.data.data);
      setAddresses(addressesResponse.data.data);
      if (addressesResponse.data.data.length > 0) {
        const defaultAddress = addressesResponse.data.data.find(a => a.isDefault) 
          || addressesResponse.data.data[0];
        setSelectedAddress(defaultAddress);
      }
      setLoading(false);
    } catch (error) {
      console.error('Error fetching data:', error);
      setLoading(false);
    }
  };

  const handleRazorpayPayment = async () => {
    setProcessing(true);
    try {
      const orderRequest = {
        amount: cart.totalAmount,
        currency: 'INR',
        receipt: `receipt_${Date.now()}`,
        addressId: selectedAddress.id
      };

      const orderResponse = await paymentAPI.createRazorpayOrder(orderRequest);
      const { orderId, amount, currency, key } = orderResponse.data.data;

      const options = {
        key: key,
        amount: (parseFloat(amount) * 100).toString(),
        currency: currency,
        name: 'VYNTRA',
        description: 'Order Payment',
        order_id: orderId,
        handler: async function (response) {
          try {
            const checkoutRequest = {
              addressId: selectedAddress.id,
              paymentMethod: 'RAZORPAY',
              amount: cart.totalAmount,
              razorpayOrderId: response.razorpay_order_id,
              razorpayPaymentId: response.razorpay_payment_id,
              razorpaySignature: response.razorpay_signature
            };

            await orderAPI.checkout(checkoutRequest);
            navigate('/orders');
          } catch (error) {
            console.error('Payment verification failed:', error);
            alert('Payment verification failed: ' + (error.response?.data?.message || error.message));
          }
        },
        prefill: {
          name: 'Customer',
          email: 'customer@example.com'
        },
        theme: {
          color: '#ff9900'
        }
      };

      const razorpay = new window.Razorpay(options);
      razorpay.open();
      
      razorpay.on('payment.failed', function (response) {
        console.error('Payment failed:', response.error);
        alert('Payment failed: ' + (response.error.description || response.error.reason || 'Unknown error'));
        setProcessing(false);
      });
    } catch (error) {
      console.error('Error processing payment:', error);
      let errorMessage = 'Error processing payment';
      
      if (error.response?.data?.message) {
        errorMessage = error.response.data.message;
      } else if (error.message) {
        errorMessage = error.message;
      }
      
      alert(errorMessage);
      setProcessing(false);
    }
  };

  const handleCOD = async () => {
    if (!selectedAddress) {
      alert('Please select a delivery address');
      return;
    }

    setProcessing(true);
    try {
      const checkoutRequest = {
        addressId: selectedAddress.id,
        paymentMethod: 'COD',
        amount: cart.totalAmount
      };

      await orderAPI.checkout(checkoutRequest);
      alert('Order placed successfully! You will pay on delivery.');
      navigate('/orders');
    } catch (error) {
      console.error('Error placing COD order:', error);
      alert('Error placing order: ' + (error.response?.data?.message || error.message));
      setProcessing(false);
    }
  };

  const handleTestPayment = async () => {
    if (!selectedAddress) {
      alert('Please select a delivery address');
      return;
    }

    setProcessing(true);
    try {
      const checkoutRequest = {
        addressId: selectedAddress.id,
        paymentMethod: 'TEST',
        amount: cart.totalAmount
      };

      await orderAPI.checkout(checkoutRequest);
      alert('Test order placed successfully!');
      navigate('/orders');
    } catch (error) {
      console.error('Error placing test order:', error);
      alert('Error placing order: ' + (error.response?.data?.message || error.message));
      setProcessing(false);
    }
  };

  const handlePlaceOrder = () => {
    if (!selectedAddress) {
      alert('Please select a delivery address');
      return;
    }

    if (paymentMethod === 'COD') {
      handleCOD();
    } else if (paymentMethod === 'TEST') {
      handleTestPayment();
    } else if (paymentMethod === 'RAZORPAY') {
      handleRazorpayPayment();
    }
  };

  if (loading) {
    return <div className="loading">Loading...</div>;
  }

  return (
    <div className="checkout-page">
      <div className="checkout-container">
        <h2>Checkout</h2>
        
        <div className="checkout-content">
          <div className="checkout-left">
            <div className="address-section">
              <h3>Delivery Address</h3>
              {addresses.map(address => (
                <div 
                  key={address.id} 
                  className={`address-card ${selectedAddress?.id === address.id ? 'selected' : ''}`}
                  onClick={() => setSelectedAddress(address)}
                >
                  <p><strong>{address.street}</strong></p>
                  <p>{address.city}, {address.state} - {address.zipCode}</p>
                  <p>{address.country}</p>
                </div>
              ))}
              <button onClick={() => navigate('/profile')} className="add-address-button">
                Add New Address
              </button>
            </div>

            <div className="payment-method-section">
              <h3>Payment Method</h3>
              <div className="payment-options">
                <label className={`payment-option ${paymentMethod === 'COD' ? 'selected' : ''}`}>
                  <input
                    type="radio"
                    name="paymentMethod"
                    value="COD"
                    checked={paymentMethod === 'COD'}
                    onChange={(e) => setPaymentMethod(e.target.value)}
                  />
                  <div className="payment-option-content">
                    <span className="payment-option-name">Cash on Delivery (COD)</span>
                    <span className="payment-option-desc">Pay when you receive</span>
                  </div>
                </label>

                <label className={`payment-option ${paymentMethod === 'TEST' ? 'selected' : ''}`}>
                  <input
                    type="radio"
                    name="paymentMethod"
                    value="TEST"
                    checked={paymentMethod === 'TEST'}
                    onChange={(e) => setPaymentMethod(e.target.value)}
                  />
                  <div className="payment-option-content">
                    <span className="payment-option-name">Test Payment</span>
                    <span className="payment-option-desc">Demo payment (no API keys needed)</span>
                  </div>
                </label>

                <label className={`payment-option ${paymentMethod === 'RAZORPAY' ? 'selected' : ''}`}>
                  <input
                    type="radio"
                    name="paymentMethod"
                    value="RAZORPAY"
                    checked={paymentMethod === 'RAZORPAY'}
                    onChange={(e) => setPaymentMethod(e.target.value)}
                  />
                  <div className="payment-option-content">
                    <span className="payment-option-name">Razorpay (Online Payment)</span>
                    <span className="payment-option-desc">Credit/Debit Card, UPI, Net Banking</span>
                  </div>
                </label>
              </div>
            </div>
          </div>

          <div className="order-summary">
            <h3>Order Summary</h3>
            <div className="summary-item">
              <span>Subtotal:</span>
              <span>₹{cart?.totalAmount}</span>
            </div>
            <div className="summary-item">
              <span>Shipping:</span>
              <span>₹0</span>
            </div>
            <div className="summary-total">
              <span>Total:</span>
              <span>₹{cart?.totalAmount}</span>
            </div>
            <button 
              onClick={handlePlaceOrder} 
              disabled={processing || !selectedAddress}
              className="pay-button"
            >
              {processing ? 'Processing...' : 
               paymentMethod === 'COD' ? 'Place Order (COD)' :
               paymentMethod === 'TEST' ? 'Place Test Order' :
               'Pay Now'}
            </button>
            {paymentMethod === 'COD' && (
              <p className="cod-note">You will pay ₹{cart?.totalAmount} when you receive the order.</p>
            )}
            {paymentMethod === 'TEST' && (
              <p className="test-note">This is a test payment. No real money will be charged.</p>
            )}
          </div>
        </div>
      </div>
    </div>
  );
};

export default Checkout;
