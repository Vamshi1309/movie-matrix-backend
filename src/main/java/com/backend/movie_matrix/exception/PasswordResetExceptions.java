package com.backend.movie_matrix.exception;

public class PasswordResetExceptions {

    public static class InvalidOtpException extends RuntimeException {
        public InvalidOtpException(String msg) {
            super(msg);
        }
    }

    public static class OtpExpiredException extends RuntimeException {
        public OtpExpiredException(String message) {
            super(message);
        }
    }

    public static class ResendOtpTooSoonException extends RuntimeException {
        public ResendOtpTooSoonException(String message) {
            super(message);
        }
    }
}
