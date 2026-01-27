package com.backend.movie_matrix.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.backend.movie_matrix.dto.ApiResponse;
import com.backend.movie_matrix.dto.MovieDto;
import com.backend.movie_matrix.dto.SearchDataResponse;
import com.backend.movie_matrix.exception.MoviesNotFoundException;
import com.backend.movie_matrix.service.SearchService;

@RestController
@RequestMapping("/api/search")
@CrossOrigin(origins = "*")
public class SearchController {

    @Autowired
    private SearchService searchService;

    @GetMapping("/data")
    public ResponseEntity<ApiResponse<SearchDataResponse>> getSearchData(@RequestParam Long userId) {
        SearchDataResponse data = searchService.getSearchData(userId);

        if (data == null ||
                (data.getRecentSearches().isEmpty() &&
                        data.getPopularMovies().isEmpty())) {

            throw new MoviesNotFoundException("No Search Data Found");
        }

        ApiResponse<SearchDataResponse> response = new ApiResponse<>(
                true,
                "Search Data Fetched Successfully",
                data);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/suggestions")
    public ResponseEntity<ApiResponse<List<MovieDto>>> getSuggestions(@RequestParam String query) {
        List<MovieDto> suggestions = searchService.getSuggestions(query);

        if (suggestions.isEmpty()) {
            throw new MoviesNotFoundException("No Suggestions Found");
        }

        ApiResponse<List<MovieDto>> response = new ApiResponse<>(
                true,
                "Suggestions Fetched Successfully",
                suggestions);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/movie")
    public ResponseEntity<ApiResponse<MovieDto>> getMovieByTitle(
            @RequestParam String title,
            @RequestParam Long userId) {
        MovieDto movie = searchService.getMovieByTitle(userId, title);

        if (movie == null) {
            throw new MoviesNotFoundException("Movie Not Found");
        }

        ApiResponse<MovieDto> response = new ApiResponse<>(
                true,
                "Movie Fetched Successfully",
                movie);
        return ResponseEntity.ok(response);
    }

}
