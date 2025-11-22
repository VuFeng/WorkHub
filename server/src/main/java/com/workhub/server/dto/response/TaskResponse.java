package com.workhub.server.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.workhub.server.constant.TaskStatus;

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
public class TaskResponse {
    private UUID id;
    private UUID companyId;
    private String companyName;
    private UUID jobId;
    private String jobTitle;
    private UUID assigneeId;
    private String assigneeName;
    private String title;
    private String description;
    private TaskStatus status;
    private LocalDateTime startDate;
    private LocalDateTime dueDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

