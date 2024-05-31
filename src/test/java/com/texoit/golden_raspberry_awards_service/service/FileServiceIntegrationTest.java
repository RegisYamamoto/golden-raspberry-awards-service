package com.texoit.golden_raspberry_awards_service.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;

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
public class FileServiceIntegrationTest {

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
}