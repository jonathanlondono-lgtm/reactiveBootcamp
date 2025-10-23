package com.reactive.bootcamps.infraestructure.driven.rabbitmq.mapper;

import com.reactive.bootcamps.domain.event.BootcampCreatedEvent;
import com.reactive.bootcamps.infraestructure.driven.rabbitmq.dto.BootcampCreatedEventDTO;

public class BootcampCreatedEventMapper {
    public static BootcampCreatedEventDTO toDTO(BootcampCreatedEvent event) {
        return new BootcampCreatedEventDTO(
                event.getBootcampId(),
                event.getCapabilityIds(),
                event.getOccurredOn()
        );
    }
}
