package com.reactive.bootcamps.application.ports.out;

import com.reactive.bootcamps.domain.model.Bootcamp;
import reactor.core.publisher.Mono;

public interface BootcampRepository {
    Mono<Bootcamp> save(Bootcamp bootcamp);
}