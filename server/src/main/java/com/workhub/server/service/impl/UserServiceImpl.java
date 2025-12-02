package com.workhub.server.service.impl;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.workhub.server.dto.request.UserRequest;
import com.workhub.server.dto.response.PaginationResponse;
import com.workhub.server.dto.response.UserResponse;
import com.workhub.server.entity.Company;
import com.workhub.server.entity.CompanyUser;
import com.workhub.server.entity.User;
import com.workhub.server.exception.custom.CompanyNotFoundException;
import com.workhub.server.exception.custom.DuplicateEmailException;
import com.workhub.server.exception.custom.UserNotFoundException;
import com.workhub.server.mapper.UserMapper;
import com.workhub.server.repository.CompanyRepository;
import com.workhub.server.repository.CompanyUserRepository;
import com.workhub.server.repository.UserRepository;
import com.workhub.server.service.UserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@SuppressWarnings("null")
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final CompanyRepository companyRepository;
    private final CompanyUserRepository companyUserRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public UserResponse createUser(UserRequest request) {
        // Kiểm tra email đã tồn tại chưa
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateEmailException(request.getEmail());
        }

        User user = userMapper.toEntity(request);
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));

        // Set default isActive if not provided
        if (user.getIsActive() == null) {
            user.setIsActive(true);
        }

        User savedUser = userRepository.save(user);

        // Nếu có truyền companyId thì mới tạo quan hệ company_user
        if (request.getCompanyId() != null) {
            Company company = companyRepository.findById(request.getCompanyId())
                    .orElseThrow(() -> new CompanyNotFoundException(request.getCompanyId()));

            CompanyUser companyUser = new CompanyUser();
            companyUser.setCompany(company);
            companyUser.setUser(savedUser);
            companyUserRepository.save(companyUser);
        }

        return userMapper.toResponse(savedUser);
    }

    @Override
    @Transactional
    public UserResponse updateUser(UUID id, UserRequest request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));

        // Kiểm tra email mới có trùng với user khác không
        if (!user.getEmail().equals(request.getEmail()) &&
                userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateEmailException(request.getEmail());
        }

        // Handle company change through company_users relationship (nếu có truyền companyId)
        if (request.getCompanyId() != null) {
            List<CompanyUser> existingCompanyUsers = companyUserRepository.findByUserId(id);
            boolean hasCompany = existingCompanyUsers.stream()
                    .anyMatch(cu -> cu.getCompany().getId().equals(request.getCompanyId()));

            if (!hasCompany) {
                // Check if new company exists
                Company newCompany = companyRepository.findById(request.getCompanyId())
                        .orElseThrow(() -> new CompanyNotFoundException(request.getCompanyId()));

                // Remove old company relationships (optional: could keep multiple companies)
                // For now, we'll replace with new company
                companyUserRepository.deleteAll(existingCompanyUsers);

                // Create new company_user relationship
                CompanyUser companyUser = new CompanyUser();
                companyUser.setCompany(newCompany);
                companyUser.setUser(user);
                companyUserRepository.save(companyUser);
            }
        }

        userMapper.updateEntityFromRequest(request, user);

        // Chỉ update password nếu có password mới
        if (request.getPassword() != null && !request.getPassword().isEmpty()) {
            user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        }

        User updatedUser = userRepository.save(user);
        return userMapper.toResponse(updatedUser);
    }

    @Override
    public UserResponse getUserById(UUID id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
        return userMapper.toResponse(user);
    }

    @Override
    public PaginationResponse<UserResponse> getAllUsers(int page, int size) {
        Page<User> users = userRepository.findAll(PageRequest.of(page, size));

        return new PaginationResponse<>(
                users.stream()
                        .map(userMapper::toResponse)
                        .collect(Collectors.toList()),
                page,
                size,
                users.getTotalElements(),
                users.getTotalPages());
    }

    @Override
    public PaginationResponse<UserResponse> getUsersByCompany(UUID companyId, int page, int size) {
        // Kiểm tra company có tồn tại không
        if (!companyRepository.existsById(companyId)) {
            throw new CompanyNotFoundException(companyId);
        }

        // Get users through company_users junction table
        List<User> allUsers = companyUserRepository.findUsersByCompanyId(companyId);
        
        // Manual pagination (can be optimized with native query)
        int start = page * size;
        int end = Math.min(start + size, allUsers.size());
        List<User> paginatedUsers = allUsers.subList(Math.min(start, allUsers.size()), end);

        return new PaginationResponse<>(
                paginatedUsers.stream()
                        .map(userMapper::toResponse)
                        .collect(Collectors.toList()),
                page,
                size,
                allUsers.size(),
                (int) Math.ceil((double) allUsers.size() / size));
    }

    @Override
    @Transactional
    public void deleteUser(UUID id) {
        userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));

        // Delete all company_user relationships first
        List<CompanyUser> companyUsers = companyUserRepository.findByUserId(id);
        companyUserRepository.deleteAll(companyUsers);

        // Then delete the user
        userRepository.deleteById(id);
    }
}
