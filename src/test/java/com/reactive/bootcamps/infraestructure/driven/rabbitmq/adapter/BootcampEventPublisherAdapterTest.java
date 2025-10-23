package com.reactive.bootcamps.infraestructure.driven.rabbitmq.adapter;

import com.reactive.bootcamps.domain.event.BootcampCreatedEvent;
import com.reactive.bootcamps.infraestructure.driven.rabbitmq.config.RabbitMQConfig;
import com.reactive.bootcamps.infraestructure.driven.rabbitmq.dto.BootcampCreatedEventDTO;
import com.reactive.bootcamps.infraestructure.driven.rabbitmq.mapper.BootcampCreatedEventMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class BootcampEventPublisherAdapterTest {
    private RabbitTemplate rabbitTemplate;
    private BootcampEventPublisherAdapter adapter;

    @BeforeEach
    void setUp() {
        rabbitTemplate = mock(RabbitTemplate.class);
        adapter = new BootcampEventPublisherAdapter(rabbitTemplate);
    }

    @Test
    void shouldMapAndSendEventWithTypeIdHeader() {
        UUID bootcampId = UUID.randomUUID();
        List<UUID> capabilityIds = List.of(UUID.randomUUID());
        BootcampCreatedEvent event = new BootcampCreatedEvent(bootcampId, capabilityIds);
        BootcampCreatedEventDTO dto = BootcampCreatedEventMapper.toDTO(event);

        doNothing().when(rabbitTemplate).convertAndSend(
                eq(RabbitMQConfig.BOOTCAMP_EXCHANGE),
                eq(RabbitMQConfig.BOOTCAMP_ROUTING_KEY),
                eq(dto),
                any(MessagePostProcessor.class)
        );

        Mono<Void> result = adapter.publishBootcampCreated(event);
        StepVerifier.create(result)
                .verifyComplete();

        verify(rabbitTemplate).convertAndSend(
                eq(RabbitMQConfig.BOOTCAMP_EXCHANGE),
                eq(RabbitMQConfig.BOOTCAMP_ROUTING_KEY),
                any(BootcampCreatedEventDTO.class),
                any(MessagePostProcessor.class)
        );
    }

    @Test
    void shouldPropagateRabbitTemplateError() {
        UUID bootcampId = UUID.randomUUID();
        List<UUID> capabilityIds = List.of(UUID.randomUUID());
        BootcampCreatedEvent event = new BootcampCreatedEvent(bootcampId, capabilityIds);
        BootcampCreatedEventDTO dto = BootcampCreatedEventMapper.toDTO(event);

        doThrow(new RuntimeException("Rabbit error")).when(rabbitTemplate).convertAndSend(
                eq(RabbitMQConfig.BOOTCAMP_EXCHANGE),
                eq(RabbitMQConfig.BOOTCAMP_ROUTING_KEY),
                any(BootcampCreatedEventDTO.class),
                any(MessagePostProcessor.class)
        );

        Mono<Void> result = adapter.publishBootcampCreated(event);
        StepVerifier.create(result)
                .expectErrorMatches(e -> e.getMessage().equals("Rabbit error"))
                .verify();
    }
}
