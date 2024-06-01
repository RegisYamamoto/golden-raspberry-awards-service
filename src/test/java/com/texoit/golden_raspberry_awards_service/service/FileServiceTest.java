package com.texoit.golden_raspberry_awards_service.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.texoit.golden_raspberry_awards_service.entity.Movie;
import com.texoit.golden_raspberry_awards_service.entity.Producer;
import com.texoit.golden_raspberry_awards_service.entity.Studio;
import com.texoit.golden_raspberry_awards_service.repository.MovieRepository;
import com.texoit.golden_raspberry_awards_service.repository.ProducerRepository;
import com.texoit.golden_raspberry_awards_service.repository.StudiosRepository;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.Arrays;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class FileServiceTest {

    @Mock
    private StudiosRepository studiosRepository;

    @Mock
    private ProducerRepository producerRepository;

    @Mock
    private MovieRepository movieRepository;

    @InjectMocks
    private FileService fileService;

    @Captor
    ArgumentCaptor<Studio> studioCaptor;

    @Captor
    ArgumentCaptor<Producer> producerCaptor;

    @Captor
    ArgumentCaptor<Movie> movieCaptor;

    @Test
    void itShouldCreateAndSaveEntitiesWhenStudioAndProducerDoNotExist() {
        // Arrange
        List<List<String>> records = Arrays.asList(
                Arrays.asList("year", "title", "studios", "producers", "winner"),
                Arrays.asList("1980", "Test Movie", "Test Studio", "Test Producer", "yes")
        );

        // Act
        fileService.createAndSaveEntities(records);

        // Assert
        verify(studiosRepository).save(studioCaptor.capture());
        verify(producerRepository).save(producerCaptor.capture());
        verify(movieRepository).save(movieCaptor.capture());

        Studio studioCaptorValue = studioCaptor.getValue();
        Producer producerCaptorValue = producerCaptor.getValue();
        Movie movieCaptorValue = movieCaptor.getValue();

        assertNull(studioCaptorValue.getId());
        assertEquals("Test Studio", studioCaptorValue.getName());
        assertNull(producerCaptorValue.getId());
        assertEquals("Test Producer", producerCaptorValue.getName());
        assertNull(movieCaptorValue.getId());
        assertEquals(1980, movieCaptorValue.getReleaseYear());
        assertEquals("Test Movie", movieCaptorValue.getTitle());
        assertEquals(1, movieCaptorValue.getStudios().size());
        assertEquals(1, movieCaptorValue.getProducers().size());
        assertTrue(movieCaptorValue.isWinner());
    }

    @Test
    public void itShouldDontSaveStudioWhenStudioAlredyExist() {
        // Arrange
        List<List<String>> records = Arrays.asList(
                Arrays.asList("year", "title", "studios", "producers", "winner"),
                Arrays.asList("1980", "Test Movie", "Test Studio", "Test Producer", "yes")
        );

        Studio studio = new Studio();
        studio.setId(1L);
        List<Studio> studios = Arrays.asList(studio);

        when(studiosRepository.findByName(any())).thenReturn(studios);

        // Act
        fileService.createAndSaveEntities(records);

        // Assert
        verify(studiosRepository, never()).save(any());
    }

    @Test
    public void itShouldSaveThreeStudiosWhenHaveThreeStudiosInRecords() {
        // Arrange
        List<List<String>> records = Arrays.asList(
                Arrays.asList("year", "title", "studios", "producers", "winner"),
                Arrays.asList("1980", "Test Movie", "Test Studio and Test Studio 2, Test Studio 3", "Test Producer", "yes")
        );

        // Act
        fileService.createAndSaveEntities(records);

        // Assert
        verify(studiosRepository, times(3)).save(any());
    }

    @Test
    public void itShouldDontSaveProducerWhenProducerAlredyExist() {
        // Arrange
        List<List<String>> records = Arrays.asList(
                Arrays.asList("year", "title", "studios", "producers", "winner"),
                Arrays.asList("1980", "Test Movie", "Test Studio", "Test Producer", "yes")
        );

        Producer producer = new Producer();
        producer.setId(1L);
        List<Producer> producers = Arrays.asList(producer);

        when(producerRepository.findByName(any())).thenReturn(producers);

        // Act
        fileService.createAndSaveEntities(records);

        // Assert
        verify(producerRepository, never()).save(any());
    }

    @Test
    public void itShouldSaveThreeProducersWhenHaveThreeProducedrsInRecords() {
        // Arrange
        List<List<String>> records = Arrays.asList(
                Arrays.asList("year", "title", "studios", "producers", "winner"),
                Arrays.asList("1980", "Test Movie", "Test Studio", "Test Producer and Test Producer 2, Test Producer 3", "yes")
        );

        // Act
        fileService.createAndSaveEntities(records);

        // Assert
        verify(producerRepository, times(3)).save(any());
    }
}