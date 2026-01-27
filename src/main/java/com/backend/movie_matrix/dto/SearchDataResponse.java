package com.backend.movie_matrix.dto;

import java.util.List;

public class SearchDataResponse {
    private List<String> recentSearches;
    private List<String> popularMovies;

    public SearchDataResponse(){

    }

    public SearchDataResponse(List<String> recentSeaches, List<String> popularMovies){
        this.recentSearches = recentSeaches;
        this.popularMovies = popularMovies;
    }

    public List<String> getRecentSearches() {
        return recentSearches;
    }

    public void setRecentSearches(List<String> recentSearches) {
        this.recentSearches = recentSearches;
    }

    public List<String> getPopularMovies() {
        return popularMovies;
    }

    public void setPopularMovies(List<String> popularMovies) {
        this.popularMovies = popularMovies;
    }

    
}
