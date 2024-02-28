package com.smsoft.playgroundbe.api.health.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter @Builder
public class HealthCheckResponseDTO {
    private String health;
    private List<String> activeProfiles;
}
