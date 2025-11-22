package com.workhub.server.dto.request;

import com.workhub.server.constant.JobStatus;

import jakarta.validation.constraints.NotNull;
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
public class JobStatusUpdateRequest {
    @NotNull(message = "Status is required")
    private JobStatus status;
}

