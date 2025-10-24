package com.reactive.bootcamps.infraestructure.driver.rest.dto;

// src/main/java/com/reactive/bootcamps/infraestructure/driven/capabilities/dto/CapabilitiesBatchResponse.java

import lombok.Data;
import java.util.List;
import java.util.UUID;

@Data
public class CapabilitiesBatchResponse {
    private UUID bootcampId;
    private List<CapabilityDetail> capabilities;

    @Data
    public static class CapabilityDetail {
        private UUID capabilityId;
        private String name;
        private String description;
        private List<TechnologyDetail> technologies;
    }

    @Data
    public static class TechnologyDetail {
        private UUID id;
        private String name;
    }
}

