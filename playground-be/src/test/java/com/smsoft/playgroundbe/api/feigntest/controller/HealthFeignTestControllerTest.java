package com.smsoft.playgroundbe.api.feigntest.controller;

import com.smsoft.playgroundbe.api.feigntest.client.HelloClient;
import com.smsoft.playgroundbe.api.health.dto.HealthCheckResponseDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.BDDMockito.given;

@WebMvcTest(HealthFeignTestController.class)
class HealthFeignTestControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private HelloClient helloClient;

    @Test
    @DisplayName("HealthCheck Test - openFeign")
    public void healthCheckTest_ReturnsOk() throws Exception {
        HealthCheckResponseDTO mockResponse = HealthCheckResponseDTO.builder()
                .health("ok")
                .activeProfiles(java.util.Collections.singletonList("test"))
                .build();

        given(helloClient.healthCheck()).willReturn(mockResponse);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/health/feign-test")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.health").value("ok"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.activeProfiles[0]").value("test"));

    }
}