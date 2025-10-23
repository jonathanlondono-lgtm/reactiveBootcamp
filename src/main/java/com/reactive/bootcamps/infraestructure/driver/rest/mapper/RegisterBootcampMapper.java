package com.reactive.bootcamps.infraestructure.driver.rest.mapper;

import com.reactive.bootcamps.domain.model.CapabilityRef;
import com.reactive.bootcamps.infraestructure.driver.rest.dto.CapabilityRefRequest;
import java.util.List;
import java.util.stream.Collectors;

public class RegisterBootcampMapper {
    public static List<CapabilityRef> toDomainCapabilities(List<CapabilityRefRequest> requests) {
        return requests.stream()
                .map(req -> new CapabilityRef(req.getId(), req.getName()))
                .collect(Collectors.toList());
    }
}
