package com.workhub.server.service;

import java.util.UUID;

import com.workhub.server.constant.TaskStatus;
import com.workhub.server.dto.request.TaskRequest;
import com.workhub.server.dto.request.TaskStatusUpdateRequest;
import com.workhub.server.dto.response.TaskResponse;
import com.workhub.server.dto.response.PaginationResponse;

public interface TaskService {
    TaskResponse createTask(TaskRequest request);

    TaskResponse updateTask(UUID id, TaskRequest request);

    TaskResponse updateTaskStatus(UUID id, TaskStatusUpdateRequest request);

    TaskResponse getTaskById(UUID id);

    PaginationResponse<TaskResponse> getAllTasks(int page, int size);

    PaginationResponse<TaskResponse> getTasksByCompany(UUID companyId, int page, int size);

    PaginationResponse<TaskResponse> getTasksByJob(UUID jobId, int page, int size);

    PaginationResponse<TaskResponse> getTasksByAssignee(UUID assigneeId, int page, int size);

    PaginationResponse<TaskResponse> getTasksByStatus(TaskStatus status, int page, int size);

    PaginationResponse<TaskResponse> getTasksByJobAndStatus(UUID jobId, TaskStatus status, int page, int size);

    PaginationResponse<TaskResponse> getTasksByAssigneeAndStatus(UUID assigneeId, TaskStatus status, int page,
            int size);

    void deleteTask(UUID id);
}
