package com.texoit.golden_raspberry_awards_service.controller.dto;

import lombok.Data;

import java.util.List;

@Data
public class ProducerResponseDto {
    private List<ProducerIntervalResponseDto> min;
    private List<ProducerIntervalResponseDto> max;
}
