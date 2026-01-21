package com.backend.movie_matrix.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "movie_categories")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class MovieCategory {

    @Id
    @Column(name = "movie_id")
    private Long movieId;

    @Id
    @Column(name = "category_id")
    private Long categoryId;

    @Column(name = "display_order")
    private Integer displayOrder;
}
