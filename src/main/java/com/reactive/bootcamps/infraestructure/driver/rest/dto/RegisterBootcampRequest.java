package com.reactive.bootcamps.infraestructure.driver.rest.dto;

import java.time.LocalDate;
import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RegisterBootcampRequest {
    private String name;
    private String description;
    private LocalDate launchDate;
    private String duration;
    private List<CapabilityRefRequest> capabilities;
}