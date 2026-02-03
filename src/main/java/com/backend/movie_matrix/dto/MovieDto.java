package com.backend.movie_matrix.dto;

public class MovieDto {
    private Long id;
    private String title;
    private String posterUrl;
    private Double rating;
    private String description;
    private String genre;
    private Integer duration;
    private Integer releaseYear;

    public MovieDto() {
    }

    public Integer getReleaseYear() {
        return releaseYear;
    }

    public MovieDto(Long id, String title, String posterUrl, Double rating, String description, String genre,
            int releaseYear, int duration) {
        this.id = id;
        this.title = title;
        this.posterUrl = posterUrl;
        this.rating = rating;
        this.description = description;
        this.genre = genre;
        this.releaseYear = releaseYear;
        this.duration = duration;
    }

    public String getGenre() {
        return genre;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public void setReleaseYear(Integer releaseYear) {
        this.releaseYear = releaseYear;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPosterUrl() {
        return posterUrl;
    }

    public void setPosterUrl(String posterUrl) {
        this.posterUrl = posterUrl;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
