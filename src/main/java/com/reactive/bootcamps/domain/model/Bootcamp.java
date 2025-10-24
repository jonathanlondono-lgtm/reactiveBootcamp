package com.reactive.bootcamps.domain.model;

import com.reactive.bootcamps.domain.utils.DomainException;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class Bootcamp {

    private final UUID id;
    private final String name;
    private final String description;
    private final LocalDate launchDate;
    private final String duration;
    private final List<CapabilityRef> capabilities;

    private Bootcamp(UUID id, String name, String description,
                     LocalDate launchDate, String duration, List<CapabilityRef> capabilities) {
        validate(name, description, launchDate, duration, capabilities);
        this.id = id;
        this.name = name;
        this.description = description;
        this.launchDate = launchDate;
        this.duration = duration;
        this.capabilities = capabilities;
    }

    public static Bootcamp create(String name, String description,
                                  LocalDate launchDate, String duration,
                                  List<CapabilityRef> capabilities) {
        UUID id = UUID.randomUUID();
        return new Bootcamp(id, name, description, launchDate, duration, capabilities);
    }

    public static Bootcamp createForList(UUID id, String name, String description, LocalDate launchDate, String duration) {
        return new Bootcamp(id, name, description, launchDate, duration, java.util.List.of(), false);
    }

    private Bootcamp(UUID id, String name, String description,
                     LocalDate launchDate, String duration, List<CapabilityRef> capabilities, boolean validate) {
        if (validate) {
            validate(name, description, launchDate, duration, capabilities);
        }
        this.id = id;
        this.name = name;
        this.description = description;
        this.launchDate = launchDate;
        this.duration = duration;
        this.capabilities = capabilities;
    }

    private void validate(String name, String description, LocalDate launchDate,
                          String duration, List<CapabilityRef> capabilities) {

        if (name == null || name.isBlank())
            throw new DomainException(DomainException.NAME_REQUIRED);

        if (description == null || description.isBlank())
            throw new DomainException(DomainException.DESCRIPTION_REQUIRED);

        if (launchDate == null)
            throw new DomainException(DomainException.LAUNCH_DATE_REQUIRED);

        if (duration == null || duration.isBlank())
            throw new DomainException(DomainException.DURATION_REQUIRED);

        if (capabilities == null || capabilities.isEmpty())
            throw new DomainException(DomainException.CAPABILITIES_REQUIRED);

        if (capabilities.size() > 4)
            throw new DomainException(DomainException.CAPABILITIES_MAX);
    }

    // getters
    public UUID getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public LocalDate getLaunchDate() { return launchDate; }
    public String getDuration() { return duration; }
    public List<CapabilityRef> getCapabilities() { return capabilities; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Bootcamp)) return false;
        Bootcamp bootcamp = (Bootcamp) o;
        return Objects.equals(id, bootcamp.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
