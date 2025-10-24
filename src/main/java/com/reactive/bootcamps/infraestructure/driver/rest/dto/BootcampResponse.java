package com.reactive.bootcamps.infraestructure.driver.rest.dto;

// src/main/java/com/reactive/bootcamps/application/dto/BootcampResponse.java

import lombok.Data;
import java.util.List;
import java.util.UUID;

@Data
public class BootcampResponse {
    private UUID id;
    private String name;
    private List<CapabilityResponse> capabilities;
    private List<TechnologyResponse> technologies;

    @Data
    public static class CapabilityResponse {
        private UUID id;
        private String name;
    }

    @Data
    public static class TechnologyResponse {
        private UUID id;
        private String name;
    }
}
