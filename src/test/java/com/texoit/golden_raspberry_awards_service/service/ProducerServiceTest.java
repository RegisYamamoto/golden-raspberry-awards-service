package com.texoit.golden_raspberry_awards_service.service;

import com.texoit.golden_raspberry_awards_service.controller.dto.ProducerResponseDto;
import com.texoit.golden_raspberry_awards_service.entity.Movie;
import com.texoit.golden_raspberry_awards_service.entity.Producer;
import com.texoit.golden_raspberry_awards_service.repository.MovieRepository;
import com.texoit.golden_raspberry_awards_service.repository.ProducerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class ProducerServiceTest {

    @Mock
    private ProducerRepository producerRepository;

    @Mock
    private MovieRepository movieRepository;

    @InjectMocks
    private ProducerService producerService;

    private static final boolean IS_WINNER = true;

    @Test
    public void itShouldReturnOneMinAndOneMaxWhenTwoProducersHasTwoWinsSuccessfully() {
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
        movie2.setReleaseYear(2001);
        movie2.setWinner(true);

        Producer producer2 = new Producer();
        producer2.setId(2L);
        producer2.setName("Producer 2");

        Movie movie3 = new Movie();
        movie3.setProducers(Collections.singletonList(producer2));
        movie3.setReleaseYear(2010);
        movie3.setWinner(true);

        Movie movie4 = new Movie();
        movie4.setProducers(Collections.singletonList(producer2));
        movie4.setReleaseYear(2015);
        movie4.setWinner(true);

        Mockito.when(producerRepository.findAll()).thenReturn(Arrays.asList(producer1, producer2));
        Mockito.when(movieRepository.findByProducerIdAndWinner(1L, IS_WINNER)).thenReturn(Arrays.asList(movie1, movie2));
        Mockito.when(movieRepository.findByProducerIdAndWinner(2L, IS_WINNER)).thenReturn(Arrays.asList(movie3, movie4));

        // Act
        ProducerResponseDto responseDto = producerService.getProducerWithTheLongestAndShortestGapBetweenTwoConsecutiveAwards();

        // Assert
        assertEquals(1, responseDto.getMin().size());
        assertEquals("Producer 1", responseDto.getMin().get(0).getProducer());
        assertEquals(1, responseDto.getMin().get(0).getInterval());
        assertEquals(2000, responseDto.getMin().get(0).getPreviousWin());
        assertEquals(2001, responseDto.getMin().get(0).getFollowingWin());

        assertEquals(1, responseDto.getMax().size());
        assertEquals("Producer 2", responseDto.getMax().get(0).getProducer());
        assertEquals(5, responseDto.getMax().get(0).getInterval());
        assertEquals(2010, responseDto.getMax().get(0).getPreviousWin());
        assertEquals(2015, responseDto.getMax().get(0).getFollowingWin());
    }

    @Test
    public void itShouldReturnTwoMinWhenHasTwoEqualsMinIntervals() {
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
        movie2.setReleaseYear(2001);
        movie2.setWinner(true);

        Producer producer2 = new Producer();
        producer2.setId(2L);
        producer2.setName("Producer 2");

        Movie movie3 = new Movie();
        movie3.setProducers(Collections.singletonList(producer2));
        movie3.setReleaseYear(2010);
        movie3.setWinner(true);

        Movie movie4 = new Movie();
        movie4.setProducers(Collections.singletonList(producer2));
        movie4.setReleaseYear(2011);
        movie4.setWinner(true);

        Mockito.when(producerRepository.findAll()).thenReturn(Arrays.asList(producer1, producer2));
        Mockito.when(movieRepository.findByProducerIdAndWinner(1L, IS_WINNER)).thenReturn(Arrays.asList(movie1, movie2));
        Mockito.when(movieRepository.findByProducerIdAndWinner(2L, IS_WINNER)).thenReturn(Arrays.asList(movie3, movie4));

        // Act
        ProducerResponseDto responseDto = producerService.getProducerWithTheLongestAndShortestGapBetweenTwoConsecutiveAwards();

        // Assert
        assertEquals(2, responseDto.getMin().size());

        assertEquals("Producer 1", responseDto.getMin().get(0).getProducer());
        assertEquals(1, responseDto.getMin().get(0).getInterval());
        assertEquals(2000, responseDto.getMin().get(0).getPreviousWin());
        assertEquals(2001, responseDto.getMin().get(0).getFollowingWin());

        assertEquals("Producer 2", responseDto.getMin().get(1).getProducer());
        assertEquals(1, responseDto.getMin().get(1).getInterval());
        assertEquals(2010, responseDto.getMin().get(1).getPreviousWin());
        assertEquals(2011, responseDto.getMin().get(1).getFollowingWin());
    }

    @Test
    public void itShouldReturnMinAndMaxSizeZeroWhenProducersIsEmpty() {
        // Act
        ProducerResponseDto responseDto = producerService.getProducerWithTheLongestAndShortestGapBetweenTwoConsecutiveAwards();

        // Assert
        assertEquals(0, responseDto.getMin().size());
        assertEquals(0, responseDto.getMax().size());
    }

    @Test
    public void itShouldReturnMinAndMaxSizeZeroWhenMoviesIsEmpty() {
        // Arrange
        Producer producer1 = new Producer();
        producer1.setId(1L);
        producer1.setName("Producer 1");

        Mockito.when(producerRepository.findAll()).thenReturn(Collections.singletonList(producer1));

        // Act
        ProducerResponseDto responseDto = producerService.getProducerWithTheLongestAndShortestGapBetweenTwoConsecutiveAwards();

        // Assert
        assertEquals(0, responseDto.getMin().size());
        assertEquals(0, responseDto.getMax().size());
    }

    @Test
    public void itShouldReturnMinAndMaxSizeZeroWhenMoviesSizeIsOne() {
        // Arrange
        Producer producer1 = new Producer();
        producer1.setId(1L);
        producer1.setName("Producer 1");

        Movie movie1 = new Movie();
        movie1.setProducers(Collections.singletonList(producer1));
        movie1.setReleaseYear(2000);
        movie1.setWinner(true);

        Mockito.when(producerRepository.findAll()).thenReturn(Collections.singletonList(producer1));
        Mockito.when(movieRepository.findByProducerIdAndWinner(1L, IS_WINNER)).thenReturn(Collections.singletonList(movie1));

        // Act
        ProducerResponseDto responseDto = producerService.getProducerWithTheLongestAndShortestGapBetweenTwoConsecutiveAwards();

        // Assert
        assertEquals(0, responseDto.getMin().size());
        assertEquals(0, responseDto.getMax().size());
    }
}