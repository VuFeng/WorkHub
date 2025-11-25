package com.workhub.server.controller;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.workhub.server.constant.TaskStatus;
import com.workhub.server.constant.UserRole;
import com.workhub.server.dto.request.TaskRequest;
import com.workhub.server.dto.request.TaskStatusUpdateRequest;
import com.workhub.server.dto.response.ApiResponse;
import com.workhub.server.dto.response.PaginationResponse;
import com.workhub.server.dto.response.TaskResponse;
import com.workhub.server.security.annotation.RequireAnyRole;
import com.workhub.server.service.TaskService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.PatchMapping;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
@Validated
public class TaskController {
    private final TaskService taskService;

    @PostMapping
    @RequireAnyRole({UserRole.ADMIN, UserRole.MANAGER})
    public ResponseEntity<ApiResponse<TaskResponse>> createTask(
            @Valid @RequestBody TaskRequest request) {
        TaskResponse task = taskService.createTask(request);
        ApiResponse<TaskResponse> response = ApiResponse.success("Task created successfully", task);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    @RequireAnyRole({UserRole.ADMIN, UserRole.MANAGER, UserRole.STAFF})
    public ResponseEntity<ApiResponse<TaskResponse>> getTaskById(@PathVariable UUID id) {
        TaskResponse task = taskService.getTaskById(id);
        ApiResponse<TaskResponse> response = ApiResponse.success(task);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    @RequireAnyRole({UserRole.ADMIN, UserRole.MANAGER, UserRole.STAFF})
    public ResponseEntity<ApiResponse<PaginationResponse<TaskResponse>>> getAllTasks(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        PaginationResponse<TaskResponse> tasks = taskService.getAllTasks(page, size);
        ApiResponse<PaginationResponse<TaskResponse>> response = ApiResponse.success(tasks);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/company/{companyId}")
    @RequireAnyRole({UserRole.ADMIN, UserRole.MANAGER, UserRole.STAFF})
    public ResponseEntity<ApiResponse<PaginationResponse<TaskResponse>>> getTasksByCompany(
            @PathVariable UUID companyId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        PaginationResponse<TaskResponse> tasks = taskService.getTasksByCompany(companyId, page, size);
        ApiResponse<PaginationResponse<TaskResponse>> response = ApiResponse.success(tasks);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/job/{jobId}")
    @RequireAnyRole({UserRole.ADMIN, UserRole.MANAGER, UserRole.STAFF})
    public ResponseEntity<ApiResponse<PaginationResponse<TaskResponse>>> getTasksByJob(
            @PathVariable UUID jobId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        PaginationResponse<TaskResponse> tasks = taskService.getTasksByJob(jobId, page, size);
        ApiResponse<PaginationResponse<TaskResponse>> response = ApiResponse.success(tasks);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/assignee/{assigneeId}")
    @RequireAnyRole({UserRole.ADMIN, UserRole.MANAGER, UserRole.STAFF})
    public ResponseEntity<ApiResponse<PaginationResponse<TaskResponse>>> getTasksByAssignee(
            @PathVariable UUID assigneeId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        PaginationResponse<TaskResponse> tasks = taskService.getTasksByAssignee(assigneeId, page, size);
        ApiResponse<PaginationResponse<TaskResponse>> response = ApiResponse.success(tasks);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/status/{status}")
    @RequireAnyRole({UserRole.ADMIN, UserRole.MANAGER, UserRole.STAFF})
    public ResponseEntity<ApiResponse<PaginationResponse<TaskResponse>>> getTasksByStatus(
            @PathVariable TaskStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        PaginationResponse<TaskResponse> tasks = taskService.getTasksByStatus(status, page, size);
        ApiResponse<PaginationResponse<TaskResponse>> response = ApiResponse.success(tasks);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/job/{jobId}/status/{status}")
    @RequireAnyRole({UserRole.ADMIN, UserRole.MANAGER, UserRole.STAFF})
    public ResponseEntity<ApiResponse<PaginationResponse<TaskResponse>>> getTasksByJobAndStatus(
            @PathVariable UUID jobId,
            @PathVariable TaskStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        PaginationResponse<TaskResponse> tasks = taskService.getTasksByJobAndStatus(jobId, status, page, size);
        ApiResponse<PaginationResponse<TaskResponse>> response = ApiResponse.success(tasks);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/assignee/{assigneeId}/status/{status}")
    @RequireAnyRole({UserRole.ADMIN, UserRole.MANAGER, UserRole.STAFF})
    public ResponseEntity<ApiResponse<PaginationResponse<TaskResponse>>> getTasksByAssigneeAndStatus(
            @PathVariable UUID assigneeId,
            @PathVariable TaskStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        PaginationResponse<TaskResponse> tasks = taskService.getTasksByAssigneeAndStatus(assigneeId, status, page, size);
        ApiResponse<PaginationResponse<TaskResponse>> response = ApiResponse.success(tasks);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    @RequireAnyRole({UserRole.ADMIN, UserRole.MANAGER})
    public ResponseEntity<ApiResponse<TaskResponse>> updateTask(
            @PathVariable UUID id,
            @Valid @RequestBody TaskRequest request) {
        TaskResponse task = taskService.updateTask(id, request);
        ApiResponse<TaskResponse> response = ApiResponse.success("Task updated successfully", task);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/status")
    @RequireAnyRole({UserRole.ADMIN, UserRole.MANAGER})
    public ResponseEntity<ApiResponse<TaskResponse>> updateTaskStatus(
            @PathVariable UUID id,
            @Valid @RequestBody TaskStatusUpdateRequest request) {
        TaskResponse task = taskService.updateTaskStatus(id, request);
        ApiResponse<TaskResponse> response = ApiResponse.success("Task status updated successfully", task);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @RequireAnyRole({UserRole.ADMIN, UserRole.MANAGER})
    public ResponseEntity<ApiResponse<Void>> deleteTask(@PathVariable UUID id) {
        taskService.deleteTask(id);
        ApiResponse<Void> response = ApiResponse.successWithoutData("Task deleted successfully");
        return ResponseEntity.ok(response);
    }
}

