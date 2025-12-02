package com.workhub.server.service;

import java.util.UUID;

import com.workhub.server.dto.request.AddUserToCompanyRequest;
import com.workhub.server.dto.request.CompanyRequest;
import com.workhub.server.dto.response.CompanyResponse;
import com.workhub.server.dto.response.PaginationResponse;

public interface CompanyService {
    CompanyResponse createCompany(CompanyRequest request);

    CompanyResponse updateCompany(UUID id, CompanyRequest request);

    CompanyResponse getCompanyById(UUID id);

    PaginationResponse<CompanyResponse> getAllCompanies(int page, int size);

    void deleteCompany(UUID id);

    void addUserToCompany(UUID companyId, AddUserToCompanyRequest request);
}

