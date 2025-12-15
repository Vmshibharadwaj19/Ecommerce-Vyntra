package com.ecommerce.repositories;

import com.ecommerce.entities.PasswordResetOtp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface PasswordResetOtpRepository extends JpaRepository<PasswordResetOtp, Long> {

    Optional<PasswordResetOtp> findByEmailAndOtpAndIsUsedFalse(String email, String otp);

    @Modifying
    @Query("DELETE FROM PasswordResetOtp p WHERE p.expiresAt < :now OR p.isUsed = true")
    void deleteExpiredOtps(@Param("now") LocalDateTime now);

    @Modifying
    @Query("UPDATE PasswordResetOtp p SET p.isUsed = true WHERE p.email = :email")
    void markAsUsed(@Param("email") String email);
}

