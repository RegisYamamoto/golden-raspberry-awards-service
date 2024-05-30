package com.texoit.golden_raspberry_awards_service.service;

import com.texoit.golden_raspberry_awards_service.entity.Movie;
import com.texoit.golden_raspberry_awards_service.entity.Producer;
import com.texoit.golden_raspberry_awards_service.entity.Studios;
import com.texoit.golden_raspberry_awards_service.repository.MovieRepository;
import com.texoit.golden_raspberry_awards_service.repository.ProducerRepository;
import com.texoit.golden_raspberry_awards_service.repository.StudiosRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FileService {

    @Autowired
    private StudiosRepository studiosRepository;

    @Autowired
    private ProducerRepository producerRepository;

    @Autowired
    private MovieRepository movieRepository;

    private static final String FILE_PATH = "src/main/resources/csvfiles/";
    private static final String COMMA_DELIMITER = ";";

    @PostConstruct
    private void fileHandle() throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_PATH + "movielist.csv"))) {
            List<List<String>> records = readFile(br);
            createAndSaveEntities(records);
        }
    }

    public List<List<String>> readFile(BufferedReader br) throws IOException {
        List<List<String>> records = new ArrayList<>();
        String line;

        while ((line = br.readLine()) != null) {
            String[] values = line.split(COMMA_DELIMITER);
            records.add(Arrays.asList(values));
        }

        return records;
    }

    public void createAndSaveEntities(List<List<String>> records) {
        records.stream()
                .skip(1) // Skip header row
                .map(this::createMovieFromRecord)
                .forEach(movieRepository::save);
    }

    private Movie createMovieFromRecord(List<String> record) {
        Movie movie = new Movie();
        movie.setReleaseYear(Integer.parseInt(record.get(0)));
        movie.setTitle(record.get(1));

        // Create studios
        List<Studios> studios = parseStudios(record.get(2));
        movie.setStudios(studios);

        // Create producer
        List<Producer> producers = parseProducers(record.get(3));
        movie.setProducer(producers);

        if (record.size() >= 5) {
            movie.setWinner("yes".equalsIgnoreCase(record.get(4)));
        }

        return movie;
    }

    private List<Producer> parseProducers(String producersRecord) {
        return Arrays.stream(producersRecord.split("\\band\\b|,"))
                .map(String::trim)
                .map(name -> {
                    Producer producer = new Producer();
                    producer.setName(name);
                    return getOrCreateProducer(producer);
                })
                .collect(Collectors.toList());
    }

    private List<Studios> parseStudios(String studiosRecord) {
        return Arrays.stream(studiosRecord.split("\\band\\b|,"))
                .map(String::trim)
                .map(name -> {
                    Studios studios = new Studios();
                    studios.setName(name);
                    return getOrCreateStudios(studios);
                })
                .collect(Collectors.toList());
    }

    private Studios getOrCreateStudios(Studios studios) {
        return studiosRepository.findByName(studios.getName())
                .stream()
                .findFirst()
                .orElseGet(() -> studiosRepository.save(studios));
    }

    private Producer getOrCreateProducer(Producer producer) {
        return producerRepository.findByName(producer.getName())
                .stream()
                .findFirst()
                .orElseGet(() -> producerRepository.save(producer));
    }
}
