package com.reactive.bootcamps.application.ports.out;

import com.reactive.bootcamps.infraestructure.driver.rest.dto.CapabilitiesBatchResponse;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.UUID;

public interface CapabilitiesBatchClient {
    Flux<CapabilitiesBatchResponse> fetchCapabilitiesBatch(List<UUID> bootcampIds);

}
