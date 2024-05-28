package com.texoit.golden_raspberry_awards_service.repository;

import com.texoit.golden_raspberry_awards_service.entity.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {
}
