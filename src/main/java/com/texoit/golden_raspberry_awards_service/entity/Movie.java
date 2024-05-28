package com.texoit.golden_raspberry_awards_service.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.Setter;

@Data
@Entity
@Table(name = "movie")
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "release_year")
    private Integer releaseYear;

    @Column(name = "title")
    private String title;

    @ManyToOne
    private Studios studios;

    @ManyToOne
    private Producer producer;

    @Column(name = "winner")
    private boolean winner;
}
