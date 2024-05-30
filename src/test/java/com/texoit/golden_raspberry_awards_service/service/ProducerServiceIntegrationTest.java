package com.texoit.golden_raspberry_awards_service.service;

import com.texoit.golden_raspberry_awards_service.controller.dto.ProducerResponseDto;
import com.texoit.golden_raspberry_awards_service.entity.Movie;
import com.texoit.golden_raspberry_awards_service.entity.Producer;
import com.texoit.golden_raspberry_awards_service.repository.MovieRepository;
import com.texoit.golden_raspberry_awards_service.repository.ProducerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ProducerServiceIntegrationTest {

    @Autowired
    private ProducerService producerService;

    @MockBean
    private ProducerRepository producerRepository;

    @MockBean
    private MovieRepository movieRepository;

    @Test
    public void itShouldTestGetProducerWithTheLongestAndShortestGapBetweenTwoConsecutiveAwardsSuccessfully() {
        // Arrange
        Producer producer1 = new Producer();
        producer1.setId(1L);
        producer1.setName("Producer 1");

        Movie movie1 = new Movie();
        movie1.setProducers(Collections.singletonList(producer1));
        movie1.setReleaseYear(2000);
        movie1.setWinner(true);

        Movie movie2 = new Movie();
        movie1.setProducers(Collections.singletonList(producer1));
        movie2.setReleaseYear(2005);
        movie2.setWinner(true);

        Movie movie3 = new Movie();
        movie1.setProducers(Collections.singletonList(producer1));
        movie3.setReleaseYear(2010);
        movie3.setWinner(true);

        Mockito.when(producerRepository.findAll()).thenReturn(Collections.singletonList(producer1));
        Mockito.when(movieRepository.findByProducerIdAndWinner(1L, true)).thenReturn(Arrays.asList(movie1, movie2, movie3));

        // Act
        ProducerResponseDto responseDto = producerService.getProducerWithTheLongestAndShortestGapBetweenTwoConsecutiveAwards();

        // Assert
        assertEquals(1, responseDto.getMin().size());
        assertEquals(1, responseDto.getMax().size());
        assertEquals("Producer 1", responseDto.getMin().get(0).getProducer());
        assertEquals(2000, responseDto.getMin().get(0).getPreviousWin());
        assertEquals(2005, responseDto.getMin().get(0).getFollowingWin());
        assertEquals(5, responseDto.getMin().get(0).getInterval());
        assertEquals("Producer 1", responseDto.getMax().get(0).getProducer());
        assertEquals(2005, responseDto.getMax().get(0).getPreviousWin());
        assertEquals(2010, responseDto.getMax().get(0).getFollowingWin());
        assertEquals(5, responseDto.getMax().get(0).getInterval());
    }
}