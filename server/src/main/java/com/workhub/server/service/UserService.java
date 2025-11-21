package com.workhub.server.service;

import com.workhub.server.dto.request.UserRequest;
import com.workhub.server.dto.response.UserResponse;
import com.workhub.server.dto.response.PaginationResponse;

public interface UserService {
    UserResponse createUser(UserRequest request);

    UserResponse updateUser(Long id, UserRequest request);

    UserResponse getUserById(Long id);

    PaginationResponse<UserResponse> getAllUsers(int page, int size);

    void deleteUser(Long id);
}
