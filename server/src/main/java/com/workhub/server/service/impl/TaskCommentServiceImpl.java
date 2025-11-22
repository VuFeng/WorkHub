package com.workhub.server.service.impl;

import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.workhub.server.dto.request.TaskCommentRequest;
import com.workhub.server.dto.response.PaginationResponse;
import com.workhub.server.dto.response.TaskCommentResponse;
import com.workhub.server.entity.Task;
import com.workhub.server.entity.TaskComment;
import com.workhub.server.entity.User;
import com.workhub.server.exception.custom.TaskCommentNotFoundException;
import com.workhub.server.exception.custom.TaskNotFoundException;
import com.workhub.server.exception.custom.UserNotFoundException;
import com.workhub.server.mapper.TaskCommentMapper;
import com.workhub.server.repository.TaskCommentRepository;
import com.workhub.server.repository.TaskRepository;
import com.workhub.server.repository.UserRepository;
import com.workhub.server.service.TaskCommentService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@SuppressWarnings("null")
public class TaskCommentServiceImpl implements TaskCommentService {
    private final TaskCommentRepository taskCommentRepository;
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final TaskCommentMapper taskCommentMapper;

    @Override
    @Transactional
    public TaskCommentResponse createComment(TaskCommentRequest request) {
        // Verify task exists
        Task task = taskRepository.findById(request.getTaskId())
                .orElseThrow(() -> new TaskNotFoundException(request.getTaskId()));

        // Verify user exists
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new UserNotFoundException(request.getUserId()));

        TaskComment comment = taskCommentMapper.toEntity(request);
        comment.setTask(task);
        comment.setUser(user);

        TaskComment savedComment = taskCommentRepository.save(comment);
        return taskCommentMapper.toResponse(savedComment);
    }

    @Override
    public TaskCommentResponse getCommentById(UUID id) {
        TaskComment comment = taskCommentRepository.findById(id)
                .orElseThrow(() -> new TaskCommentNotFoundException(id));
        return taskCommentMapper.toResponse(comment);
    }

    @Override
    public PaginationResponse<TaskCommentResponse> getAllComments(int page, int size) {
        Page<TaskComment> comments = taskCommentRepository.findAll(PageRequest.of(page, size));

        return new PaginationResponse<>(
                comments.stream()
                        .map(taskCommentMapper::toResponse)
                        .collect(Collectors.toList()),
                page,
                size,
                comments.getTotalElements(),
                comments.getTotalPages());
    }

    @Override
    public PaginationResponse<TaskCommentResponse> getCommentsByTask(UUID taskId, int page, int size) {
        // Verify task exists
        if (!taskRepository.existsById(taskId)) {
            throw new TaskNotFoundException(taskId);
        }

        Page<TaskComment> comments = taskCommentRepository.findByTaskId(taskId, PageRequest.of(page, size));

        return new PaginationResponse<>(
                comments.stream()
                        .map(taskCommentMapper::toResponse)
                        .collect(Collectors.toList()),
                page,
                size,
                comments.getTotalElements(),
                comments.getTotalPages());
    }

    @Override
    public PaginationResponse<TaskCommentResponse> getCommentsByUser(UUID userId, int page, int size) {
        // Verify user exists
        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException(userId);
        }

        Page<TaskComment> comments = taskCommentRepository.findByUserId(userId, PageRequest.of(page, size));

        return new PaginationResponse<>(
                comments.stream()
                        .map(taskCommentMapper::toResponse)
                        .collect(Collectors.toList()),
                page,
                size,
                comments.getTotalElements(),
                comments.getTotalPages());
    }

    @Override
    @Transactional
    public void deleteComment(UUID id) {
        taskCommentRepository.findById(id)
                .orElseThrow(() -> new TaskCommentNotFoundException(id));

        taskCommentRepository.deleteById(id);
    }
}

