package com.texoit.golden_raspberry_awards_service.controller;

import com.texoit.golden_raspberry_awards_service.controller.dto.ProducerResponseDto;
import com.texoit.golden_raspberry_awards_service.repository.MovieRepository;
import com.texoit.golden_raspberry_awards_service.service.ProducerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/producers")
public class ProducerController {

    @Autowired
    private ProducerService producerService;
    @Autowired
    private MovieRepository movieRepository;

    @GetMapping(value = "/consecutive-awards")
    public ResponseEntity<ProducerResponseDto> getProducerWithTheLongestAndShortestGapBetweenTwoConsecutiveAwards() {
        ProducerResponseDto producerResponseDto = producerService.getProducerWithTheLongestAndShortestGapBetweenTwoConsecutiveAwards();
        return ResponseEntity.status(HttpStatus.OK).body(producerResponseDto);
    }
}
