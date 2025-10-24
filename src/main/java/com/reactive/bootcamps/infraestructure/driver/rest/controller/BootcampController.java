package com.reactive.bootcamps.infraestructure.driver.rest.controller;

import com.reactive.bootcamps.application.ports.in.RegisterBootcampUseCase;
import com.reactive.bootcamps.application.ports.in.ListBootcampsQuery;
import com.reactive.bootcamps.domain.model.Bootcamp;
import com.reactive.bootcamps.domain.model.PageRequest;
import com.reactive.bootcamps.infraestructure.driver.rest.dto.RegisterBootcampRequest;
import com.reactive.bootcamps.infraestructure.driver.rest.dto.BootcampResponse;
import com.reactive.bootcamps.infraestructure.driver.rest.mapper.RegisterBootcampMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*; // 👈 este incluye el correcto @RequestBody
import reactor.core.publisher.Mono;
import reactor.core.publisher.Flux;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/bootcamps")
public class BootcampController {

    private static final Logger logger = LoggerFactory.getLogger(BootcampController.class);
    private final RegisterBootcampUseCase registerBootcampUseCase;
    private final ListBootcampsQuery listBootcampsQuery;

    public BootcampController(RegisterBootcampUseCase registerBootcampUseCase, ListBootcampsQuery listBootcampsQuery) {
        this.registerBootcampUseCase = registerBootcampUseCase;
        this.listBootcampsQuery = listBootcampsQuery;
    }

    @Operation(
            summary = "Registrar un nuevo bootcamp",
            description = "Crea un bootcamp con las capacidades especificadas",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody( // ✅ Swagger: solo para documentación
                    description = "Datos para crear el bootcamp",
                    required = true,
                    content = @Content(schema = @Schema(implementation = RegisterBootcampRequest.class))
            )
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Bootcamp creado exitosamente",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Bootcamp.class))),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
    })
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Bootcamp> registerBootcamp(
            @org.springframework.web.bind.annotation.RequestBody RegisterBootcampRequest request // ✅ Spring: para leer el JSON real
    ) {
        logger.info("Petición recibida para registrar bootcamp: name={}, description={}, launchDate={}, duration={}",
                request.getName(), request.getDescription(), request.getLaunchDate(), request.getDuration());
        logger.info("Capacidades recibidas: {}", request.getCapabilities());

        return registerBootcampUseCase.registerBootcamp(
                request.getName(),
                request.getDescription(),
                request.getLaunchDate(),
                request.getDuration(),
                RegisterBootcampMapper.toDomainCapabilities(request.getCapabilities())
        );
    }

    @Operation(
            summary = "Listar bootcamps",
            description = "Obtiene una lista de bootcamps con capacidades y tecnologías asociadas, con soporte para paginación y orden."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de bootcamps obtenida exitosamente",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = BootcampResponse.class))),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
    })
    @GetMapping(value = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
    public Flux<BootcampResponse> listBootcamps(
            @RequestParam int page,
            @RequestParam int size,
            @RequestParam String sortBy,
            @RequestParam String direction
    ) {
        logger.info("Petición recibida para listar bootcamps: page={}, size={}, sortBy={}, direction={}", page, size, sortBy, direction);
        PageRequest pageRequest = new PageRequest(page, size, sortBy, direction);
        return listBootcampsQuery.listBootcamps(pageRequest);
    }
}
