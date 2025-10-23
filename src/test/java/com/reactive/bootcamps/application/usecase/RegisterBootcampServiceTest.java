package com.reactive.bootcamps.application.usecase;

import com.reactive.bootcamps.application.ports.out.BootcampRepository;
import com.reactive.bootcamps.application.ports.out.BootcampEventPublisher;
import com.reactive.bootcamps.domain.model.Bootcamp;
import com.reactive.bootcamps.domain.model.CapabilityRef;
import com.reactive.bootcamps.domain.utils.DomainException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class RegisterBootcampServiceTest {
    private BootcampRepository repository;
    private BootcampEventPublisher publisher;
    private RegisterBootcampService service;

    @BeforeEach
    void setUp() {
        repository = mock(BootcampRepository.class);
        publisher = mock(BootcampEventPublisher.class);
        service = new RegisterBootcampService(repository, publisher);
    }

    @Test
    void shouldRegisterBootcampAndPublishEvent() {
        List<CapabilityRef> capabilities = List.of(new CapabilityRef(UUID.randomUUID(), "Backend"));
        Bootcamp bootcamp = Bootcamp.create("Java Bootcamp", "Desc", LocalDate.now(), "2 meses", capabilities);

        when(repository.save(any())).thenReturn(Mono.just(bootcamp));
        when(publisher.publishBootcampCreated(any())).thenReturn(Mono.empty());

        Mono<Bootcamp> result = service.registerBootcamp(
                bootcamp.getName(),
                bootcamp.getDescription(),
                bootcamp.getLaunchDate(),
                bootcamp.getDuration(),
                capabilities
        );

        StepVerifier.create(result)
                .expectNextMatches(b -> b.getName().equals("Java Bootcamp"))
                .verifyComplete();

        verify(repository).save(any());
        verify(publisher).publishBootcampCreated(any());
    }

    @Test
    void shouldThrowDomainExceptionForInvalidData() {
        List<CapabilityRef> emptyCapabilities = List.of();
        Mono<Bootcamp> result = service.registerBootcamp(
                "", // nombre vacío
                "Desc",
                LocalDate.now(),
                "2 meses",
                emptyCapabilities
        );

        StepVerifier.create(result)
                .expectErrorMatches(e -> e instanceof DomainException && e.getMessage().equals(DomainException.NAME_REQUIRED))
                .verify();
    }

    @Test
    void shouldPropagateRepositoryErrorAndNotPublishEvent() {
        List<CapabilityRef> capabilities = List.of(new CapabilityRef(UUID.randomUUID(), "Backend"));
        when(repository.save(any())).thenReturn(Mono.error(new RuntimeException("DB error")));

        Mono<Bootcamp> result = service.registerBootcamp(
                "Java Bootcamp",
                "Desc",
                LocalDate.now(),
                "2 meses",
                capabilities
        );

        StepVerifier.create(result)
                .expectErrorMatches(e -> e.getMessage().equals("DB error"))
                .verify();

        verify(repository).save(any());
        verify(publisher, never()).publishBootcampCreated(any());
    }

    @Test
    void shouldPropagatePublisherError() {
        List<CapabilityRef> capabilities = List.of(new CapabilityRef(UUID.randomUUID(), "Backend"));
        Bootcamp bootcamp = Bootcamp.create("Java Bootcamp", "Desc", LocalDate.now(), "2 meses", capabilities);

        when(repository.save(any())).thenReturn(Mono.just(bootcamp));
        when(publisher.publishBootcampCreated(any())).thenReturn(Mono.error(new RuntimeException("Publisher error")));

        Mono<Bootcamp> result = service.registerBootcamp(
                bootcamp.getName(),
                bootcamp.getDescription(),
                bootcamp.getLaunchDate(),
                bootcamp.getDuration(),
                capabilities
        );

        StepVerifier.create(result)
                .expectErrorMatches(e -> e.getMessage().equals("Publisher error"))
                .verify();

        verify(repository).save(any());
        verify(publisher).publishBootcampCreated(any());
    }

    @Test
    void shouldRegisterBootcampWithOneCapability() {
        List<CapabilityRef> capabilities = List.of(new CapabilityRef(UUID.randomUUID(), "Backend"));
        Bootcamp bootcamp = Bootcamp.create("Bootcamp Uno", "Desc", LocalDate.now(), "1 mes", capabilities);
        when(repository.save(any())).thenReturn(Mono.just(bootcamp));
        when(publisher.publishBootcampCreated(any())).thenReturn(Mono.empty());
        Mono<Bootcamp> result = service.registerBootcamp(
                bootcamp.getName(), bootcamp.getDescription(), bootcamp.getLaunchDate(), bootcamp.getDuration(), capabilities);
        StepVerifier.create(result)
                .expectNextMatches(b -> b.getName().equals("Bootcamp Uno"))
                .verifyComplete();
    }

    @Test
    void shouldRegisterBootcampWithFourCapabilities() {
        List<CapabilityRef> capabilities = List.of(
                new CapabilityRef(UUID.randomUUID(), "Backend"),
                new CapabilityRef(UUID.randomUUID(), "Frontend"),
                new CapabilityRef(UUID.randomUUID(), "DevOps"),
                new CapabilityRef(UUID.randomUUID(), "QA")
        );
        Bootcamp bootcamp = Bootcamp.create("Bootcamp Cuatro", "Desc", LocalDate.now(), "4 meses", capabilities);
        when(repository.save(any())).thenReturn(Mono.just(bootcamp));
        when(publisher.publishBootcampCreated(any())).thenReturn(Mono.empty());
        Mono<Bootcamp> result = service.registerBootcamp(
                bootcamp.getName(), bootcamp.getDescription(), bootcamp.getLaunchDate(), bootcamp.getDuration(), capabilities);
        StepVerifier.create(result)
                .expectNextMatches(b -> b.getName().equals("Bootcamp Cuatro"))
                .verifyComplete();
    }

    @Test
    void shouldThrowDomainExceptionForTooManyCapabilities() {
        List<CapabilityRef> capabilities = List.of(
                new CapabilityRef(UUID.randomUUID(), "Backend"),
                new CapabilityRef(UUID.randomUUID(), "Frontend"),
                new CapabilityRef(UUID.randomUUID(), "DevOps"),
                new CapabilityRef(UUID.randomUUID(), "QA"),
                new CapabilityRef(UUID.randomUUID(), "Extra")
        );
        Mono<Bootcamp> result = service.registerBootcamp(
                "Bootcamp Exceso", "Desc", LocalDate.now(), "5 meses", capabilities);
        StepVerifier.create(result)
                .expectErrorMatches(e -> e instanceof DomainException && e.getMessage().equals(DomainException.CAPABILITIES_MAX))
                .verify();
    }

    @Test
    void shouldThrowDomainExceptionForNullLaunchDate() {
        List<CapabilityRef> capabilities = List.of(new CapabilityRef(UUID.randomUUID(), "Backend"));
        Mono<Bootcamp> result = service.registerBootcamp(
                "Bootcamp Sin Fecha", "Desc", null, "1 mes", capabilities);
        StepVerifier.create(result)
                .expectErrorMatches(e -> e instanceof DomainException && e.getMessage().equals(DomainException.LAUNCH_DATE_REQUIRED))
                .verify();
    }

    @Test
    void shouldThrowDomainExceptionForEmptyDuration() {
        List<CapabilityRef> capabilities = List.of(new CapabilityRef(UUID.randomUUID(), "Backend"));
        Mono<Bootcamp> result = service.registerBootcamp(
                "Bootcamp Sin Duracion", "Desc", LocalDate.now(), "", capabilities);
        StepVerifier.create(result)
                .expectErrorMatches(e -> e instanceof DomainException && e.getMessage().equals(DomainException.DURATION_REQUIRED))
                .verify();
    }

    @Test
    void shouldThrowDomainExceptionForEmptyDescription() {
        List<CapabilityRef> capabilities = List.of(new CapabilityRef(UUID.randomUUID(), "Backend"));
        Mono<Bootcamp> result = service.registerBootcamp(
                "Bootcamp Sin Desc", "", LocalDate.now(), "1 mes", capabilities);
        StepVerifier.create(result)
                .expectErrorMatches(e -> e instanceof DomainException && e.getMessage().equals(DomainException.DESCRIPTION_REQUIRED))
                .verify();
    }

    @Test
    void shouldThrowDomainExceptionForEmptyCapabilitiesEvenWithValidName() {
        List<CapabilityRef> emptyCapabilities = List.of();
        Mono<Bootcamp> result = service.registerBootcamp(
                "Bootcamp Sin Cap", "Desc", LocalDate.now(), "1 mes", emptyCapabilities);
        StepVerifier.create(result)
                .expectErrorMatches(e -> e instanceof DomainException && e.getMessage().equals(DomainException.CAPABILITIES_REQUIRED))
                .verify();
    }

    @Test
    void shouldThrowDomainExceptionForCapabilityWithEmptyName() {
        List<CapabilityRef> capabilities = List.of(new CapabilityRef(UUID.randomUUID(), ""));
        Mono<Bootcamp> result = service.registerBootcamp(
                "Bootcamp Capacidad Vacia", "Desc", LocalDate.now(), "1 mes", capabilities);
        StepVerifier.create(result)
                .expectErrorMatches(e -> e instanceof DomainException && e.getMessage().equals(DomainException.CAPABILITY_NAME_REQUIRED))
                .verify();
    }
}
