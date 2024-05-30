package com.texoit.golden_raspberry_awards_service.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.Data;

import java.util.List;

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

    @ManyToMany
    private List<Studios> studios;

    @ManyToMany
    private List<Producer> producers;

    @Column(name = "winner")
    private boolean winner;
}
