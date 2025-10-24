package com.reactive.bootcamps.infraestructure.driven.persitence.adapter;


import com.reactive.bootcamps.application.ports.out.BootcampQueryRepository;
import com.reactive.bootcamps.domain.model.Bootcamp;
import com.reactive.bootcamps.domain.model.PageRequest;
import com.reactive.bootcamps.infraestructure.driven.persitence.mapper.BootcampMapper;
import com.reactive.bootcamps.infraestructure.driven.persitence.repository.BootcampJpaRepository;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class BootcampQueryRepositoryAdapter implements BootcampQueryRepository {

    private final BootcampJpaRepository jpaRepository;

    public BootcampQueryRepositoryAdapter(BootcampJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Flux<Bootcamp> findBootcamps(PageRequest pageRequest) {
        return jpaRepository.findAll()
                .map(BootcampMapper::toDomainForList)
                .collectList()
                .flatMapMany(list -> {
                    // Convierte la lista a mutable antes de ordenar
                    List<Bootcamp> mutableList = new java.util.ArrayList<>(list);

                    Comparator<Bootcamp> comparator = Comparator.comparing(Bootcamp::getName, String::compareToIgnoreCase);
                    if ("desc".equalsIgnoreCase(pageRequest.getDirection())) {
                        comparator = comparator.reversed();
                    }
                    mutableList.sort(comparator);

                    int from = pageRequest.getPage() * pageRequest.getSize();
                    int to = Math.min(from + pageRequest.getSize(), mutableList.size());
                    List<Bootcamp> paged = from < to ? mutableList.subList(from, to) : java.util.Collections.emptyList();

                    return Flux.fromIterable(paged);
                });
    }
}
