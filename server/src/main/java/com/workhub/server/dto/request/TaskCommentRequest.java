package com.workhub.server.dto.request;

import java.util.UUID;

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
public class TaskCommentRequest {
    @NotNull(message = "Task ID is required")
    private UUID taskId;

    @NotNull(message = "User ID is required")
    private UUID userId;

    @NotBlank(message = "Message is required")
    @Size(min = 1, max = 5000, message = "Message must be between 1 and 5000 characters")
    private String message;
}

