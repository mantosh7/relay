package com.relay.relay.dto;

import com.relay.relay.enums.Priority;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class JobMessageDTO implements Serializable {
    private UUID jobId;
    private String jobType;
    private String payload;
    private Priority priority;
}