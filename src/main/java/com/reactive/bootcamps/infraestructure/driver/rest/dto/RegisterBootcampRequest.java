package com.reactive.bootcamps.infraestructure.driver.rest.dto;

import java.time.LocalDate;
import java.util.List;

public class RegisterBootcampRequest {
    private String name;
    private String description;
    private LocalDate launchDate;
    private String duration;
    private List<CapabilityRefRequest> capabilities;

    public RegisterBootcampRequest() {}

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public LocalDate getLaunchDate() { return launchDate; }
    public void setLaunchDate(LocalDate launchDate) { this.launchDate = launchDate; }
    public String getDuration() { return duration; }
    public void setDuration(String duration) { this.duration = duration; }
    public List<CapabilityRefRequest> getCapabilities() { return capabilities; }
    public void setCapabilities(List<CapabilityRefRequest> capabilities) { this.capabilities = capabilities; }
}