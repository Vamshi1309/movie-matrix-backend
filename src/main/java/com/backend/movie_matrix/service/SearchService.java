package com.backend.movie_matrix.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.backend.movie_matrix.dto.MovieDto;
import com.backend.movie_matrix.dto.SearchDataResponse;
import com.backend.movie_matrix.entity.Movie;
import com.backend.movie_matrix.entity.SearchHistory;
import com.backend.movie_matrix.entity.User;
import com.backend.movie_matrix.repository.MovieRepo;
import com.backend.movie_matrix.repository.SearchHistoryRepo;
import com.backend.movie_matrix.repository.UserRepo;

@Service
public class SearchService {

    @Autowired
    private SearchHistoryRepo searchHistoryRepo;

    @Autowired
    private MovieRepo movieRepo;

    @Autowired
    private UserRepo userRepo;

    public SearchDataResponse getSearchData(Long userId) {
        List<String> recentSearches = searchHistoryRepo.findTop10RecentSearchesByUserId(userId);

        List<String> popularMovies = movieRepo.findTop10ByOrderByRatingDesc()
                .stream()
                .map(Movie::getTitle)
                .collect(Collectors.toList());

        return new SearchDataResponse(recentSearches, popularMovies);
    }

    public List<MovieDto> getSuggestions(String query) {
        if (query == null) {
            return List.of();
        }

        List<Movie> movies = movieRepo.findByTitleContainingIgnoreCase(query.trim());
        return movies.stream()
                .limit(10)
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public MovieDto getMovieByTitle(Long userId, String title) {
        Optional<Movie> movieOpt = movieRepo.findByTitleIgnoreCase(title.trim());

        if (movieOpt.isPresent()) {
            Movie movie = movieOpt.get();

            saveSearchHistory(userId, movie, title);

            return convertToDto(movie);
        }

        return null;
    }

    private void saveSearchHistory(Long userId, Movie movie, String title) {
        try {
            Optional<User> userOpt = userRepo.findById(userId);

            if (userOpt.isPresent()) {
                SearchHistory searchHistory = new SearchHistory(userOpt.get(), movie, title.trim());
                searchHistoryRepo.save(searchHistory);
            }
        } catch (Exception e) {
            System.err.println("Error saving search history: " + e.getMessage());
        }
    }

    private MovieDto convertToDto(Movie movie) {
        return new MovieDto(
                movie.getId(),
                movie.getTitle(),
                movie.getPosterUrl(),
                movie.getRating() == null
                        ? null
                        : movie.getRating().doubleValue(),
                movie.getDescription());
    }

}
