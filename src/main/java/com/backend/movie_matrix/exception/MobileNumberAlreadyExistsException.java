package com.backend.movie_matrix.exception;

public class MobileNumberAlreadyExistsException extends RuntimeException{
    public MobileNumberAlreadyExistsException(String message){
        super(message);
    }
}
