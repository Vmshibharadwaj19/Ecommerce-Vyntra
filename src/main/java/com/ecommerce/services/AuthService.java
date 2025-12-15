package com.ecommerce.services;

import com.ecommerce.dto.*;

public interface AuthService {

    JwtResponse login(LoginRequest loginRequest);

    UserDto register(SignupRequest signupRequest);

    UserDto getCurrentUser();

    void forgotPassword(ForgotPasswordRequest request);

    void verifyOtp(VerifyOtpRequest request);

    void resetPassword(ResetPasswordRequest request);
}



