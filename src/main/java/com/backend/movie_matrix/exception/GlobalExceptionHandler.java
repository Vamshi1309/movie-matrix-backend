package com.backend.movie_matrix.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.backend.movie_matrix.dto.ApiResponse;
import com.backend.movie_matrix.exception.PasswordResetExceptions.InvalidOtpException;
import com.backend.movie_matrix.exception.PasswordResetExceptions.OtpExpiredException;
import com.backend.movie_matrix.exception.PasswordResetExceptions.ResendOtpTooSoonException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private <T> ResponseEntity<ApiResponse<T>> buildResponseEntity(String msg, HttpStatus unauthorized) {
        ApiResponse<T> response = new ApiResponse<T>(false, msg, null);
        return ResponseEntity.status(unauthorized).body(response);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiResponse<Object>> handlebadCreds(BadCredentialsException ex) {
        return buildResponseEntity("Invalid Credentails", HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ApiResponse<Object>> handleUserExists(UserAlreadyExistsException ex) {
        return buildResponseEntity(ex.getMessage(), HttpStatus.CONFLICT);
    }

    // Handle token expired
    @ExceptionHandler(TokenExpiredException.class)
    public ResponseEntity<ApiResponse<Object>> handleTokenExpired(TokenExpiredException ex) {
        return buildResponseEntity(ex.getMessage(), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ApiResponse<Object>> handleUserNotFound(UserNotFoundException ex) {
        return buildResponseEntity(ex.getMessage(), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(MoviesNotFoundException.class)
    public ResponseEntity<ApiResponse<Object>> handleMoviesNotFound(MoviesNotFoundException ex) {
        return buildResponseEntity(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MobileNumberAlreadyExistsException.class)
    public ResponseEntity<ApiResponse<Object>> handleMobileNumberAlreadyExists(MobileNumberAlreadyExistsException ex) {
        return buildResponseEntity(ex.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(InvalidOtpException.class)
    public ResponseEntity<ApiResponse<Object>> handleInvalidOtp(InvalidOtpException ex) {
        return buildResponseEntity(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(OtpExpiredException.class)
    public ResponseEntity<ApiResponse<Object>> handleOtpExpired(OtpExpiredException ex) {
        return buildResponseEntity(ex.getMessage(), HttpStatus.GONE);
    }

    @ExceptionHandler(ResendOtpTooSoonException.class)
    public ResponseEntity<ApiResponse<Object>> handleResendOtpTooSoon(ResendOtpTooSoonException ex) {
        return buildResponseEntity(ex.getMessage(), HttpStatus.TOO_MANY_REQUESTS);
    }

    // Catch all other exceptions
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Object>> handleOtherExceptions(Exception ex) {
        return buildResponseEntity(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
