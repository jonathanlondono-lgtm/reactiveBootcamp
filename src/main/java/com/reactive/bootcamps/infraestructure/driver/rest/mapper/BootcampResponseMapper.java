package com.reactive.bootcamps.infraestructure.driver.rest.mapper;

import com.reactive.bootcamps.infraestructure.driver.rest.dto.BootcampResponse;
import com.reactive.bootcamps.infraestructure.driver.rest.dto.CapabilitiesBatchResponse;

import java.util.*;
import java.util.stream.Collectors;

public class BootcampResponseMapper {

    public static Map<UUID, List<BootcampResponse.CapabilityResponse>> mapCapabilities(List<CapabilitiesBatchResponse> batchList) {
        Map<UUID, List<BootcampResponse.CapabilityResponse>> capabilitiesMap = new HashMap<>();
        batchList.forEach(batch -> {
            capabilitiesMap.put(batch.getBootcampId(), batch.getCapabilities().stream()
                    .map(cap -> {
                        BootcampResponse.CapabilityResponse cr = new BootcampResponse.CapabilityResponse();
                        cr.setId(cap.getCapabilityId());
                        cr.setName(cap.getName());
                        return cr;
                    }).collect(Collectors.toList()));
        });
        return capabilitiesMap;
    }

    public static Map<UUID, List<BootcampResponse.TechnologyResponse>> mapTechnologies(List<CapabilitiesBatchResponse> batchList) {
        Map<UUID, List<BootcampResponse.TechnologyResponse>> techMap = new HashMap<>();
        batchList.forEach(batch -> {
            techMap.put(batch.getBootcampId(), batch.getCapabilities().stream()
                    .flatMap(cap -> cap.getTechnologies().stream())
                    .map(tech -> {
                        BootcampResponse.TechnologyResponse tr = new BootcampResponse.TechnologyResponse();
                        tr.setId(tech.getId());
                        tr.setName(tech.getName());
                        return tr;
                    }).distinct().collect(Collectors.toList()));
        });
        return techMap;
    }
}
