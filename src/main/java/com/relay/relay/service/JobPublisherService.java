package com.relay.relay.service;

import com.relay.relay.config.RabbitMQConfig;
import com.relay.relay.dto.JobMessageDTO;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
public class JobPublisherService {

    private final RabbitTemplate rabbitTemplate;

    public JobPublisherService(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void publishJob(JobMessageDTO jobMessage) {
        MessagePostProcessor priorityProcessor = message -> {
            message.getMessageProperties().setPriority(jobMessage.getPriority().getLevel());
            return message;
        };

        rabbitTemplate.convertAndSend(
                RabbitMQConfig.JOB_EXCHANGE,
                RabbitMQConfig.JOB_ROUTING_KEY,
                jobMessage,
                priorityProcessor
        );
    }
}