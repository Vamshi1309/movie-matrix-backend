package com.backend.movie_matrix.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.backend.movie_matrix.entity.Movie;

@Repository
public interface MovieRepo extends JpaRepository<Movie, Long> {
    @Query("SELECT m FROM Movie m " +
            "JOIN MovieCategory mc ON m.id = mc.movieId " +
            "JOIN Category c ON mc.categoryId = c.id " +
            "WHERE c.name = :categoryName " +
            "ORDER BY mc.displayOrder")
    List<Movie> findMoviesByCategory(@Param("categoryName") String categoryName);

    List<Movie> findTop10ByOrderByRatingDesc();

    List<Movie> findByTitleContainingIgnoreCase(String title);

    Optional<Movie> findByTitleIgnoreCase(String title);
}
