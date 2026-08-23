package com.relay.relay.service;

import com.relay.relay.dto.JobRequestDTO;
import com.relay.relay.dto.JobResponseDTO;

import java.util.List;
import java.util.UUID;

public interface JobService {

    JobResponseDTO createJob(JobRequestDTO request);
    JobResponseDTO getJobById(UUID id);
    List<JobResponseDTO> getAllJobs();
    List<JobResponseDTO> getJobsByClientId(UUID clientId);

}
