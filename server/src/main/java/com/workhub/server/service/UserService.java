package com.workhub.server.service;

import java.util.Map;

import com.workhub.server.dto.request.UserRequest;
import com.workhub.server.dto.response.UserResponse;
import com.workhub.server.dto.response.PaginationResponse;

public interface UserService {
    UserResponse createUser(UserRequest request);

    UserResponse updateUser(Long id, UserRequest request);

    UserResponse getUserById(Long id);

    PaginationResponse<UserResponse> getAllUsers(int page, int size);

    Map<String, Object> deleteUser(Long id);
}
