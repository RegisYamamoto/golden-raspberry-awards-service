package com.texoit.golden_raspberry_awards_service.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest
public class ProducerControllerIntegrationTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void itShouldReturnProducerWithTheLongestAndShortestGapBetweenTwoConsecutiveAwards() throws Exception {
        String expectedJsonResponse = """
            {
                "min": [
                    {
                        "producer": "Joel Silver",
                        "interval": 1,
                        "previousWin": 1990,
                        "followingWin": 1991
                    }
                ],
                "max": [
                    {
                        "producer": "Matthew Vaughn",
                        "interval": 13,
                        "previousWin": 2002,
                        "followingWin": 2015
                    }
                ]
            }
            """;

        mockMvc.perform(MockMvcRequestBuilders.get("/producers/consecutive-awards")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(expectedJsonResponse));
    }
}
