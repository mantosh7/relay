package com.relay.relay.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String JOB_QUEUE = "job.queue";
    public static final String JOB_EXCHANGE = "job.exchange";
    public static final String JOB_ROUTING_KEY = "job.routingkey";

    @Bean
    public Queue jobQueue() {
        return new Queue(JOB_QUEUE, true);
    }

    @Bean
    public DirectExchange jobExchange() {
        return new DirectExchange(JOB_EXCHANGE);
    }

    @Bean
    public Binding binding(Queue jobQueue, DirectExchange jobExchange) {
        return BindingBuilder.bind(jobQueue).to(jobExchange).with(JOB_ROUTING_KEY);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new JacksonJsonMessageConverter();
    }
}