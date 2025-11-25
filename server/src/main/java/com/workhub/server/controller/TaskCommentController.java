package com.workhub.server.controller;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.workhub.server.constant.UserRole;
import com.workhub.server.dto.request.TaskCommentRequest;
import com.workhub.server.dto.response.ApiResponse;
import com.workhub.server.dto.response.PaginationResponse;
import com.workhub.server.dto.response.TaskCommentResponse;
import com.workhub.server.security.annotation.RequireAnyRole;
import com.workhub.server.service.TaskCommentService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/task-comments")
@RequiredArgsConstructor
@Validated
public class TaskCommentController {
    private final TaskCommentService taskCommentService;

    @PostMapping
    @RequireAnyRole({UserRole.ADMIN, UserRole.MANAGER, UserRole.STAFF})
    public ResponseEntity<ApiResponse<TaskCommentResponse>> createComment(
            @Valid @RequestBody TaskCommentRequest request) {
        TaskCommentResponse comment = taskCommentService.createComment(request);
        ApiResponse<TaskCommentResponse> response = ApiResponse.success("Comment created successfully", comment);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    @RequireAnyRole({UserRole.ADMIN, UserRole.MANAGER, UserRole.STAFF})
    public ResponseEntity<ApiResponse<TaskCommentResponse>> getCommentById(@PathVariable UUID id) {
        TaskCommentResponse comment = taskCommentService.getCommentById(id);
        ApiResponse<TaskCommentResponse> response = ApiResponse.success(comment);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    @RequireAnyRole({UserRole.ADMIN, UserRole.MANAGER, UserRole.STAFF})
    public ResponseEntity<ApiResponse<PaginationResponse<TaskCommentResponse>>> getAllComments(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        PaginationResponse<TaskCommentResponse> comments = taskCommentService.getAllComments(page, size);
        ApiResponse<PaginationResponse<TaskCommentResponse>> response = ApiResponse.success(comments);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/task/{taskId}")
    @RequireAnyRole({UserRole.ADMIN, UserRole.MANAGER, UserRole.STAFF})
    public ResponseEntity<ApiResponse<PaginationResponse<TaskCommentResponse>>> getCommentsByTask(
            @PathVariable UUID taskId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        PaginationResponse<TaskCommentResponse> comments = taskCommentService.getCommentsByTask(taskId, page, size);
        ApiResponse<PaginationResponse<TaskCommentResponse>> response = ApiResponse.success(comments);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/user/{userId}")
    @RequireAnyRole({UserRole.ADMIN, UserRole.MANAGER, UserRole.STAFF})
    public ResponseEntity<ApiResponse<PaginationResponse<TaskCommentResponse>>> getCommentsByUser(
            @PathVariable UUID userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        PaginationResponse<TaskCommentResponse> comments = taskCommentService.getCommentsByUser(userId, page, size);
        ApiResponse<PaginationResponse<TaskCommentResponse>> response = ApiResponse.success(comments);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @RequireAnyRole({UserRole.ADMIN, UserRole.MANAGER})
    public ResponseEntity<ApiResponse<Void>> deleteComment(@PathVariable UUID id) {
        taskCommentService.deleteComment(id);
        ApiResponse<Void> response = ApiResponse.successWithoutData("Comment deleted successfully");
        return ResponseEntity.ok(response);
    }
}

