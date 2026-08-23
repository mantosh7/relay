package com.relay.relay.dto;

import com.relay.relay.enums.JobStatus;
import com.relay.relay.enums.Priority;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class JobResponseDTO {
    private UUID jobId;
    private String jobType;
    private JobStatus status;
    private Priority priority;
    private int retryCount;
    private LocalDateTime createdAt;
}
