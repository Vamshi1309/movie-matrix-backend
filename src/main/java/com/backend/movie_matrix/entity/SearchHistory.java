package com.backend.movie_matrix.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "search_history")
@AllArgsConstructor
@Data
@NoArgsConstructor
public class SearchHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "movie_id", nullable = false)
    private Movie movie;

    @Column(name = "search_query", nullable = false)
    private String searchQuery;

    @Column(name = "searched_at", nullable = false)
    private LocalDateTime searchedAt;

    public SearchHistory(User user, Movie movie, String searchQuery) {
        this.user = user;
        this.movie = movie;
        this.searchQuery = searchQuery;
        this.searchedAt = LocalDateTime.now();
    }

    @PrePersist
    protected void onCreate() {
        if (searchedAt == null) {
            searchedAt = LocalDateTime.now();
        }
    }
}
