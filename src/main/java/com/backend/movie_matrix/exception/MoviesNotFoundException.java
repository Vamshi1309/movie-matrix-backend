package com.backend.movie_matrix.exception;

public class MoviesNotFoundException extends RuntimeException{
    public MoviesNotFoundException(String message){
        super(message);
    }
}
