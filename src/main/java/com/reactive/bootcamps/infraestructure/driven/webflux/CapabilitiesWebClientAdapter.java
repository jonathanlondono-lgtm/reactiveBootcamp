package com.reactive.bootcamps.infraestructure.driven.webflux;



import com.reactive.bootcamps.application.ports.out.CapabilitiesBatchClient;
import com.reactive.bootcamps.infraestructure.driver.rest.dto.CapabilitiesBatchResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class CapabilitiesWebClientAdapter implements CapabilitiesBatchClient {
    private static final Logger log = LoggerFactory.getLogger(CapabilitiesWebClientAdapter.class);
    private final WebClient webClient;

    public CapabilitiesWebClientAdapter(WebClient.Builder builder) {
        this.webClient = builder
            .baseUrl("http://localhost:8081")
            .filter((request, next) -> {
                log.info("Request: {} {}", request.method(), request.url());
                return next.exchange(request)
                    .doOnNext(response -> log.info("Response status: {}", response.statusCode()));
            })
            .build();
    }

    @Override
    public Flux<CapabilitiesBatchResponse> fetchCapabilitiesBatch(List<UUID> bootcampIds) {
        var body = java.util.Map.of("bootcampIds", bootcampIds);

        log.info("Body enviado al endpoint de capacidades: {}", bootcampIds);

        return webClient.post()
                .uri("/api/batch/bootcamps/capabilities/query")
                .bodyValue(body)
                .retrieve()
                .bodyToFlux(CapabilitiesBatchResponse.class)
                .doOnNext(response -> log.info("Received: {}", response));
    }
}
