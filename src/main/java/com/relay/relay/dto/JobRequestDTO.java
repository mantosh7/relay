package com.relay.relay.dto;

import com.relay.relay.enums.Priority;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class JobRequestDTO {

    @NotBlank(message = "Job type is required")
    private String jobType;

    @NotNull(message = "Client ID is required")
    private UUID clientId;

    @NotNull(message = "Priority is required")
    private Priority priority;

    @NotBlank(message = "Payload is required")
    private String payload;
}

