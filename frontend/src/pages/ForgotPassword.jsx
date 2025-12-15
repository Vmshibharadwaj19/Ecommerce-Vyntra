import React, { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { useMutation } from '@tanstack/react-query';
import toast from 'react-hot-toast';
import * as authAPI from '../api/auth';
import './ForgotPassword.css';

const ForgotPassword = () => {
  const navigate = useNavigate();
  const [step, setStep] = useState(1); // 1: email, 2: OTP, 3: reset password
  const [email, setEmail] = useState('');
  const [otp, setOtp] = useState('');
  const [newPassword, setNewPassword] = useState('');
  const [confirmPassword, setConfirmPassword] = useState('');

  const forgotPasswordMutation = useMutation({
    mutationFn: (email) => authAPI.forgotPassword(email),
    onSuccess: () => {
      toast.success('OTP sent to your email!');
      setStep(2);
    },
    onError: (error) => {
      toast.error(error.response?.data?.message || 'Failed to send OTP');
    },
  });

  const verifyOtpMutation = useMutation({
    mutationFn: ({ email, otp }) => authAPI.verifyOtp(email, otp),
    onSuccess: () => {
      toast.success('OTP verified!');
      setStep(3);
    },
    onError: (error) => {
      toast.error(error.response?.data?.message || 'Invalid or expired OTP');
    },
  });

  const resetPasswordMutation = useMutation({
    mutationFn: ({ email, otp, newPassword }) => 
      authAPI.resetPassword(email, otp, newPassword),
    onSuccess: () => {
      toast.success('Password reset successfully!');
      navigate('/login');
    },
    onError: (error) => {
      toast.error(error.response?.data?.message || 'Failed to reset password');
    },
  });

  const handleEmailSubmit = (e) => {
    e.preventDefault();
    if (!email) {
      toast.error('Please enter your email');
      return;
    }
    forgotPasswordMutation.mutate(email);
  };

  const handleOtpSubmit = (e) => {
    e.preventDefault();
    if (!otp || otp.length !== 6) {
      toast.error('Please enter a valid 6-digit OTP');
      return;
    }
    verifyOtpMutation.mutate({ email, otp });
  };

  const handleResetSubmit = (e) => {
    e.preventDefault();
    if (!newPassword || newPassword.length < 6) {
      toast.error('Password must be at least 6 characters');
      return;
    }
    if (newPassword !== confirmPassword) {
      toast.error('Passwords do not match');
      return;
    }
    resetPasswordMutation.mutate({ email, otp, newPassword });
  };

  return (
    <div className="forgot-password-container">
      <div className="forgot-password-card">
        <div className="forgot-password-header">
          <h2>Reset Password</h2>
          <p>Follow the steps to reset your password</p>
        </div>

        {step === 1 && (
          <form onSubmit={handleEmailSubmit} className="forgot-password-form">
            <div className="form-group">
              <label htmlFor="email">Email Address</label>
              <input
                type="email"
                id="email"
                value={email}
                onChange={(e) => setEmail(e.target.value)}
                placeholder="Enter your email"
                required
                disabled={forgotPasswordMutation.isPending}
              />
            </div>
            <button
              type="submit"
              className="btn-primary"
              disabled={forgotPasswordMutation.isPending}
            >
              {forgotPasswordMutation.isPending ? 'Sending...' : 'Send OTP'}
            </button>
            <Link to="/login" className="back-to-login">
              Back to Login
            </Link>
          </form>
        )}

        {step === 2 && (
          <form onSubmit={handleOtpSubmit} className="forgot-password-form">
            <div className="form-group">
              <label htmlFor="otp">Enter OTP</label>
              <input
                type="text"
                id="otp"
                value={otp}
                onChange={(e) => setOtp(e.target.value.replace(/\D/g, '').slice(0, 6))}
                placeholder="Enter 6-digit OTP"
                maxLength="6"
                required
                disabled={verifyOtpMutation.isPending}
                className="otp-input"
              />
              <p className="otp-hint">Check your email for the OTP code</p>
            </div>
            <button
              type="submit"
              className="btn-primary"
              disabled={verifyOtpMutation.isPending}
            >
              {verifyOtpMutation.isPending ? 'Verifying...' : 'Verify OTP'}
            </button>
            <button
              type="button"
              className="btn-secondary"
              onClick={() => {
                setStep(1);
                setOtp('');
              }}
            >
              Change Email
            </button>
          </form>
        )}

        {step === 3 && (
          <form onSubmit={handleResetSubmit} className="forgot-password-form">
            <div className="form-group">
              <label htmlFor="newPassword">New Password</label>
              <input
                type="password"
                id="newPassword"
                value={newPassword}
                onChange={(e) => setNewPassword(e.target.value)}
                placeholder="Enter new password"
                required
                minLength="6"
                disabled={resetPasswordMutation.isPending}
              />
            </div>
            <div className="form-group">
              <label htmlFor="confirmPassword">Confirm Password</label>
              <input
                type="password"
                id="confirmPassword"
                value={confirmPassword}
                onChange={(e) => setConfirmPassword(e.target.value)}
                placeholder="Confirm new password"
                required
                minLength="6"
                disabled={resetPasswordMutation.isPending}
              />
            </div>
            <button
              type="submit"
              className="btn-primary"
              disabled={resetPasswordMutation.isPending}
            >
              {resetPasswordMutation.isPending ? 'Resetting...' : 'Reset Password'}
            </button>
          </form>
        )}
      </div>
    </div>
  );
};

export default ForgotPassword;

