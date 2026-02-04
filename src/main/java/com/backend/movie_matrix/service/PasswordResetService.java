package com.backend.movie_matrix.service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Random;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.backend.movie_matrix.entity.PasswordResetOtp;
import com.backend.movie_matrix.entity.User;
import com.backend.movie_matrix.exception.PasswordResetExceptions.OtpExpiredException;
import com.backend.movie_matrix.exception.PasswordResetExceptions.ResendOtpTooSoonException;
import com.backend.movie_matrix.exception.UserNotFoundException;
import com.backend.movie_matrix.repository.ResendOtpRepo;
import com.backend.movie_matrix.repository.UserRepo;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class PasswordResetService {

    private final UserRepo userRepo;
    private final EmailService emailService;
    private final ResendOtpRepo otpRepo;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    private static final int OTP_EXPIRY_MIN = 5;
    private static final int RESEND_COOLDOWN_SECONDS = 30;

    @Transactional
    public void sendOtp(String email) {
        log.info("📧 Sending OTP to: {}", email);

        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User with email " + email + " not found"));

        otpRepo.deleteByEmail(email);

        String otp = String.format("%06d", new Random().nextInt(1000000));

        PasswordResetOtp otpEntity = new PasswordResetOtp();
        otpEntity.setEmail(email);
        otpEntity.setCreatedAt(LocalDateTime.now());
        otpEntity.setExpiresAt(LocalDateTime.now().plusMinutes(OTP_EXPIRY_MIN));
        otpEntity.setLastSent(LocalDateTime.now());
        otpEntity.setOtp(otp);
        otpEntity.setVerified(false);
        otpEntity.setResendCount(0);

        otpRepo.save(otpEntity);

        emailService.sendOtpMail(email, otp);

        log.info("✅ OTP sent successfully to: {}", email);
    }

    @Transactional
    public void resendOtp(String email) {

        log.info("🔄 Resending OTP to: {}", email);

        userRepo.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User with email " + email + " not found"));

        PasswordResetOtp existingOtp = otpRepo
                .findByEmailAndVerifiedFalseAndExpiresAtAfter(email, LocalDateTime.now())
                .orElse(null);

        if (existingOtp != null) {
            long secondSinceLastSend = Duration.between(
                    existingOtp.getLastSent(),
                    LocalDateTime.now()).getSeconds();

            if (secondSinceLastSend < RESEND_COOLDOWN_SECONDS) {
                long remaining = RESEND_COOLDOWN_SECONDS - secondSinceLastSend;
                throw new ResendOtpTooSoonException(
                        "Please wait " + remaining + " seconds before resending OTP");
            }
        }

        otpRepo.deleteByEmail(email);

        String newOtp = String.format("%06d", new Random().nextInt(1000000));

        PasswordResetOtp otpEntity = new PasswordResetOtp();
        otpEntity.setEmail(email);
        otpEntity.setOtp(newOtp);
        otpEntity.setCreatedAt(LocalDateTime.now());
        otpEntity.setExpiresAt(LocalDateTime.now().plusMinutes(OTP_EXPIRY_MIN));
        otpEntity.setLastSent(LocalDateTime.now());
        otpEntity.setVerified(false);
        otpEntity.setResendCount(existingOtp != null ? existingOtp.getResendCount() + 1 : 1);

        otpRepo.save(otpEntity);

        emailService.sendOtpMail(email, newOtp);

        log.info("✅ OTP resent successfully to: {}", email);

    }

    @Transactional
    public void verifyOtp(String email, String otp) {

        log.info("🔍 Verifying OTP for: {}", email);

        PasswordResetOtp otpEntity = otpRepo
                .findByEmailAndOtpAndVerifiedFalse(email, otp)
                .orElse(null);

        if (otpEntity.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new OtpExpiredException("OTP has expired. Please request a new one.");
        }

        otpEntity.setVerified(true);
        otpRepo.save(otpEntity);

        log.info("✅ OTP verified successfully for: {}", email);

    }

    @Transactional
    public void resetPassword(String email, String newPassword) {
        log.info("🔐 Resetting password for: {}", email);

        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        String hashedPassword = encoder.encode(newPassword);
        user.setPassword(hashedPassword);
        userRepo.save(user);

        otpRepo.deleteByEmail(email);

        log.info("✅ Password reset successfully for: {}", email);
    }

    public boolean canResendOtp(String email) {
        PasswordResetOtp existingOtp = otpRepo
                .findByEmailAndVerifiedFalseAndExpiresAtAfter(email, LocalDateTime.now())
                .orElse(null);

        if (existingOtp == null) {
            return true;
        }

        long secondsSinceLastSend = Duration.between(
                existingOtp.getLastSent(),
                LocalDateTime.now()).getSeconds();

        return secondsSinceLastSend >= RESEND_COOLDOWN_SECONDS;
    }

    public long getRemainingSecondsForResend(String email) {
        PasswordResetOtp existingOtp = otpRepo
                .findByEmailAndVerifiedFalseAndExpiresAtAfter(email, LocalDateTime.now())
                .orElse(null);

        if (existingOtp == null) {
            return 0;
        }

        long secondsSinceLastSend = Duration.between(
                existingOtp.getLastSent(),
                LocalDateTime.now()).getSeconds();

        long remaining = RESEND_COOLDOWN_SECONDS - secondsSinceLastSend;
        return Math.max(0, remaining);
    }

}
