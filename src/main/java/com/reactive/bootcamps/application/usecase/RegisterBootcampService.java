package com.reactive.bootcamps.application.usecase;

import com.reactive.bootcamps.application.ports.in.RegisterBootcampUseCase;
import com.reactive.bootcamps.application.ports.out.BootcampEventPublisher;
import com.reactive.bootcamps.application.ports.out.BootcampRepository;
import com.reactive.bootcamps.domain.event.BootcampCreatedEvent;
import com.reactive.bootcamps.domain.model.Bootcamp;
import com.reactive.bootcamps.domain.model.CapabilityRef;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class RegisterBootcampService implements RegisterBootcampUseCase {
    private final BootcampRepository bootcampRepository;
    private final BootcampEventPublisher eventPublisher;

    public RegisterBootcampService(BootcampRepository bootcampRepository, BootcampEventPublisher eventPublisher) {
        this.bootcampRepository = bootcampRepository;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public Mono<Bootcamp> registerBootcamp(String name, String description, LocalDate launchDate, String duration, List<CapabilityRef> capabilities) {
        return Mono.fromCallable(() -> Bootcamp.create(name, description, launchDate, duration, capabilities))
            .flatMap(bootcamp -> bootcampRepository.save(bootcamp)
                .flatMap(saved -> eventPublisher.publishBootcampCreated(
                        new com.reactive.bootcamps.domain.event.BootcampCreatedEvent(
                                saved.getId(),
                                capabilities.stream().map(CapabilityRef::getId).toList()
                        ))
                        .thenReturn(saved)
                )
            );
    }
}
