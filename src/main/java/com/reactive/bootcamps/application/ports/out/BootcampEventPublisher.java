package com.reactive.bootcamps.application.ports.out;

import com.reactive.bootcamps.domain.event.BootcampCreatedEvent;
import reactor.core.publisher.Mono;

public interface BootcampEventPublisher {
    Mono<Void> publishBootcampCreated(BootcampCreatedEvent event);
}
