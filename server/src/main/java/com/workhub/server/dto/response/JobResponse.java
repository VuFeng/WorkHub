package com.workhub.server.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.workhub.server.constant.JobPriority;
import com.workhub.server.constant.JobStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class JobResponse {
    private UUID id;
    private UUID companyId;
    private String companyName;
    private UUID ownerId;
    private String ownerName;
    private String title;
    private String description;
    private JobStatus status;
    private JobPriority priority;
    private LocalDateTime deadline;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}


