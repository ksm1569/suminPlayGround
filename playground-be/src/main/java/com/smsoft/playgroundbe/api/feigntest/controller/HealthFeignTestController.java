package com.smsoft.playgroundbe.api.feigntest.controller;

import com.smsoft.playgroundbe.api.feigntest.client.HelloClient;
import com.smsoft.playgroundbe.api.health.dto.HealthCheckResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api")
@RestController
public class HealthFeignTestController {
    private final HelloClient helloClient;

    @GetMapping("/health/feign-test")
    public ResponseEntity<HealthCheckResponseDTO> healthCheckTest() {
        return ResponseEntity.status(HttpStatus.OK).body(helloClient.healthCheck());
    }
}
