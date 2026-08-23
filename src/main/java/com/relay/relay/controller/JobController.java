package com.relay.relay.controller;

import com.relay.relay.dto.JobRequestDTO;
import com.relay.relay.dto.JobResponseDTO;
import com.relay.relay.service.JobService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/jobs")
public class JobController {

    private final JobService jobService;

    public JobController(JobService jobService) {
        this.jobService = jobService;
    }

    @PostMapping
    public ResponseEntity<JobResponseDTO> createJob(@Valid @RequestBody JobRequestDTO request) {
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