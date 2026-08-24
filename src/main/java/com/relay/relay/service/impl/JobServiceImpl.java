package com.relay.relay.service.impl;

import com.relay.relay.dto.JobMessageDTO;
import com.relay.relay.dto.JobRequestDTO;
import com.relay.relay.dto.JobResponseDTO;
import com.relay.relay.entity.Client;
import com.relay.relay.entity.Job;
import com.relay.relay.enums.JobStatus;
import com.relay.relay.exception.ClientNotFoundException;
import com.relay.relay.exception.JobNotFoundException;
import com.relay.relay.repository.ClientRepository;
import com.relay.relay.repository.JobRepository;
import com.relay.relay.service.JobPublisherService;
import com.relay.relay.service.JobService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class JobServiceImpl implements JobService {

    private final JobRepository jobRepository;
    private final ClientRepository clientRepository;
    private final JobPublisherService jobPublisherService;

    public JobServiceImpl(JobRepository jobRepository, ClientRepository clientRepository,
                          JobPublisherService jobPublisherService) {
        this.jobRepository = jobRepository;
        this.clientRepository = clientRepository;
        this.jobPublisherService = jobPublisherService;
    }

    @Override
    public JobResponseDTO createJob(JobRequestDTO request) {

        Client client = clientRepository.findById(request.getClientId())
                .orElseThrow(() -> new ClientNotFoundException(
                        "Client not found with id: " + request.getClientId()));

        Job job = new Job();
        job.setJobType(request.getJobType());
        job.setPriority(request.getPriority());
        job.setPayload(request.getPayload());
        job.setStatus(JobStatus.PENDING);
        job.setClient(client);

        Job savedJob = jobRepository.save(job);

        JobMessageDTO message = new JobMessageDTO(
                savedJob.getId(),
                savedJob.getJobType(),
                savedJob.getPayload(),
                savedJob.getPriority()
        );
        jobPublisherService.publishJob(message);

        return mapToResponseDTO(savedJob);
    }

    @Override
    public JobResponseDTO getJobById(UUID id) {
        Job job = jobRepository.findById(id)
                .orElseThrow(() -> new JobNotFoundException("Job not found with id: " + id));

        return mapToResponseDTO(job);
    }

    @Override
    public List<JobResponseDTO> getAllJobs() {
        return jobRepository.findAll()
                .stream()
                .map(this::mapToResponseDTO)
                .toList();
    }

    @Override
    public List<JobResponseDTO> getJobsByClientId(UUID clientId) {
        if (!clientRepository.existsById(clientId)) {
            throw new ClientNotFoundException("Client not found with id: " + clientId);
        }

        return jobRepository.findByClient_Id(clientId)
                .stream()
                .map(this::mapToResponseDTO)
                .toList();
    }

    private JobResponseDTO mapToResponseDTO(Job job) {
        return new JobResponseDTO(
                job.getId(),
                job.getJobType(),
                job.getStatus(),
                job.getPriority(),
                job.getRetryCount(),
                job.getCreatedAt()
        );
    }
}