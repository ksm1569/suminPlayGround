package com.smsoft.playgroundbe.api.feigntest.client;

import com.smsoft.playgroundbe.api.health.dto.HealthCheckResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(url = "http://localhost:8082", name = "helloClient")
public interface HelloClient {
    @GetMapping(value = "/api/health", consumes = "application/json")
    HealthCheckResponseDTO healthCheck();
}
