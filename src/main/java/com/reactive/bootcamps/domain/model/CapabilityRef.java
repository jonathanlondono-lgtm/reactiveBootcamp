package com.reactive.bootcamps.domain.model;

import com.reactive.bootcamps.domain.utils.DomainException;

import java.util.Objects;
import java.util.UUID;

public class CapabilityRef {

    public static final String CAPABILITY_ID_REQUIRED = DomainException.CAPABILITY_ID_REQUIRED;
    public static final String CAPABILITY_NAME_REQUIRED = DomainException.CAPABILITY_NAME_REQUIRED;

    private final UUID id;
    private final String name;

    public CapabilityRef(UUID id, String name) {
        if (id == null) {
            throw new DomainException(CAPABILITY_ID_REQUIRED);
        }
        if (name == null || name.isBlank()) {
            throw new DomainException(CAPABILITY_NAME_REQUIRED);
        }
        this.id = id;
        this.name = name;
    }

    public UUID getId() { return id; }
    public String getName() { return name; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CapabilityRef)) return false;
        CapabilityRef that = (CapabilityRef) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
