package com.reactive.bootcamps.infraestructure.driven.persitence.repository;

import com.reactive.bootcamps.infraestructure.driven.persitence.entity.BootcampEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;
import java.util.UUID;
import java.time.LocalDate;

public interface BootcampJpaRepository extends ReactiveCrudRepository<BootcampEntity, UUID> {
    @Query("INSERT INTO bootcamp (id, name, description, launch_date, duration) VALUES (:id, :name, :description, :launchDate, :duration)")
    Mono<Void> insertBootcamp(UUID id, String name, String description, LocalDate launchDate, String duration);
}
