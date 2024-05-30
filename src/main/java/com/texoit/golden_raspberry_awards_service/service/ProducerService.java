package com.texoit.golden_raspberry_awards_service.service;

import com.texoit.golden_raspberry_awards_service.controller.dto.ProducerIntervalResponseDto;
import com.texoit.golden_raspberry_awards_service.controller.dto.ProducerResponseDto;
import com.texoit.golden_raspberry_awards_service.entity.Movie;
import com.texoit.golden_raspberry_awards_service.entity.Producer;
import com.texoit.golden_raspberry_awards_service.repository.MovieRepository;
import com.texoit.golden_raspberry_awards_service.repository.ProducerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProducerService {

    @Autowired
    private MovieRepository movieRepository;
    @Autowired
    private ProducerRepository producerRepository;

    public ProducerResponseDto getProducerWithTheLongestAndShortestGapBetweenTwoConsecutiveAwards() {
        List<Producer> producers = producerRepository.findAll();

        List<ProducerIntervalResponseDto> producersWithMinInterval = new ArrayList<>();
        List<ProducerIntervalResponseDto> producersWithMaxInterval = new ArrayList<>();
        int minReleaseYearsInteval = Integer.MAX_VALUE;
        int maxReleaseYearsInteval = 0;

        for (Producer producer : producers) {
            List<Movie> movies = movieRepository.findByProducerIdAndWinner(producer.getId(), true);

            if (movies.size() >= 2) {
                List<Integer> sortedReleaseYears = movies.stream()
                        .map(Movie::getReleaseYear)
                        .sorted()
                        .collect(Collectors.toList());

                for (int i = 1; i < sortedReleaseYears.size(); i++) {
                    int releaseYearsInteval = sortedReleaseYears.get(i) - sortedReleaseYears.get(i - 1);

                    // Update the shortest interval
                    if (releaseYearsInteval <= minReleaseYearsInteval) {
                        if (releaseYearsInteval < minReleaseYearsInteval) {
                            minReleaseYearsInteval = releaseYearsInteval;
                            producersWithMinInterval.clear();
                        }
                        producersWithMinInterval.add(createProducerIntervalResponseDto(producer.getName(), sortedReleaseYears.get(i - 1), sortedReleaseYears.get(i), releaseYearsInteval));
                    }

                    // Update the longest interval
                    if (releaseYearsInteval >= maxReleaseYearsInteval) {
                        if (releaseYearsInteval > maxReleaseYearsInteval) {
                            maxReleaseYearsInteval = releaseYearsInteval;
                            producersWithMaxInterval.clear();
                        }
                        producersWithMaxInterval.add(createProducerIntervalResponseDto(producer.getName(), sortedReleaseYears.get(i - 1), sortedReleaseYears.get(i), releaseYearsInteval));
                    }
                }
            }
        }

        ProducerResponseDto producerResponseDto = new ProducerResponseDto();
        producerResponseDto.setMin(producersWithMinInterval);
        producerResponseDto.setMax(producersWithMaxInterval);

        return producerResponseDto;
    }

    private ProducerIntervalResponseDto createProducerIntervalResponseDto(
            String producerName, int previousWin, int followingWin, int interval) {
        ProducerIntervalResponseDto dto = new ProducerIntervalResponseDto();
        dto.setProducer(producerName);
        dto.setPreviousWin(previousWin);
        dto.setFollowingWin(followingWin);
        dto.setInterval(interval);
        return dto;
    }
}
