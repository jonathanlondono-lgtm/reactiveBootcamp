package com.reactive.bootcamps.application.ports.in;

import com.reactive.bootcamps.domain.model.PageRequest;
import com.reactive.bootcamps.infraestructure.driver.rest.dto.BootcampResponse;
import reactor.core.publisher.Flux;

public interface ListBootcampsQuery {
    Flux<BootcampResponse> listBootcamps(PageRequest pageRequest);
}