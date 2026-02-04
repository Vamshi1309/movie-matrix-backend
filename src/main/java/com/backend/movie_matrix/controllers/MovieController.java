package com.backend.movie_matrix.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.backend.movie_matrix.dto.ApiResponse;
import com.backend.movie_matrix.entity.Movie;
import com.backend.movie_matrix.exception.MoviesNotFoundException;
import com.backend.movie_matrix.service.MovieService;

@RestController
@RequestMapping("/api/movies")
@CrossOrigin(origins = "*")
public class MovieController {

    @Autowired
    private MovieService movieService;

    @GetMapping("/all")
    public ResponseEntity<ApiResponse<List<Movie>>> getAllMovies() {

        List<Movie> movies = movieService.getAllMovies();

        if (movies.isEmpty()) {
            throw new MoviesNotFoundException("No Movies Found");
        }

        ApiResponse<List<Movie>> response = new ApiResponse<List<Movie>>(
                true,
                "All Movies Fetched Successfully",
                movies);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/trending")
    public ResponseEntity<ApiResponse<List<Movie>>> getTrendingMovies() {

        List<Movie> movies = movieService.getTrendingMovies();

        if (movies.isEmpty()) {
            throw new MoviesNotFoundException("No Trending Movies Found");
        }

        ApiResponse<List<Movie>> response = new ApiResponse<List<Movie>>(
                true,
                "Trending Movies Fetched Successfully",
                movies);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/top-rated")
    public ResponseEntity<ApiResponse<Page<Movie>>> getTopRatedMovies(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int limit) {

        Page<Movie> movies = movieService.getTopRatedMovies(page, limit);

        if (movies.isEmpty()) {
            throw new MoviesNotFoundException("No Top Rated Movies Found");
        }

        ApiResponse<Page<Movie>> response = new ApiResponse<>(
                true,
                "Top Rated Movies Fetched Successfully",
                movies);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/for-you")
    public ResponseEntity<ApiResponse<List<Movie>>> getForYouMovies() {

        List<Movie> movies = movieService.getForYouMovies();
        if (movies.isEmpty()) {
            throw new MoviesNotFoundException("No Movies Found for You");
        }
        ApiResponse<List<Movie>> response = new ApiResponse<List<Movie>>(
                true,
                "Movies For You Fetched Successfully",
                movies);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/popular")
    public ResponseEntity<ApiResponse<Page<Movie>>> getPopularMovies(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int limit) {
        Page<Movie> movies = movieService.getPopularMovies(page, limit);

        if (movies.isEmpty()) {
            throw new MoviesNotFoundException("No Popular Movies Found");
        }

        ApiResponse<Page<Movie>> response = new ApiResponse<>(
                true,
                "popular Movies Fetched Successfully",
                movies);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/now-playing")
    public ResponseEntity<ApiResponse<Page<Movie>>> getNowPlayingMovies(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int limit) {
        Page<Movie> movies = movieService.getNowPlayingMovies(page, limit);

        if (movies.isEmpty()) {
            throw new MoviesNotFoundException("No Movies are Now Playing");
        }

        ApiResponse<Page<Movie>> response = new ApiResponse<>(
                true,
                "Now Playing Movies Fetched Successfully",
                movies);

        return ResponseEntity.ok(response);
    }
}
