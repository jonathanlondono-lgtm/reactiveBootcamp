package com.reactive.bootcamps.infraestructure.driver.rest.controller;

import com.reactive.bootcamps.application.ports.in.RegisterBootcampUseCase;
import com.reactive.bootcamps.domain.model.Bootcamp;
import com.reactive.bootcamps.infraestructure.driver.rest.dto.RegisterBootcampRequest;
import com.reactive.bootcamps.infraestructure.driver.rest.mapper.RegisterBootcampMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/bootcamps")
public class BootcampController {
    private final RegisterBootcampUseCase registerBootcampUseCase;

    public BootcampController(RegisterBootcampUseCase registerBootcampUseCase) {
        this.registerBootcampUseCase = registerBootcampUseCase;
    }

    @Operation(
        summary = "Registrar un nuevo bootcamp",
        description = "Crea un bootcamp con las capacidades especificadas",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
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
    public Mono<Bootcamp> registerBootcamp(@RequestBody RegisterBootcampRequest request) {
        return registerBootcampUseCase.registerBootcamp(
                request.getName(),
                request.getDescription(),
                request.getLaunchDate(),
                request.getDuration(),
                RegisterBootcampMapper.toDomainCapabilities(request.getCapabilities())
        );
    }
}
