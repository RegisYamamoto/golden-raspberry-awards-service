package com.texoit.golden_raspberry_awards_service.service;

import com.texoit.golden_raspberry_awards_service.entity.Movie;
import com.texoit.golden_raspberry_awards_service.repository.MovieRepository;
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
    private MovieRepository movieRepository;

    private static final String FILE_PATH = "src/main/resources/csvfiles/";
    private static final String COMMA_DELIMITER = ";";

    @PostConstruct
    private void fileHandle() throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(FILE_PATH + "movielist.csv"));

        List<List<String>> records = readFile(br);

        List<Movie> movies = createMovies(records);

        saveMovies(movies);
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

    public List<Movie> createMovies(List<List<String>> records) {
        List<Movie> movies = new ArrayList<>();
        for (List<String> record : records) {
            if (!record.get(0).equals("year")) {
                Movie movie = new Movie();
                for (int i = 0; i < record.size(); i++) {
                    if (i == 0) {
                        movie.setReleaseYear(Integer.parseInt(record.get(i)));
                    } else if (i == 1) {
                        movie.setTitle(record.get(i));
                    } else if (i == 2) {
                        movie.setStudios(record.get(i));
                    } else if (i == 3) {
                        movie.setProducers(record.get(i));
                    } else if (i == 4) {
                        if (record.get(i).equals("yes")) {
                            movie.setWinner(true);
                        } else {
                            movie.setWinner(false);
                        }
                    }
                }
                movies.add(movie);
            }
        }

        return movies;
    }

    public void saveMovies(List<Movie> movies) {
        movies.forEach(movie -> movieRepository.save(movie));
    }
}
