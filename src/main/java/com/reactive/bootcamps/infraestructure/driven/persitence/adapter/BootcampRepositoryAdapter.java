package com.reactive.bootcamps.infraestructure.driven.persitence.adapter;

import com.reactive.bootcamps.application.ports.out.BootcampRepository;
import com.reactive.bootcamps.domain.model.Bootcamp;
import com.reactive.bootcamps.infraestructure.driven.persitence.entity.BootcampEntity;
import com.reactive.bootcamps.infraestructure.driven.persitence.mapper.BootcampMapper;
import com.reactive.bootcamps.infraestructure.driven.persitence.repository.BootcampJpaRepository;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Component
public class BootcampRepositoryAdapter implements BootcampRepository {
    private final BootcampJpaRepository jpaRepository;

    public BootcampRepositoryAdapter(BootcampJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Mono<Bootcamp> save(Bootcamp bootcamp) {
        BootcampEntity entity = BootcampMapper.toEntity(bootcamp);
        UUID id = entity.getId();

        return jpaRepository.insertBootcamp(
                entity.getId(),
                entity.getName(),
                entity.getDescription(),
                entity.getLaunchDate(),
                entity.getDuration()
        ).thenReturn(bootcamp);
    }
}
