package com.texoit.golden_raspberry_awards_service.service;

import com.texoit.golden_raspberry_awards_service.controller.dto.ProducerIntervalResponseDto;
import com.texoit.golden_raspberry_awards_service.controller.dto.ProducerResponseDto;
import com.texoit.golden_raspberry_awards_service.entity.Movie;
import com.texoit.golden_raspberry_awards_service.entity.Producer;
import com.texoit.golden_raspberry_awards_service.repository.MovieRepository;
import com.texoit.golden_raspberry_awards_service.repository.ProducerRepository;
import com.texoit.golden_raspberry_awards_service.service.dto.ProducerInterval;
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
        List<ProducerInterval> producersInterval = new ArrayList<>();

        for (Producer producer : producers) {
            List<Movie> movies = movieRepository.findByProducerIdAndWinner(producer.getId(), true);
            if (movies.size() >= 2) {
                List<Integer> releaseYears = movies.stream().map(Movie::getReleaseYear).sorted().collect(Collectors.toList());

                // Calculate the min and max interval for this producer
                int minProducerInterval = Integer.MAX_VALUE;
                int maxProducerInterval = 0;
                int previousWin = 0;
                int followingWin = 0;
                for (int i = 1; i < releaseYears.size(); i++) {
                    int interval = releaseYears.get(i) - releaseYears.get(i - 1);
                    if (interval < minProducerInterval) {
                        minProducerInterval = interval;
                        previousWin = releaseYears.get(i - 1);
                    }
                    if (interval > maxProducerInterval) {
                        maxProducerInterval = interval;
                        followingWin = releaseYears.get(i);
                    }
                }

                ProducerInterval producerInterval = new ProducerInterval();
                producerInterval.setId(producer.getId());
                producerInterval.setName(producer.getName());
                producerInterval.setMinInterval(minProducerInterval);
                producerInterval.setMaxInterval(maxProducerInterval);
                producerInterval.setPreviousWin(previousWin);
                producerInterval.setFollowingWin(followingWin);

                producersInterval.add(producerInterval);
            }
        }

        List<ProducerInterval> producersWithMinInterval = new ArrayList<>();
        List<ProducerInterval> producersWithMaxInterval = new ArrayList<>();
        int minProducerInterval = Integer.MAX_VALUE;
        int maxProducerInterval = Integer.MIN_VALUE;
        for (ProducerInterval producerInterval : producersInterval) {
            if (producerInterval.getMinInterval() <= minProducerInterval) {
                producersWithMinInterval.add(producerInterval);
            }
            if (producerInterval.getMaxInterval() >= maxProducerInterval) {
                producersWithMaxInterval.add(producerInterval);
            }
        }

        ProducerResponseDto producerResponseDto = new ProducerResponseDto();
        List<ProducerIntervalResponseDto> min = new ArrayList<>();
        List<ProducerIntervalResponseDto> max = new ArrayList<>();
        for (ProducerInterval producerWithMinInterval : producersWithMinInterval) {
            ProducerIntervalResponseDto producerIntervalResponseDto = new ProducerIntervalResponseDto();
            producerIntervalResponseDto.setProducer(producerWithMinInterval.getName());
            producerIntervalResponseDto.setInterval(producerWithMinInterval.getMinInterval());
            producerIntervalResponseDto.setPreviousWin(producerWithMinInterval.getPreviousWin());
            producerIntervalResponseDto.setFollowingWin(producerWithMinInterval.getFollowingWin());

            min.add(producerIntervalResponseDto);

        }

        for (ProducerInterval producerWithMaxInterval : producersWithMaxInterval) {
            ProducerIntervalResponseDto producerIntervalResponseDto = new ProducerIntervalResponseDto();
            producerIntervalResponseDto.setProducer(producerWithMaxInterval.getName());
            producerIntervalResponseDto.setInterval(producerWithMaxInterval.getMinInterval());
            producerIntervalResponseDto.setPreviousWin(producerWithMaxInterval.getPreviousWin());
            producerIntervalResponseDto.setFollowingWin(producerWithMaxInterval.getFollowingWin());

            max.add(producerIntervalResponseDto);
        }

        producerResponseDto.setMin(min);
        producerResponseDto.setMax(min);

        return producerResponseDto;
    }
}
