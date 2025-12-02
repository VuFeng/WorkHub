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

import com.workhub.server.constant.UserRole;
import com.workhub.server.dto.request.AddUserToCompanyRequest;
import com.workhub.server.dto.request.CompanyRequest;
import com.workhub.server.dto.response.ApiResponse;
import com.workhub.server.dto.response.CompanyResponse;
import com.workhub.server.dto.response.PaginationResponse;
import com.workhub.server.security.annotation.RequireAnyRole;
import com.workhub.server.security.annotation.RequireRole;
import com.workhub.server.service.CompanyService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/companies")
@RequiredArgsConstructor
@Validated
public class CompanyController {
    private final CompanyService companyService;

    @PostMapping
    @RequireRole(UserRole.ADMIN)
    public ResponseEntity<ApiResponse<CompanyResponse>> createCompany(
            @Valid @RequestBody CompanyRequest request) {
        CompanyResponse company = companyService.createCompany(request);
        ApiResponse<CompanyResponse> response = ApiResponse.success("Company created successfully", company);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    @RequireAnyRole({UserRole.ADMIN, UserRole.MANAGER, UserRole.STAFF})
    public ResponseEntity<ApiResponse<CompanyResponse>> getCompanyById(@PathVariable UUID id) {
        CompanyResponse company = companyService.getCompanyById(id);
        ApiResponse<CompanyResponse> response = ApiResponse.success(company);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    @RequireRole(UserRole.ADMIN)
    public ResponseEntity<ApiResponse<PaginationResponse<CompanyResponse>>> getAllCompanies(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        PaginationResponse<CompanyResponse> companies = companyService.getAllCompanies(page, size);
        ApiResponse<PaginationResponse<CompanyResponse>> response = ApiResponse.success(companies);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    @RequireRole(UserRole.ADMIN)
    public ResponseEntity<ApiResponse<CompanyResponse>> updateCompany(
            @PathVariable UUID id,
            @Valid @RequestBody CompanyRequest request) {
        CompanyResponse company = companyService.updateCompany(id, request);
        ApiResponse<CompanyResponse> response = ApiResponse.success("Company updated successfully", company);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @RequireRole(UserRole.ADMIN)
    public ResponseEntity<ApiResponse<Void>> deleteCompany(@PathVariable UUID id) {
        companyService.deleteCompany(id);
        ApiResponse<Void> response = ApiResponse.successWithoutData("Company deleted successfully");
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}/users")
    @RequireRole(UserRole.ADMIN)
    public ResponseEntity<ApiResponse<Void>> addUserToCompany(
            @PathVariable UUID id,
            @Valid @RequestBody AddUserToCompanyRequest request) {
        companyService.addUserToCompany(id, request);
        ApiResponse<Void> response = ApiResponse.successWithoutData("User added to company successfully");
        return ResponseEntity.ok(response);
    }
}
