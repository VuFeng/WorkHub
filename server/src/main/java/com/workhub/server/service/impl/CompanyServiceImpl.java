package com.workhub.server.service.impl;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import com.workhub.server.entity.User;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.workhub.server.dto.request.AddUserToCompanyRequest;
import com.workhub.server.dto.request.CompanyRequest;
import com.workhub.server.dto.response.CompanyResponse;
import com.workhub.server.dto.response.PaginationResponse;
import com.workhub.server.entity.Company;
import com.workhub.server.entity.CompanyUser;
import com.workhub.server.exception.custom.CompanyNotFoundException;
import com.workhub.server.exception.custom.DuplicateCompanyNameException;
import com.workhub.server.exception.custom.UserNotFoundException;
import com.workhub.server.mapper.CompanyMapper;
import com.workhub.server.mapper.UserMapper;
import com.workhub.server.repository.CompanyRepository;
import com.workhub.server.repository.CompanyUserRepository;
import com.workhub.server.repository.UserRepository;
import com.workhub.server.service.CompanyService;
import com.workhub.server.service.FileStorageService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;

import java.net.URI;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@SuppressWarnings("null")
public class CompanyServiceImpl implements CompanyService {
    private final CompanyRepository companyRepository;
    private final CompanyMapper companyMapper;
    private final CompanyUserRepository companyUserRepository;
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final FileStorageService fileStorageService;
    
    @Value("${aws.s3.bucket}")
    private String bucketName;

    @Override
    @Transactional
    public CompanyResponse createCompany(CompanyRequest request) {
        // Kiểm tra tên công ty đã tồn tại chưa
        if (companyRepository.existsByName(request.getName())) {
            throw new DuplicateCompanyNameException(request.getName());
        }

        Company company = companyMapper.toEntity(request);
        Company savedCompany = companyRepository.save(company);
        return companyMapper.toResponse(savedCompany);
    }

    @Override
    @Transactional
    public CompanyResponse updateCompany(UUID id, CompanyRequest request) {
        Company company = companyRepository.findById(id)
                .orElseThrow(() -> new CompanyNotFoundException(id));

        // Kiểm tra tên mới có trùng với công ty khác không
        if (!company.getName().equals(request.getName()) &&
                companyRepository.existsByName(request.getName())) {
            throw new DuplicateCompanyNameException(request.getName());
        }

        companyMapper.updateEntityFromRequest(request, company);
        Company updatedCompany = companyRepository.save(company);
        return companyMapper.toResponse(updatedCompany);
    }

    @Override
    public CompanyResponse getCompanyById(UUID id) {
        log.info("Fetching company with id: {}", companyRepository.findById(id));
        Company company = companyRepository.findById(id)
                .orElseThrow(() -> new CompanyNotFoundException(id));
        CompanyResponse response = companyMapper.toResponse(company);
        // Load and set users through company_users junction table
        List<User> users = companyUserRepository.findUsersByCompanyId(id);
        response.setUsers(users.stream()
                .map(userMapper::toResponse)
                .collect(Collectors.toList()));
        return response;
    }

    @Override
    public PaginationResponse<CompanyResponse> getAllCompanies(int page, int size) {
        Page<Company> companies = companyRepository.findAll(PageRequest.of(page, size));

        return new PaginationResponse<>(
                companies.stream()
                        .map(company -> {
                            CompanyResponse response = companyMapper.toResponse(company);
                            // Load and set users through company_users junction table
                            List<User> users = companyUserRepository.findUsersByCompanyId(company.getId());
                            response.setUsers(users.stream()
                                    .map(userMapper::toResponse)
                                    .collect(Collectors.toList()));
                            return response;
                        })
                        .collect(Collectors.toList()),
                page,
                size,
                companies.getTotalElements(),
                companies.getTotalPages());
    }

    @Override
    @Transactional
    public void deleteCompany(UUID id) {
        Company company = companyRepository.findById(id)
                .orElseThrow(() -> new CompanyNotFoundException(id));

        // Delete all company_user relationships (this won't delete the users themselves)
        companyUserRepository.deleteByCompanyId(id);
        log.info("Deleted all company_user relationships for company: {}", id);

        // Delete logo from S3 if exists
        if (company.getLogoUrl() != null && !company.getLogoUrl().isEmpty()) {
            try {
                String key = extractKeyFromUrl(company.getLogoUrl());
                if (key != null) {
                    fileStorageService.delete(key);
                    log.info("Deleted company logo from S3: {}", company.getLogoUrl());
                } else {
                    log.warn("Could not extract key from logo URL: {}", company.getLogoUrl());
                }
            } catch (Exception e) {
                log.warn("Failed to delete company logo from S3: {}", company.getLogoUrl(), e);
                // Continue with company deletion even if logo deletion fails
            }
        }

        companyRepository.delete(company);
    }

    @Override
    @Transactional
    public void addUserToCompany(UUID companyId, AddUserToCompanyRequest request) {
        // Kiểm tra company có tồn tại không
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new CompanyNotFoundException(companyId));

        // Kiểm tra user có tồn tại không
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new UserNotFoundException(request.getUserId()));

        // Kiểm tra user đã thuộc company này chưa
        if (companyUserRepository.existsByCompanyIdAndUserId(companyId, request.getUserId())) {
            throw new IllegalArgumentException("User is already a member of this company");
        }

        // Tạo relationship
        CompanyUser companyUser = new CompanyUser();
        companyUser.setCompany(company);
        companyUser.setUser(user);
        companyUserRepository.save(companyUser);

        log.info("Added user {} to company {}", request.getUserId(), companyId);
    }

    private String extractKeyFromUrl(String url) {
        try {
            URI uri = URI.create(url);
            String path = uri.getPath();
            // Remove leading slash
            if (path != null && path.startsWith("/")) {
                path = path.substring(1);
            }
            return path;
        } catch (Exception e) {
            log.error("Invalid URL for key extraction: {}", url, e);
            return null;
        }
    }
}