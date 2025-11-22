package com.workhub.server.service.impl;

import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.workhub.server.constant.JobStatus;
import com.workhub.server.dto.request.JobRequest;
import com.workhub.server.dto.response.JobResponse;
import com.workhub.server.dto.response.PaginationResponse;
import com.workhub.server.entity.Company;
import com.workhub.server.entity.Job;
import com.workhub.server.entity.User;
import com.workhub.server.exception.custom.CompanyNotFoundException;
import com.workhub.server.exception.custom.JobNotFoundException;
import com.workhub.server.exception.custom.UserNotFoundException;
import com.workhub.server.mapper.JobMapper;
import com.workhub.server.repository.CompanyRepository;
import com.workhub.server.repository.JobRepository;
import com.workhub.server.repository.UserRepository;
import com.workhub.server.service.JobService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@SuppressWarnings("null")
public class JobServiceImpl implements JobService {
        private final JobRepository jobRepository;
        private final CompanyRepository companyRepository;
        private final UserRepository userRepository;
        private final JobMapper jobMapper;

        @Override
        @Transactional
        public JobResponse createJob(JobRequest request) {
                // Verify company exists
                Company company = companyRepository.findById(request.getCompanyId())
                                .orElseThrow(() -> new CompanyNotFoundException(request.getCompanyId()));

                // Verify owner exists
                User owner = userRepository.findById(request.getOwnerId())
                                .orElseThrow(() -> new UserNotFoundException(request.getOwnerId()));

                Job job = jobMapper.toEntity(request);
                job.setCompany(company);
                job.setOwner(owner);

                Job savedJob = jobRepository.save(job);
                return jobMapper.toResponse(savedJob);
        }

        @Override
        @Transactional
        public JobResponse updateJob(UUID id, JobRequest request) {
                Job job = jobRepository.findById(id)
                                .orElseThrow(() -> new JobNotFoundException(id));

                // If changing company, verify new company exists
                if (!job.getCompany().getId().equals(request.getCompanyId())) {
                        Company newCompany = companyRepository.findById(request.getCompanyId())
                                        .orElseThrow(() -> new CompanyNotFoundException(request.getCompanyId()));
                        job.setCompany(newCompany);
                }

                // If changing owner, verify new owner exists
                if (!job.getOwner().getId().equals(request.getOwnerId())) {
                        User newOwner = userRepository.findById(request.getOwnerId())
                                        .orElseThrow(() -> new UserNotFoundException(request.getOwnerId()));
                        job.setOwner(newOwner);
                }

                jobMapper.updateEntityFromRequest(request, job);
                Job updatedJob = jobRepository.save(job);
                return jobMapper.toResponse(updatedJob);
        }

        @Override
        public JobResponse getJobById(UUID id) {
                Job job = jobRepository.findById(id)
                                .orElseThrow(() -> new JobNotFoundException(id));
                return jobMapper.toResponse(job);
        }

        @Override
        public PaginationResponse<JobResponse> getAllJobs(int page, int size) {
                Page<Job> jobs = jobRepository.findAll(PageRequest.of(page, size));

                return new PaginationResponse<>(
                                jobs.stream()
                                                .map(jobMapper::toResponse)
                                                .collect(Collectors.toList()),
                                page,
                                size,
                                jobs.getTotalElements(),
                                jobs.getTotalPages());
        }

        @Override
        public PaginationResponse<JobResponse> getJobsByCompany(UUID companyId, int page, int size) {
                // Verify company exists
                if (!companyRepository.existsById(companyId)) {
                        throw new CompanyNotFoundException(companyId);
                }

                Page<Job> jobs = jobRepository.findByCompanyId(companyId, PageRequest.of(page, size));

                return new PaginationResponse<>(
                                jobs.stream()
                                                .map(jobMapper::toResponse)
                                                .collect(Collectors.toList()),
                                page,
                                size,
                                jobs.getTotalElements(),
                                jobs.getTotalPages());
        }

        @Override
        public PaginationResponse<JobResponse> getJobsByOwner(UUID ownerId, int page, int size) {
                // Verify owner exists
                if (!userRepository.existsById(ownerId)) {
                        throw new UserNotFoundException(ownerId);
                }

                Page<Job> jobs = jobRepository.findByOwnerId(ownerId, PageRequest.of(page, size));

                return new PaginationResponse<>(
                                jobs.stream()
                                                .map(jobMapper::toResponse)
                                                .collect(Collectors.toList()),
                                page,
                                size,
                                jobs.getTotalElements(),
                                jobs.getTotalPages());
        }

        @Override
        public PaginationResponse<JobResponse> getJobsByStatus(JobStatus status, int page, int size) {
                Page<Job> jobs = jobRepository.findByStatus(status, PageRequest.of(page, size));

                return new PaginationResponse<>(
                                jobs.stream()
                                                .map(jobMapper::toResponse)
                                                .collect(Collectors.toList()),
                                page,
                                size,
                                jobs.getTotalElements(),
                                jobs.getTotalPages());
        }

        @Override
        public PaginationResponse<JobResponse> getJobsByCompanyAndStatus(UUID companyId, JobStatus status, int page,
                        int size) {
                // Verify company exists
                if (!companyRepository.existsById(companyId)) {
                        throw new CompanyNotFoundException(companyId);
                }

                Page<Job> jobs = jobRepository.findByCompanyIdAndStatus(companyId, status, PageRequest.of(page, size));

                return new PaginationResponse<>(
                                jobs.stream()
                                                .map(jobMapper::toResponse)
                                                .collect(Collectors.toList()),
                                page,
                                size,
                                jobs.getTotalElements(),
                                jobs.getTotalPages());
        }

        @Override
        @Transactional
        public void deleteJob(UUID id) {
                jobRepository.findById(id)
                                .orElseThrow(() -> new JobNotFoundException(id));

                jobRepository.deleteById(id);
        }
}
