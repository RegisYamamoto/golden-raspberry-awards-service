package com.texoit.golden_raspberry_awards_service.service.dto;

import lombok.Data;

@Data
public class ProducerInterval {

    private Long id;
    private String name;
    private Integer minInterval;
    private Integer maxInterval;
    private Integer previousWin;
    private Integer followingWin;
}
