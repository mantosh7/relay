package com.relay.relay.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class RabbitMQConfig {

    public static final String JOB_QUEUE = "job.queue";
    public static final String JOB_EXCHANGE = "job.exchange";
    public static final String JOB_ROUTING_KEY = "job.routingkey";

    public static final String RETRY_QUEUE = "job.retry.queue";
    public static final String RETRY_EXCHANGE = "job.retry.exchange";
    public static final String RETRY_ROUTING_KEY = "job.retry.routingkey";

    public static final String DLQ_QUEUE = "job.dlq";
    public static final String DLQ_EXCHANGE = "job.dlq.exchange";
    public static final String DLQ_ROUTING_KEY = "job.dlq.routingkey";

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
    public Queue retryQueue() {
        Map<String, Object> args = new HashMap<>();
        args.put("x-dead-letter-exchange", JOB_EXCHANGE);
        args.put("x-dead-letter-routing-key", JOB_ROUTING_KEY);
        args.put("x-message-ttl", 10000); // 10 second wait
        return new Queue(RETRY_QUEUE, true, false, false, args);
    }

    @Bean
    public DirectExchange retryExchange() {
        return new DirectExchange(RETRY_EXCHANGE);
    }

    @Bean
    public Binding retryBinding(Queue retryQueue, DirectExchange retryExchange) {
        return BindingBuilder.bind(retryQueue).to(retryExchange).with(RETRY_ROUTING_KEY);
    }

    @Bean
    public Queue dlqQueue() {
        return new Queue(DLQ_QUEUE, true);
    }

    @Bean
    public DirectExchange dlqExchange() {
        return new DirectExchange(DLQ_EXCHANGE);
    }

    @Bean
    public Binding dlqBinding(Queue dlqQueue, DirectExchange dlqExchange) {
        return BindingBuilder.bind(dlqQueue).to(dlqExchange).with(DLQ_ROUTING_KEY);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new JacksonJsonMessageConverter();
    }
}