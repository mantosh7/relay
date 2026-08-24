package com.relay.relay.service;

import com.relay.relay.config.RabbitMQConfig;
import com.relay.relay.dto.JobMessageDTO;
import com.relay.relay.entity.Job;
import com.relay.relay.enums.JobStatus;
import com.relay.relay.repository.JobRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class JobConsumerService {

    private final JobRepository jobRepository;

    public JobConsumerService(JobRepository jobRepository) {
        this.jobRepository = jobRepository;
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
            job.setStatus(JobStatus.FAILED);
            job.setErrorMessage(e.getMessage());
            jobRepository.save(job);
            System.out.println("Job failed: " + message.getJobId());
        }
    }

    private void processJob(JobMessageDTO message) throws InterruptedException {
        System.out.println("Processing job type: " + message.getJobType());
        System.out.println("Payload: " + message.getPayload());
        Thread.sleep(2000); // 2 second ka fake processing time
    }
}