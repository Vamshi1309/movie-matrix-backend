package com.backend.movie_matrix.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.backend.movie_matrix.entity.SearchHistory;

@Repository
public interface SearchHistoryRepo extends JpaRepository<SearchHistory, Long> {
    @Query("""
            SELECT sh.searchQuery
            FROM SearchHistory sh
            WHERE sh.user.id = :userId
            GROUP BY sh.searchQuery
            ORDER BY MAX(sh.searchedAt) DESC
            """)
    List<String> findTop10RecentSearchesByUserId(Long userId);
}
