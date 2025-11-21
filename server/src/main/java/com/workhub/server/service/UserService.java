package com.workhub.server.service;

import java.util.UUID;

import com.workhub.server.dto.request.UserRequest;
import com.workhub.server.dto.response.UserResponse;
import com.workhub.server.dto.response.PaginationResponse;

public interface UserService {
    UserResponse createUser(UserRequest request);

    UserResponse updateUser(UUID id, UserRequest request);

    UserResponse getUserById(UUID id);

    PaginationResponse<UserResponse> getAllUsers(int page, int size);

    PaginationResponse<UserResponse> getUsersByCompany(UUID companyId, int page, int size);

    void deleteUser(UUID id);
}
