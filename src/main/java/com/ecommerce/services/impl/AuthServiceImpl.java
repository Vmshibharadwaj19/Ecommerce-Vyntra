package com.ecommerce.services.impl;

import com.ecommerce.dto.*;
import com.ecommerce.entities.PasswordResetOtp;
import com.ecommerce.entities.User;
import com.ecommerce.exceptions.ResourceNotFoundException;
import com.ecommerce.repositories.PasswordResetOtpRepository;
import com.ecommerce.repositories.UserRepository;
import com.ecommerce.security.JwtTokenProvider;
import com.ecommerce.security.UserPrincipal;
import com.ecommerce.services.AuthService;
import com.ecommerce.services.NotificationService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Service
@Transactional
public class AuthServiceImpl implements AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private PasswordResetOtpRepository otpRepository;

    private static final int OTP_EXPIRY_MINUTES = 10;

    @Override
    public JwtResponse login(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = tokenProvider.generateToken(authentication);

        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        User user = userRepository.findById(userPrincipal.getId())
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userPrincipal.getId()));

        return new JwtResponse(
                jwt,
                "Bearer",
                user.getId(),
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                user.getRole().name()
        );
    }

    @Override
    public UserDto register(SignupRequest signupRequest) {
        if (userRepository.existsByEmail(signupRequest.getEmail())) {
            throw new RuntimeException("Email is already in use!");
        }

        if (signupRequest.getPhoneNumber() != null && 
            userRepository.existsByPhoneNumber(signupRequest.getPhoneNumber())) {
            throw new RuntimeException("Phone number is already in use!");
        }

        User user = new User();
        user.setEmail(signupRequest.getEmail());
        user.setPassword(passwordEncoder.encode(signupRequest.getPassword()));
        user.setFirstName(signupRequest.getFirstName());
        user.setLastName(signupRequest.getLastName());
        user.setPhoneNumber(signupRequest.getPhoneNumber());
        user.setRole(signupRequest.getRole() != null ? signupRequest.getRole() : User.Role.ROLE_CUSTOMER);
        user.setIsActive(true);
        user.setIsBlocked(false);

        if (user.getRole() == User.Role.ROLE_SELLER) {
            user.setBusinessName(signupRequest.getBusinessName());
            user.setGstNumber(signupRequest.getGstNumber());
            user.setPanNumber(signupRequest.getPanNumber());
            user.setIsApproved(false);
        } else if (user.getRole() == User.Role.ROLE_ADMIN) {
            user.setIsApproved(true);
        }

        User savedUser = userRepository.save(user);
        return modelMapper.map(savedUser, UserDto.class);
    }

    @Override
    public UserDto getCurrentUser() {
        UserPrincipal userPrincipal = (UserPrincipal) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
        User user = userRepository.findById(userPrincipal.getId())
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userPrincipal.getId()));
        return modelMapper.map(user, UserDto.class);
    }

    @Override
    public void forgotPassword(ForgotPasswordRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", request.getEmail()));

        // Generate 6-digit OTP
        String otp = generateOtp();

        // Delete any existing OTPs for this email
        otpRepository.deleteExpiredOtps(LocalDateTime.now());
        Optional<PasswordResetOtp> existingOtp = otpRepository.findByEmailAndOtpAndIsUsedFalse(request.getEmail(), otp);
        existingOtp.ifPresent(o -> otpRepository.delete(o));

        // Create new OTP
        PasswordResetOtp passwordResetOtp = new PasswordResetOtp();
        passwordResetOtp.setEmail(request.getEmail());
        passwordResetOtp.setOtp(otp);
        passwordResetOtp.setExpiresAt(LocalDateTime.now().plusMinutes(OTP_EXPIRY_MINUTES));
        passwordResetOtp.setIsUsed(false);
        passwordResetOtp.setCreatedAt(LocalDateTime.now());
        otpRepository.save(passwordResetOtp);

        // Send OTP email
        notificationService.sendPasswordResetOtpEmail(
                user.getEmail(),
                otp,
                user.getFirstName()
        );
    }

    @Override
    public void verifyOtp(VerifyOtpRequest request) {
        PasswordResetOtp otp = otpRepository.findByEmailAndOtpAndIsUsedFalse(request.getEmail(), request.getOtp())
                .orElseThrow(() -> new RuntimeException("Invalid or expired OTP"));

        if (otp.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("OTP has expired");
        }

        // OTP is valid, but we don't mark it as used yet - that happens in resetPassword
    }

    @Override
    public void resetPassword(ResetPasswordRequest request) {
        PasswordResetOtp otp = otpRepository.findByEmailAndOtpAndIsUsedFalse(request.getEmail(), request.getOtp())
                .orElseThrow(() -> new RuntimeException("Invalid or expired OTP"));

        if (otp.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("OTP has expired");
        }

        if (otp.getIsUsed()) {
            throw new RuntimeException("OTP has already been used");
        }

        // Find user and update password
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", request.getEmail()));

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);

        // Mark OTP as used
        otp.setIsUsed(true);
        otpRepository.save(otp);
    }

    private String generateOtp() {
        Random random = new Random();
        int otp = 100000 + random.nextInt(900000); // 6-digit OTP
        return String.valueOf(otp);
    }
}



