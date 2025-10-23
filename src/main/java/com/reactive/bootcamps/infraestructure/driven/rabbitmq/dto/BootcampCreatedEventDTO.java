package com.reactive.bootcamps.infraestructure.driven.rabbitmq.dto;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public class BootcampCreatedEventDTO {
    private UUID bootcampId;
    private List<UUID> capabilityIds;
    private Instant occurredOn;

    public BootcampCreatedEventDTO() {}

    public BootcampCreatedEventDTO(UUID bootcampId, List<UUID> capabilityIds, Instant occurredOn) {
        this.bootcampId = bootcampId;
        this.capabilityIds = capabilityIds;
        this.occurredOn = occurredOn;
    }

    public UUID getBootcampId() { return bootcampId; }
    public void setBootcampId(UUID bootcampId) { this.bootcampId = bootcampId; }
    public List<UUID> getCapabilityIds() { return capabilityIds; }
    public void setCapabilityIds(List<UUID> capabilityIds) { this.capabilityIds = capabilityIds; }
    public Instant getOccurredOn() { return occurredOn; }
    public void setOccurredOn(Instant occurredOn) { this.occurredOn = occurredOn; }
}

