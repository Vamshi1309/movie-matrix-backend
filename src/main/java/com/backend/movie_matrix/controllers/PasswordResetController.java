package com.backend.movie_matrix.controllers;

import javax.management.RuntimeErrorException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.backend.movie_matrix.dto.ApiResponse;
import com.backend.movie_matrix.dto.ForgotPasswordRequest;
import com.backend.movie_matrix.dto.ResendOtpRequest;
import com.backend.movie_matrix.dto.ResetPasswordRequest;
import com.backend.movie_matrix.dto.VerifyOtpRequest;
import com.backend.movie_matrix.exception.UserNotFoundException;
import com.backend.movie_matrix.exception.PasswordResetExceptions.InvalidOtpException;
import com.backend.movie_matrix.exception.PasswordResetExceptions.OtpExpiredException;
import com.backend.movie_matrix.exception.PasswordResetExceptions.ResendOtpTooSoonException;
import com.backend.movie_matrix.service.PasswordResetService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/auth/reset-password")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class PasswordResetController {

    private final PasswordResetService passwordResetService;

    @PostMapping("/send-otp")
    public ResponseEntity<ApiResponse<Object>> sendOtp(@Valid @RequestBody ForgotPasswordRequest request) {

        try {
            passwordResetService.sendOtp(request.getEmail());

            ApiResponse<Object> res = new ApiResponse<>(
                    true,
                    "OTP sent successfully to " + request.getEmail(),
                    "expiresIn 5 minutes");

            return ResponseEntity.ok(res);

        } catch (UserNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw e;
        }
    }

    @PostMapping("/resend-otp")
    public ResponseEntity<ApiResponse<Object>> resendOtp(@Valid @RequestBody ResendOtpRequest request) {
        try {
            if (!passwordResetService.canResendOtp(request.getEmail())) {
                long remaining = passwordResetService.getRemainingSecondsForResend(request.getEmail());

                ApiResponse<Object> response = new ApiResponse<Object>(
                        false,
                        "Please wait " + remaining + " seconds before resending OTP",
                        remaining);

                return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body(response);
            }

            passwordResetService.resendOtp(request.getEmail());

            ApiResponse<Object> response = new ApiResponse<Object>(
                    true,
                    "OTP resent successfully",
                    "expiresIn 5 minutes");

            return ResponseEntity.ok(response);
        } catch (UserNotFoundException e) {
            throw e;
        } catch (ResendOtpTooSoonException e) {
            throw e;
        } catch (Exception e) {
            throw e;
        }
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<ApiResponse<Object>> verifyOtp(@Valid @RequestBody VerifyOtpRequest request) {
        try {

            passwordResetService.verifyOtp(request.getEmail(), request.getOtp());

            ApiResponse<Object> response = new ApiResponse<Object>(
                    true,
                    "OTP verified successfully",
                    "email : " + request.getEmail());

            return ResponseEntity.ok(response);

        } catch (InvalidOtpException | OtpExpiredException e) {
            throw e;
        } catch (Exception e) {
            throw e;
        }
    }

    @PostMapping("/reset")
    public ResponseEntity<ApiResponse<Object>> resetPassword(@Valid @RequestBody ResetPasswordRequest request) {
        try {
            passwordResetService.resetPassword(request.getEmail(), request.getNewPassword());

            ApiResponse<Object> response = new ApiResponse<Object>(true, "Password reset successfully", null);

            return ResponseEntity.ok(response);

        } catch (UserNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw  e;
        }
    }

    @GetMapping("/can-resend")
    public ResponseEntity<ApiResponse<Object>> canResendOtp(@RequestParam String email) {
        boolean canResend = passwordResetService.canResendOtp(email);
        long remainingSeconds = passwordResetService.getRemainingSecondsForResend(email);

        ApiResponse<Object> response = new ApiResponse<Object>(
                canResend,
                String.valueOf(remainingSeconds),
                null);

        return ResponseEntity.ok(response);
    }
}
