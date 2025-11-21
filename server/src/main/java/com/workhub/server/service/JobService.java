package com.workhub.server.service;

import java.util.UUID;

import com.workhub.server.constant.JobStatus;
import com.workhub.server.dto.request.JobRequest;
import com.workhub.server.dto.response.JobResponse;
import com.workhub.server.dto.response.PaginationResponse;

public interface JobService {
    JobResponse createJob(JobRequest request);

    JobResponse updateJob(UUID id, JobRequest request);

    JobResponse getJobById(UUID id);

    PaginationResponse<JobResponse> getAllJobs(int page, int size);
    
    PaginationResponse<JobResponse> getJobsByCompany(UUID companyId, int page, int size);
    
    PaginationResponse<JobResponse> getJobsByOwner(UUID ownerId, int page, int size);
    
    PaginationResponse<JobResponse> getJobsByStatus(JobStatus status, int page, int size);
    
    PaginationResponse<JobResponse> getJobsByCompanyAndStatus(UUID companyId, JobStatus status, int page, int size);

    void deleteJob(UUID id);
}


