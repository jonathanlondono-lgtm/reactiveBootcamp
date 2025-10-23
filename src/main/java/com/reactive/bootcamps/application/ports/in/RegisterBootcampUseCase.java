package com.reactive.bootcamps.application.ports.in;

import com.reactive.bootcamps.domain.model.Bootcamp;
import com.reactive.bootcamps.domain.model.CapabilityRef;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.List;

public interface RegisterBootcampUseCase {

    Mono<Bootcamp> registerBootcamp(
            String name,
            String description,
            LocalDate launchDate,
            String duration,
            List<CapabilityRef> capabilities
    );
}