package com.texoit.golden_raspberry_awards_service.controller;

import com.texoit.golden_raspberry_awards_service.controller.dto.ProducerDto;
import com.texoit.golden_raspberry_awards_service.entity.Movie;
import com.texoit.golden_raspberry_awards_service.service.ProducerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/producers")
public class ProducerController {

    @Autowired
    private ProducerService producerService;

    @GetMapping(value = "/consecutive-awards")
    public ResponseEntity<ProducerDto> getProducerWithTheLongestAndShortestGapBetweenTwoConsecutiveAwards() {
        ProducerDto producerDto = new ProducerDto();
        producerDto.setMin(1L);

        return ResponseEntity.status(HttpStatus.OK).body(producerDto);
    }
}
