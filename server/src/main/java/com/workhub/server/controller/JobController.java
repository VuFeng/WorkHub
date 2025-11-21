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

import com.workhub.server.constant.JobStatus;
import com.workhub.server.dto.request.JobRequest;
import com.workhub.server.dto.response.ApiResponse;
import com.workhub.server.dto.response.JobResponse;
import com.workhub.server.dto.response.PaginationResponse;
import com.workhub.server.service.JobService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/jobs")
@RequiredArgsConstructor
@Validated
public class JobController {
    private final JobService jobService;

    @PostMapping
    public ResponseEntity<ApiResponse<JobResponse>> createJob(
            @Valid @RequestBody JobRequest request) {
        JobResponse job = jobService.createJob(request);
        ApiResponse<JobResponse> response = ApiResponse.success("Job created successfully", job);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<JobResponse>> getJobById(@PathVariable UUID id) {
        JobResponse job = jobService.getJobById(id);
        ApiResponse<JobResponse> response = ApiResponse.success(job);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<PaginationResponse<JobResponse>>> getAllJobs(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        PaginationResponse<JobResponse> jobs = jobService.getAllJobs(page, size);
        ApiResponse<PaginationResponse<JobResponse>> response = ApiResponse.success(jobs);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/company/{companyId}")
    public ResponseEntity<ApiResponse<PaginationResponse<JobResponse>>> getJobsByCompany(
            @PathVariable UUID companyId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        PaginationResponse<JobResponse> jobs = jobService.getJobsByCompany(companyId, page, size);
        ApiResponse<PaginationResponse<JobResponse>> response = ApiResponse.success(jobs);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/owner/{ownerId}")
    public ResponseEntity<ApiResponse<PaginationResponse<JobResponse>>> getJobsByOwner(
            @PathVariable UUID ownerId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        PaginationResponse<JobResponse> jobs = jobService.getJobsByOwner(ownerId, page, size);
        ApiResponse<PaginationResponse<JobResponse>> response = ApiResponse.success(jobs);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<ApiResponse<PaginationResponse<JobResponse>>> getJobsByStatus(
            @PathVariable JobStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        PaginationResponse<JobResponse> jobs = jobService.getJobsByStatus(status, page, size);
        ApiResponse<PaginationResponse<JobResponse>> response = ApiResponse.success(jobs);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/company/{companyId}/status/{status}")
    public ResponseEntity<ApiResponse<PaginationResponse<JobResponse>>> getJobsByCompanyAndStatus(
            @PathVariable UUID companyId,
            @PathVariable JobStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        PaginationResponse<JobResponse> jobs = jobService.getJobsByCompanyAndStatus(companyId, status, page, size);
        ApiResponse<PaginationResponse<JobResponse>> response = ApiResponse.success(jobs);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<JobResponse>> updateJob(
            @PathVariable UUID id,
            @Valid @RequestBody JobRequest request) {
        JobResponse job = jobService.updateJob(id, request);
        ApiResponse<JobResponse> response = ApiResponse.success("Job updated successfully", job);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteJob(@PathVariable UUID id) {
        jobService.deleteJob(id);
        ApiResponse<Void> response = ApiResponse.successWithoutData("Job deleted successfully");
        return ResponseEntity.ok(response);
    }
}


