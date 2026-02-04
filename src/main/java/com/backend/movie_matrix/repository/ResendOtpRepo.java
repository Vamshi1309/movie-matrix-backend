package com.backend.movie_matrix.repository;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.backend.movie_matrix.entity.PasswordResetOtp;

public interface ResendOtpRepo extends JpaRepository<PasswordResetOtp, Long> {

    Optional<PasswordResetOtp> findByEmailAndVerifiedFalseAndExpiresAtAfter(
            String email,
            LocalDateTime currentTime);

    Optional<PasswordResetOtp> findByEmailAndOtpAndVerifiedFalse(
            String email,
            String otp);

    void deleteByEmail(String email);

}
