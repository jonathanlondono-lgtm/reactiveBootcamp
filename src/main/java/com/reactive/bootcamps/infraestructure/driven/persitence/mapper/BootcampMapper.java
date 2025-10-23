package com.reactive.bootcamps.infraestructure.driven.persitence.mapper;

import com.reactive.bootcamps.domain.model.Bootcamp;
import com.reactive.bootcamps.infraestructure.driven.persitence.entity.BootcampEntity;

public class BootcampMapper {
    public static BootcampEntity toEntity(Bootcamp bootcamp) {
        return new BootcampEntity(
                bootcamp.getId(),
                bootcamp.getName(),
                bootcamp.getDescription(),
                bootcamp.getLaunchDate(),
                bootcamp.getDuration()
        );
    }

    public static Bootcamp toDomain(BootcampEntity entity) {
        return Bootcamp.create(
                entity.getName(),
                entity.getDescription(),
                entity.getLaunchDate(),
                entity.getDuration(),
                java.util.List.of() // capabilities vacío
        );
    }
}

