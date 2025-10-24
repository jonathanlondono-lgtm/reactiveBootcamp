package com.reactive.bootcamps.infraestructure.driver.rest.dto;

import java.util.UUID;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CapabilityRefRequest {
    private UUID id;
    private String name;
}
