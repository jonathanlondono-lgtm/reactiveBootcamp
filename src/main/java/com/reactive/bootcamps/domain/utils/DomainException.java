package com.reactive.bootcamps.domain.utils;

public class DomainException extends RuntimeException {
    public static final String NAME_REQUIRED = "Bootcamp name is required.";
    public static final String DESCRIPTION_REQUIRED = "Bootcamp description is required.";
    public static final String LAUNCH_DATE_REQUIRED = "Bootcamp launch date is required.";
    public static final String CAPABILITIES_REQUIRED = "Bootcamp must have at least one capability.";
    public static final String CAPABILITIES_MAX = "Bootcamp cannot have more than 4 capabilities.";
    public static final String DURATION_REQUIRED = "Bootcamp duration is required.";
    public static final String CAPABILITY_ID_REQUIRED = "Capability ID is required.";
    public static final String CAPABILITY_NAME_REQUIRED = "Capability name is required.";

    public DomainException(String message) {
        super(message);
    }
}
