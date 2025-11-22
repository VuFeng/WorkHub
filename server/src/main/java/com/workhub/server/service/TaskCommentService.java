package com.workhub.server.service;

import java.util.UUID;

import com.workhub.server.dto.request.TaskCommentRequest;
import com.workhub.server.dto.response.TaskCommentResponse;
import com.workhub.server.dto.response.PaginationResponse;

public interface TaskCommentService {
    TaskCommentResponse createComment(TaskCommentRequest request);

    TaskCommentResponse getCommentById(UUID id);

    PaginationResponse<TaskCommentResponse> getAllComments(int page, int size);
    
    PaginationResponse<TaskCommentResponse> getCommentsByTask(UUID taskId, int page, int size);
    
    PaginationResponse<TaskCommentResponse> getCommentsByUser(UUID userId, int page, int size);

    void deleteComment(UUID id);
}

