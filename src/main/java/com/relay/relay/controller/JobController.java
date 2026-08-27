package com.relay.relay.controller;

import com.relay.relay.dto.JobRequestDTO;
import com.relay.relay.dto.JobResponseDTO;
import com.relay.relay.service.JobService;
import com.relay.relay.service.RateLimiterService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/jobs")
public class JobController {

    private final JobService jobService;
    private final RateLimiterService rateLimiterService;

    public JobController(JobService jobService, RateLimiterService rateLimiterService) {
        this.jobService = jobService;
        this.rateLimiterService = rateLimiterService;
    }

    @PostMapping
    public ResponseEntity<JobResponseDTO> createJob(@Valid @RequestBody JobRequestDTO request) {
        if (!rateLimiterService.isAllowed(request.getClientId().toString())) {
            throw new RateLimitExceededException("Rate limit exceeded for client: " + request.getClientId());
        }

        return ResponseEntity.ok(jobService.createJob(request));
    }

    @GetMapping("/client/{clientId}")
    public ResponseEntity<List<JobResponseDTO>> getJobsByClient(@PathVariable UUID clientId) {
        return ResponseEntity.ok(jobService.getJobsByClientId(clientId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<JobResponseDTO> getJob(@PathVariable UUID id) {
        return ResponseEntity.ok(jobService.getJobById(id));
    }

    @GetMapping
    public ResponseEntity<List<JobResponseDTO>> getAllJobs() {
        return ResponseEntity.ok(jobService.getAllJobs());
    }
}