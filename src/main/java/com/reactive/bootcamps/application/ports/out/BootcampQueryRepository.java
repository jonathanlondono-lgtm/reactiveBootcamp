package com.reactive.bootcamps.application.ports.out;

import com.reactive.bootcamps.domain.model.PageRequest;
import com.reactive.bootcamps.domain.model.Bootcamp;
import reactor.core.publisher.Flux;

public interface BootcampQueryRepository {
    Flux<Bootcamp> findBootcamps(PageRequest pageRequest);
}