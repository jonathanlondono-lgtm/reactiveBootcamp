package com.reactive.bootcamps.application.usecase;

import com.reactive.bootcamps.application.ports.in.ListBootcampsQuery;
import com.reactive.bootcamps.application.ports.out.BootcampQueryRepository;
import com.reactive.bootcamps.application.ports.out.CapabilitiesBatchClient;
import com.reactive.bootcamps.domain.model.Bootcamp;
import com.reactive.bootcamps.domain.model.PageRequest;
import com.reactive.bootcamps.infraestructure.driver.rest.dto.BootcampResponse;
import com.reactive.bootcamps.infraestructure.driver.rest.mapper.BootcampResponseMapper;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ListBootcampsService  implements ListBootcampsQuery {
    private static final Logger log = LoggerFactory.getLogger(ListBootcampsService.class);
    private final BootcampQueryRepository bootcampRepo;
    private final CapabilitiesBatchClient capabilitiesClient;
    public ListBootcampsService(BootcampQueryRepository bootcampRepo, CapabilitiesBatchClient capabilitiesClient) {
        this.bootcampRepo = bootcampRepo;
        this.capabilitiesClient = capabilitiesClient;
    }
    @Override
    public Flux<BootcampResponse> listBootcamps(PageRequest pageRequest) {
        log.info("Entrando a listBootcamps con PageRequest: {}", pageRequest);
        return bootcampRepo.findBootcamps(pageRequest)
                .doOnNext(entity -> log.info("Entidad recibida del repositorio: {}", entity))
                .collectList()
                .doOnNext(bootcamps -> log.info("Lista de bootcamps obtenidos: {}", bootcamps))
                .flatMapMany(bootcamps -> {
                    log.info("Procesando lista de bootcamps para obtener capabilities");
                    List<UUID> bootcampIds = bootcamps.stream().map(Bootcamp::getId).toList();
                    return capabilitiesClient.fetchCapabilitiesBatch(bootcampIds)
                            .collectList()
                            .doOnNext(batchList -> log.info("CapabilitiesBatchResponse recibidos: {}", batchList))
                            .flatMapMany(batchList -> {
                                log.info("Mapeando capabilities y tecnologías");
                                var capabilitiesMap = BootcampResponseMapper.mapCapabilities(batchList);
                                var techMap = BootcampResponseMapper.mapTechnologies(batchList);

                                List<BootcampResponse> responses = bootcamps.stream().map(b -> {
                                    BootcampResponse resp = new BootcampResponse();
                                    resp.setId(b.getId());
                                    resp.setName(b.getName());
                                    resp.setCapabilities(capabilitiesMap.getOrDefault(b.getId(), List.of()));
                                    resp.setTechnologies(techMap.getOrDefault(b.getId(), List.of()));
                                    log.info("BootcampResponse creado: {}", resp);
                                    return resp;
                                }).collect(Collectors.toCollection(java.util.ArrayList::new));

                                log.info("Lista de BootcampResponse creada: {}", responses);
                                // Orden y paginación
                                Comparator<BootcampResponse> comparator;
                                if ("name".equalsIgnoreCase(pageRequest.getSortBy())) {
                                    comparator = Comparator.comparing(BootcampResponse::getName, Comparator.nullsLast(String::compareToIgnoreCase));
                                } else if ("capabilities".equalsIgnoreCase(pageRequest.getSortBy())) {
                                    comparator = Comparator.comparing(r -> r.getCapabilities().size());
                                } else {
                                    comparator = Comparator.comparing(BootcampResponse::getName);
                                }
                                if ("desc".equalsIgnoreCase(pageRequest.getDirection())) {
                                    comparator = comparator.reversed();
                                }
                                responses.sort(comparator);

                                int from = pageRequest.getPage() * pageRequest.getSize();
                                int to = Math.min(from + pageRequest.getSize(), responses.size());
                                List<BootcampResponse> paged = from < to ? new java.util.ArrayList<>(responses).subList(from, to) : java.util.Collections.emptyList();

                                return Flux.fromIterable(paged);
                            });
                });
    }
}
