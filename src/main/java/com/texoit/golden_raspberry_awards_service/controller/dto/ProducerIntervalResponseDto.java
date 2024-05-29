package com.texoit.golden_raspberry_awards_service.controller.dto;

import lombok.Data;

@Data
public class ProducerIntervalResponseDto {

    private String producer;
    private Integer interval;
    private Integer previousWin;
    private Integer followingWin;
}
