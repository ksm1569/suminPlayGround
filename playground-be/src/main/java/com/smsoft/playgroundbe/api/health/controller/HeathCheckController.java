package com.smsoft.playgroundbe.api.health.controller;

import com.smsoft.playgroundbe.api.health.dto.HealthCheckResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;

@RequiredArgsConstructor
@RequestMapping("/api")
@RestController
public class HeathCheckController {
    private final Environment environment;

    @GetMapping("/health")
    public ResponseEntity<HealthCheckResponseDTO> healthCheck() {
        HealthCheckResponseDTO healthCheckResponseDTO = HealthCheckResponseDTO.builder()
                .health("ok")
                .activeProfiles(Arrays.asList(environment.getActiveProfiles()))
                .build();

        return ResponseEntity.ok(healthCheckResponseDTO);
    }
}
