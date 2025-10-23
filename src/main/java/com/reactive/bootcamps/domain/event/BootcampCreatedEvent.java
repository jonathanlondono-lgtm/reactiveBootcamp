package com.reactive.bootcamps.domain.event;


import java.time.Instant;
import java.util.List;
import java.util.UUID;

public class BootcampCreatedEvent {

    private final UUID bootcampId;
    private final List<UUID> capabilityIds;
    private final Instant occurredOn;

    public BootcampCreatedEvent(UUID bootcampId, List<UUID> capabilityIds) {
        this.bootcampId = bootcampId;
        this.capabilityIds = List.copyOf(capabilityIds);
        this.occurredOn = Instant.now();
    }

    public UUID getBootcampId() {
        return bootcampId;
    }

    public List<UUID> getCapabilityIds() {
        return capabilityIds;
    }

    public Instant getOccurredOn() {
        return occurredOn;
    }
}
