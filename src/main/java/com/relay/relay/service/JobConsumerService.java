package com.relay.relay.service;

import com.relay.relay.config.RabbitMQConfig;
import com.relay.relay.dto.JobMessageDTO;
import com.relay.relay.entity.Job;
import com.relay.relay.enums.JobStatus;
import com.relay.relay.repository.JobRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
public class JobConsumerService {

    private final JobRepository jobRepository;
    private final RabbitTemplate rabbitTemplate;

    public JobConsumerService(JobRepository jobRepository, RabbitTemplate rabbitTemplate) {
        this.jobRepository = jobRepository;
        this.rabbitTemplate = rabbitTemplate;
    }

    @RabbitListener(queues = RabbitMQConfig.JOB_QUEUE)
    public void consumeJob(JobMessageDTO message) {
        System.out.println("Received job: " + message.getJobId());

        Job job = jobRepository.findById(message.getJobId())
                .orElseThrow(() -> new RuntimeException("Job not found: " + message.getJobId()));

        job.setStatus(JobStatus.PROCESSING);
        jobRepository.save(job);

        try {
            processJob(message);

            job.setStatus(JobStatus.SUCCESS);
            jobRepository.save(job);
            System.out.println("Job completed successfully: " + message.getJobId());

        } catch (Exception e) {
            handleFailure(job, message, e);
        }
    }

    private void processJob(JobMessageDTO message) throws Exception {
        System.out.println("Processing job type: " + message.getJobType());
        System.out.println("Payload: " + message.getPayload());

        Thread.sleep(2000);
    }

    private void handleFailure(Job job, JobMessageDTO message, Exception e) {
        int newRetryCount = job.getRetryCount() + 1;
        job.setRetryCount(newRetryCount);
        job.setErrorMessage(e.getMessage());

        if (newRetryCount >= job.getMaxRetries()) {
            job.setStatus(JobStatus.FAILED);
            jobRepository.save(job);

            rabbitTemplate.convertAndSend(
                    RabbitMQConfig.DLQ_EXCHANGE,
                    RabbitMQConfig.DLQ_ROUTING_KEY,
                    message
            );
            System.out.println("Job moved to DLQ after " + newRetryCount + " attempts: " + job.getId());

        } else {
            job.setStatus(JobStatus.PENDING);
            jobRepository.save(job);

            rabbitTemplate.convertAndSend(
                    RabbitMQConfig.RETRY_EXCHANGE,
                    RabbitMQConfig.RETRY_ROUTING_KEY,
                    message
            );
            System.out.println("Job sent to retry queue, attempt " + newRetryCount + ": " + job.getId());
        }
    }
}