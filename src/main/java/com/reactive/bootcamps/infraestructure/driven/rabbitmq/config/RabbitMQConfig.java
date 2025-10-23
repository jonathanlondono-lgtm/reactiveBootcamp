package com.reactive.bootcamps.infraestructure.driven.rabbitmq.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Configuration
public class RabbitMQConfig {

    private static final Logger logger = LoggerFactory.getLogger(RabbitMQConfig.class);

        public static final String BOOTCAMP_QUEUE = "bootcamp.queue";
    public static final String BOOTCAMP_EXCHANGE = "bootcamp.exchange";
    public static final String BOOTCAMP_ROUTING_KEY = "bootcamp.routing.key";

    @Bean
    public Queue technologiesQueue() {
        logger.info("Creando queue: {}", BOOTCAMP_QUEUE);
        return new Queue(BOOTCAMP_QUEUE, true);
    }

    @Bean
    public DirectExchange technologiesExchange() {
        logger.info("Creando exchange: {}", BOOTCAMP_EXCHANGE);
        return new DirectExchange(BOOTCAMP_EXCHANGE);
    }

    @Bean
    public Binding bindingTechnologiesQueue(Queue technologiesQueue, DirectExchange technologiesExchange) {
        logger.info("Creando binding entre queue '{}' y exchange '{}' con routingKey '{}'", BOOTCAMP_QUEUE, BOOTCAMP_EXCHANGE, BOOTCAMP_ROUTING_KEY);
        return BindingBuilder.bind(technologiesQueue)
                .to(technologiesExchange)
                .with(BOOTCAMP_ROUTING_KEY);
    }
    @Bean
    public Jackson2JsonMessageConverter jackson2JsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory,
                                         Jackson2JsonMessageConverter converter) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(converter);
        return template;
    }
}
