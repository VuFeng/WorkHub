package com.workhub.server.service.impl;

import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.workhub.server.dto.request.CompanyRequest;
import com.workhub.server.dto.response.CompanyResponse;
import com.workhub.server.dto.response.PaginationResponse;
import com.workhub.server.entity.Company;
import com.workhub.server.exception.custom.CompanyNotFoundException;
import com.workhub.server.exception.custom.DuplicateCompanyNameException;
import com.workhub.server.mapper.CompanyMapper;
import com.workhub.server.repository.CompanyRepository;
import com.workhub.server.service.CompanyService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@SuppressWarnings("null")
public class CompanyServiceImpl implements CompanyService {
    private final CompanyRepository companyRepository;
    private final CompanyMapper companyMapper;

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
        Company company = companyRepository.findById(id)
                .orElseThrow(() -> new CompanyNotFoundException(id));
        return companyMapper.toResponse(company);
    }

    @Override
    public PaginationResponse<CompanyResponse> getAllCompanies(int page, int size) {
        Page<Company> companies = companyRepository.findAll(PageRequest.of(page, size));

        return new PaginationResponse<>(
                companies.stream()
                        .map(companyMapper::toResponse)
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

        companyRepository.delete(company);
    }
}