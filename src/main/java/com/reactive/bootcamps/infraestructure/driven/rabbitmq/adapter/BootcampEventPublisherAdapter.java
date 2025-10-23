package com.reactive.bootcamps.infraestructure.driven.rabbitmq.adapter;

import com.reactive.bootcamps.application.ports.out.BootcampEventPublisher;
import com.reactive.bootcamps.domain.event.BootcampCreatedEvent;
import com.reactive.bootcamps.infraestructure.driven.rabbitmq.config.RabbitMQConfig;
import com.reactive.bootcamps.infraestructure.driven.rabbitmq.dto.BootcampCreatedEventDTO;
import com.reactive.bootcamps.infraestructure.driven.rabbitmq.mapper.BootcampCreatedEventMapper;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class BootcampEventPublisherAdapter implements BootcampEventPublisher {
    private final RabbitTemplate rabbitTemplate;

    public BootcampEventPublisherAdapter(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public Mono<Void> publishBootcampCreated(BootcampCreatedEvent event) {
        return Mono.create(sink -> {
            try {
                BootcampCreatedEventDTO dto = BootcampCreatedEventMapper.toDTO(event);
                MessagePostProcessor typeIdProcessor = message -> {
                    message.getMessageProperties().setHeader("__TypeId__", "bootcampCapabilitiesEvent");
                    return message;
                };
                rabbitTemplate.convertAndSend(
                    RabbitMQConfig.BOOTCAMP_EXCHANGE,
                    RabbitMQConfig.BOOTCAMP_ROUTING_KEY,
                    dto,
                    typeIdProcessor
                );
                sink.success();
            } catch (Exception e) {
                sink.error(e);
            }
        });
    }
}
