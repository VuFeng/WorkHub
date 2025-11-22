package com.workhub.server.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonInclude;

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
public class TaskCommentResponse {
    private UUID id;
    private UUID taskId;
    private String taskTitle;
    private UUID userId;
    private String userName;
    private String message;
    private LocalDateTime createdAt;
}

