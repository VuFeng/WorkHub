package com.workhub.server.service.impl;

import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.workhub.server.constant.TaskStatus;
import com.workhub.server.dto.request.TaskRequest;
import com.workhub.server.dto.request.TaskStatusUpdateRequest;
import com.workhub.server.dto.response.PaginationResponse;
import com.workhub.server.dto.response.TaskResponse;
import com.workhub.server.entity.Company;
import com.workhub.server.entity.Job;
import com.workhub.server.entity.Task;
import com.workhub.server.entity.User;
import com.workhub.server.exception.custom.CompanyNotFoundException;
import com.workhub.server.exception.custom.JobNotFoundException;
import com.workhub.server.exception.custom.TaskNotFoundException;
import com.workhub.server.exception.custom.UserNotFoundException;
import com.workhub.server.mapper.TaskMapper;
import com.workhub.server.repository.CompanyRepository;
import com.workhub.server.repository.JobRepository;
import com.workhub.server.repository.TaskRepository;
import com.workhub.server.repository.UserRepository;
import com.workhub.server.service.TaskService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@SuppressWarnings("null")
public class TaskServiceImpl implements TaskService {
    private final TaskRepository taskRepository;
    private final CompanyRepository companyRepository;
    private final JobRepository jobRepository;
    private final UserRepository userRepository;
    private final TaskMapper taskMapper;

    @Override
    @Transactional
    public TaskResponse createTask(TaskRequest request) {
        // Verify company exists
        Company company = companyRepository.findById(request.getCompanyId())
                .orElseThrow(() -> new CompanyNotFoundException(request.getCompanyId()));

        // Verify job exists
        Job job = jobRepository.findById(request.getJobId())
                .orElseThrow(() -> new JobNotFoundException(request.getJobId()));

        // Verify assignee exists
        User assignee = userRepository.findById(request.getAssigneeId())
                .orElseThrow(() -> new UserNotFoundException(request.getAssigneeId()));

        Task task = taskMapper.toEntity(request);
        task.setCompany(company);
        task.setJob(job);
        task.setAssignee(assignee);

        Task savedTask = taskRepository.save(task);
        return taskMapper.toResponse(savedTask);
    }

    @Override
    @Transactional
    public TaskResponse updateTask(UUID id, TaskRequest request) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(id));

        // If changing company, verify new company exists
        if (!task.getCompany().getId().equals(request.getCompanyId())) {
            Company newCompany = companyRepository.findById(request.getCompanyId())
                    .orElseThrow(() -> new CompanyNotFoundException(request.getCompanyId()));
            task.setCompany(newCompany);
        }

        // If changing job, verify new job exists
        if (!task.getJob().getId().equals(request.getJobId())) {
            Job newJob = jobRepository.findById(request.getJobId())
                    .orElseThrow(() -> new JobNotFoundException(request.getJobId()));
            task.setJob(newJob);
        }

        // If changing assignee, verify new assignee exists
        if (!task.getAssignee().getId().equals(request.getAssigneeId())) {
            User newAssignee = userRepository.findById(request.getAssigneeId())
                    .orElseThrow(() -> new UserNotFoundException(request.getAssigneeId()));
            task.setAssignee(newAssignee);
        }

        taskMapper.updateEntityFromRequest(request, task);
        Task updatedTask = taskRepository.save(task);
        return taskMapper.toResponse(updatedTask);
    }

    @Override
    @Transactional
    public TaskResponse updateTaskStatus(UUID id, TaskStatusUpdateRequest request) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(id));
        
        task.setStatus(request.getStatus());
        Task updatedTask = taskRepository.save(task);
        return taskMapper.toResponse(updatedTask);
    }

    @Override
    public TaskResponse getTaskById(UUID id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(id));
        return taskMapper.toResponse(task);
    }

    @Override
    public PaginationResponse<TaskResponse> getAllTasks(int page, int size) {
        Page<Task> tasks = taskRepository.findAll(PageRequest.of(page, size));

        return new PaginationResponse<>(
                tasks.stream()
                        .map(taskMapper::toResponse)
                        .collect(Collectors.toList()),
                page,
                size,
                tasks.getTotalElements(),
                tasks.getTotalPages());
    }

    @Override
    public PaginationResponse<TaskResponse> getTasksByCompany(UUID companyId, int page, int size) {
        // Verify company exists
        if (!companyRepository.existsById(companyId)) {
            throw new CompanyNotFoundException(companyId);
        }

        Page<Task> tasks = taskRepository.findByCompanyId(companyId, PageRequest.of(page, size));

        return new PaginationResponse<>(
                tasks.stream()
                        .map(taskMapper::toResponse)
                        .collect(Collectors.toList()),
                page,
                size,
                tasks.getTotalElements(),
                tasks.getTotalPages());
    }

    @Override
    public PaginationResponse<TaskResponse> getTasksByJob(UUID jobId, int page, int size) {
        // Verify job exists
        if (!jobRepository.existsById(jobId)) {
            throw new JobNotFoundException(jobId);
        }

        Page<Task> tasks = taskRepository.findByJobId(jobId, PageRequest.of(page, size));

        return new PaginationResponse<>(
                tasks.stream()
                        .map(taskMapper::toResponse)
                        .collect(Collectors.toList()),
                page,
                size,
                tasks.getTotalElements(),
                tasks.getTotalPages());
    }

    @Override
    public PaginationResponse<TaskResponse> getTasksByAssignee(UUID assigneeId, int page, int size) {
        // Verify assignee exists
        if (!userRepository.existsById(assigneeId)) {
            throw new UserNotFoundException(assigneeId);
        }

        Page<Task> tasks = taskRepository.findByAssigneeId(assigneeId, PageRequest.of(page, size));

        return new PaginationResponse<>(
                tasks.stream()
                        .map(taskMapper::toResponse)
                        .collect(Collectors.toList()),
                page,
                size,
                tasks.getTotalElements(),
                tasks.getTotalPages());
    }

    @Override
    public PaginationResponse<TaskResponse> getTasksByStatus(TaskStatus status, int page, int size) {
        Page<Task> tasks = taskRepository.findByStatus(status, PageRequest.of(page, size));

        return new PaginationResponse<>(
                tasks.stream()
                        .map(taskMapper::toResponse)
                        .collect(Collectors.toList()),
                page,
                size,
                tasks.getTotalElements(),
                tasks.getTotalPages());
    }

    @Override
    public PaginationResponse<TaskResponse> getTasksByJobAndStatus(UUID jobId, TaskStatus status, int page, int size) {
        // Verify job exists
        if (!jobRepository.existsById(jobId)) {
            throw new JobNotFoundException(jobId);
        }

        Page<Task> tasks = taskRepository.findByJobIdAndStatus(jobId, status, PageRequest.of(page, size));

        return new PaginationResponse<>(
                tasks.stream()
                        .map(taskMapper::toResponse)
                        .collect(Collectors.toList()),
                page,
                size,
                tasks.getTotalElements(),
                tasks.getTotalPages());
    }

    @Override
    public PaginationResponse<TaskResponse> getTasksByAssigneeAndStatus(UUID assigneeId, TaskStatus status, int page, int size) {
        // Verify assignee exists
        if (!userRepository.existsById(assigneeId)) {
            throw new UserNotFoundException(assigneeId);
        }

        Page<Task> tasks = taskRepository.findByAssigneeIdAndStatus(assigneeId, status, PageRequest.of(page, size));

        return new PaginationResponse<>(
                tasks.stream()
                        .map(taskMapper::toResponse)
                        .collect(Collectors.toList()),
                page,
                size,
                tasks.getTotalElements(),
                tasks.getTotalPages());
    }

    @Override
    @Transactional
    public void deleteTask(UUID id) {
        taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(id));

        taskRepository.deleteById(id);
    }
}

