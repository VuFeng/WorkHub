package com.workhub.server.service.impl;

import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.workhub.server.dto.request.UserRequest;
import com.workhub.server.dto.response.PaginationResponse;
import com.workhub.server.dto.response.UserResponse;
import com.workhub.server.entity.Company;
import com.workhub.server.entity.User;
import com.workhub.server.exception.custom.CompanyNotFoundException;
import com.workhub.server.exception.custom.DuplicateEmailException;
import com.workhub.server.exception.custom.UserNotFoundException;
import com.workhub.server.mapper.UserMapper;
import com.workhub.server.repository.CompanyRepository;
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
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    @Transactional
    public UserResponse createUser(UserRequest request) {
        // Kiểm tra email đã tồn tại chưa
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateEmailException(request.getEmail());
        }

        // Kiểm tra company có tồn tại không
        Company company = companyRepository.findById(request.getCompanyId())
                .orElseThrow(() -> new CompanyNotFoundException(request.getCompanyId()));

        User user = userMapper.toEntity(request);
        user.setCompany(company);
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));

        // Set default isActive if not provided
        if (user.getIsActive() == null) {
            user.setIsActive(true);
        }

        User savedUser = userRepository.save(user);
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

        // Nếu đổi company, kiểm tra company mới có tồn tại không
        if (!user.getCompany().getId().equals(request.getCompanyId())) {
            Company newCompany = companyRepository.findById(request.getCompanyId())
                    .orElseThrow(() -> new CompanyNotFoundException(request.getCompanyId()));
            user.setCompany(newCompany);
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

        Page<User> users = userRepository.findByCompanyId(companyId, PageRequest.of(page, size));

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
    @Transactional
    public void deleteUser(UUID id) {
        userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));

        userRepository.deleteById(id);
    }
}
