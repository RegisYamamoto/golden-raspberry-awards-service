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
        BufferedReader br = new BufferedReader(new FileReader(FILE_PATH + "movielist.csv"));
        List<List<String>> records = readFile(br);
        createAndSaveEntities(records);
    }

    public List<List<String>> readFile(BufferedReader br) throws IOException {
        List<List<String>> records = new ArrayList<>();
        String line;

        while((line = br.readLine()) != null) {
            String[] values = line.split(COMMA_DELIMITER);
            records.add(Arrays.asList(values));
        }

        return records;
    }

    public List<Movie> createAndSaveEntities(List<List<String>> records) {
        List<Movie> movies = new ArrayList<>();
        for (List<String> record : records) {
            if (!record.get(0).equals("year")) {
                Studios studios = new Studios();
                Producer producer = new Producer();
                Movie movie = new Movie();
                for (int i = 0; i < record.size(); i++) {
                    if (i == 0) {
                        movie.setReleaseYear(Integer.parseInt(record.get(i)));
                    } else if (i == 1) {
                        movie.setTitle(record.get(i));
                    } else if (i == 2) {
                        studios.setName(record.get(i));
                    } else if (i == 3) {
                        producer.setName(record.get(i));
                    } else if (i == 4) {
                        if (record.get(i).equals("yes")) {
                            movie.setWinner(true);
                        } else {
                            movie.setWinner(false);
                        }
                    }
                }
                setStudios(studios, movie);
                setProducer(producer, movie);

                movieRepository.save(movie);
            }
        }

        return movies;
    }

    private void setStudios(Studios studios, Movie movie) {
        List<Studios> studiosFromDb = studiosRepository.findByName(studios.getName());
        if (studiosFromDb.size() != 0) {
            movie.setStudios(studiosFromDb.stream().findFirst().get());
        } else {
            studiosRepository.save(studios);
            movie.setStudios(studios);
        }
    }

    private void setProducer(Producer producer, Movie movie) {
        List<Producer> producersFromDb = producerRepository.findByName(producer.getName());
        if (producersFromDb.size() != 0) {
            movie.setProducer(producersFromDb.stream().findFirst().get());
        } else {
            producerRepository.save(producer);
            movie.setProducer(producer);
        }
    }
}
