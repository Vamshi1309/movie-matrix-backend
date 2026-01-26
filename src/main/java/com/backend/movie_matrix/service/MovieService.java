package com.backend.movie_matrix.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.backend.movie_matrix.entity.Movie;
import com.backend.movie_matrix.repository.MovieRepo;

@Service
public class MovieService {

    @Autowired
    private MovieRepo movieRepo;

    public List<Movie> getAllMovies() {
        return movieRepo.findAll();
    }

    public List<Movie> getMoviesByCategory(String categoryName){
        return movieRepo.findMoviesByCategory(categoryName);
    }

    public List<Movie> getTrendingMovies() {
        return movieRepo.findMoviesByCategory("TRENDING");
    }
    
    public List<Movie> getTopRatedMovies() {
        return movieRepo.findMoviesByCategory("TOP_RATED");
    }
    
    public List<Movie> getForYouMovies() {
        return movieRepo.findMoviesByCategory("FOR_YOU");
    }

    public List<Movie> getPopularMovies() {
        return movieRepo.findMoviesByCategory("popular");
    }

    public List<Movie> getNowPlayingMovies() {
        return movieRepo.findMoviesByCategory("now_playing");
    }
}
