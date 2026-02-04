package com.backend.movie_matrix.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

    public List<Movie> getMoviesByCategory(String categoryName) {
        return movieRepo.findMoviesByCategory(categoryName, null).getContent();
    }

    public List<Movie> getTrendingMovies() {
        return movieRepo.findMoviesByCategory("trending", null).getContent();
    }

    public Page<Movie> getTopRatedMovies(int page, int limit) {
        Pageable pageable = PageRequest.of(page, limit, Sort.by("rating").descending());
        return movieRepo.findMoviesByCategory("top_rated", pageable);
    }

    public List<Movie> getForYouMovies() {
        return movieRepo.findMoviesByCategory("for_you", null).getContent();
    }

    public Page<Movie> getPopularMovies(int page, int limit) {
        Pageable pageable = PageRequest.of(page, limit);
        return movieRepo.findMoviesByCategory("popular", pageable);
    }

    public Page<Movie> getNowPlayingMovies(int page, int limit) {
        Pageable pageable = PageRequest.of(page, limit, Sort.by("releaseYear").descending());
        return movieRepo.findMoviesByCategory("now_playing", pageable);
    }
}
