package com.reactive.bootcamps.infraestructure.driver.rest.dto;

import java.util.UUID;

public class CapabilityRefRequest {
    private UUID id;
    private String name;

    public CapabilityRefRequest() {}

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
}

