package com.workhub.server.dto.request;

import java.time.LocalDateTime;
import java.util.UUID;

import com.workhub.server.constant.TaskStatus;

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
public class TaskRequest {
    @NotNull(message = "Company ID is required")
    private UUID companyId;

    @NotNull(message = "Job ID is required")
    private UUID jobId;

    @NotNull(message = "Assignee ID is required")
    private UUID assigneeId;

    @NotBlank(message = "Title is required")
    @Size(min = 3, max = 255, message = "Title must be between 3 and 255 characters")
    private String title;

    @Size(max = 5000, message = "Description must not exceed 5000 characters")
    private String description;

    @NotNull(message = "Status is required")
    private TaskStatus status;

    private LocalDateTime startDate;

    private LocalDateTime dueDate;
}

