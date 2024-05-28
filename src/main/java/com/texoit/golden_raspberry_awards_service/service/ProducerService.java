package com.texoit.golden_raspberry_awards_service.service;

import com.texoit.golden_raspberry_awards_service.entity.Movie;
import com.texoit.golden_raspberry_awards_service.repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProducerService {

    @Autowired
    private MovieRepository movieRepository;

    public List<Movie> getProducerWithTheLongestGapBetweenTwoConsecutiveAwards() {
        return movieRepository.findAll();
    }

    public void getProducerWithTheShortestGapBetweenTwoConsecutiveAwards() {

    }
}
