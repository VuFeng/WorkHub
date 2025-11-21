package com.workhub.server.dto.request;

import java.time.LocalDateTime;
import java.util.UUID;

import com.workhub.server.constant.JobPriority;
import com.workhub.server.constant.JobStatus;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
public class JobRequest {
    @NotNull(message = "Company ID is required")
    private UUID companyId;

    @NotNull(message = "Owner ID is required")
    private UUID ownerId;

    @NotBlank(message = "Title is required")
    @Size(min = 3, max = 255, message = "Title must be between 3 and 255 characters")
    private String title;

    @Size(max = 5000, message = "Description must not exceed 5000 characters")
    private String description;

    @NotNull(message = "Status is required")
    private JobStatus status;

    @NotNull(message = "Priority is required")
    private JobPriority priority;

    private LocalDateTime deadline;
}


