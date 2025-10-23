package com.reactive.bootcamps.infraestructure.driven.persitence.adapter;

import com.reactive.bootcamps.domain.model.Bootcamp;
import com.reactive.bootcamps.domain.model.CapabilityRef;
import com.reactive.bootcamps.infraestructure.driven.persitence.entity.BootcampEntity;
import com.reactive.bootcamps.infraestructure.driven.persitence.mapper.BootcampMapper;
import com.reactive.bootcamps.infraestructure.driven.persitence.repository.BootcampJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class BootcampRepositoryAdapterTest {
    private BootcampJpaRepository jpaRepository;
    private BootcampRepositoryAdapter adapter;

    @BeforeEach
    void setUp() {
        jpaRepository = mock(BootcampJpaRepository.class);
        adapter = new BootcampRepositoryAdapter(jpaRepository);
    }

    @Test
    void shouldMapAndInsertBootcampCorrectly() {
        List<CapabilityRef> capabilities = List.of(new CapabilityRef(UUID.randomUUID(), "Backend"));
        Bootcamp bootcamp = Bootcamp.create("Test", "Desc", LocalDate.now(), "1 mes", capabilities);
        BootcampEntity entity = BootcampMapper.toEntity(bootcamp);
        when(jpaRepository.insertBootcamp(
                entity.getId(), entity.getName(), entity.getDescription(), entity.getLaunchDate(), entity.getDuration()
        )).thenReturn(Mono.empty());

        Mono<Bootcamp> result = adapter.save(bootcamp);
        StepVerifier.create(result)
                .expectNext(bootcamp)
                .verifyComplete();

        ArgumentCaptor<UUID> idCaptor = ArgumentCaptor.forClass(UUID.class);
        ArgumentCaptor<String> nameCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> descCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<LocalDate> dateCaptor = ArgumentCaptor.forClass(LocalDate.class);
        ArgumentCaptor<String> durCaptor = ArgumentCaptor.forClass(String.class);
        verify(jpaRepository).insertBootcamp(
                idCaptor.capture(), nameCaptor.capture(), descCaptor.capture(), dateCaptor.capture(), durCaptor.capture()
        );
        // Puedes agregar asserts para verificar los valores capturados si lo deseas
    }

    @Test
    void shouldPropagateRepositoryError() {
        List<CapabilityRef> capabilities = List.of(new CapabilityRef(UUID.randomUUID(), "Backend"));
        Bootcamp bootcamp = Bootcamp.create("Test", "Desc", LocalDate.now(), "1 mes", capabilities);
        BootcampEntity entity = BootcampMapper.toEntity(bootcamp);
        when(jpaRepository.insertBootcamp(
                entity.getId(), entity.getName(), entity.getDescription(), entity.getLaunchDate(), entity.getDuration()
        )).thenReturn(Mono.error(new RuntimeException("DB error")));

        Mono<Bootcamp> result = adapter.save(bootcamp);
        StepVerifier.create(result)
                .expectErrorMatches(e -> e.getMessage().equals("DB error"))
                .verify();
    }
}
