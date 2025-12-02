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
import com.workhub.server.dto.request.UserRequest;
import com.workhub.server.dto.response.ApiResponse;
import com.workhub.server.dto.response.PaginationResponse;
import com.workhub.server.dto.response.UserResponse;
import com.workhub.server.security.annotation.RequireAnyRole;
import com.workhub.server.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Validated
public class UserController {
    private final UserService userService;

    @PostMapping
    @RequireAnyRole({UserRole.ADMIN})
    public ResponseEntity<ApiResponse<UserResponse>> createUser(
            @Valid @RequestBody UserRequest request) {
        UserResponse user = userService.createUser(request);
        ApiResponse<UserResponse> response = ApiResponse.success("User created successfully", user);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    @RequireAnyRole({UserRole.ADMIN, UserRole.MANAGER, UserRole.STAFF})
    public ResponseEntity<ApiResponse<UserResponse>> getUserById(@PathVariable UUID id) {
        UserResponse user = userService.getUserById(id);
        ApiResponse<UserResponse> response = ApiResponse.success(user);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    @RequireAnyRole({UserRole.ADMIN, UserRole.MANAGER})
    public ResponseEntity<ApiResponse<PaginationResponse<UserResponse>>> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        PaginationResponse<UserResponse> users = userService.getAllUsers(page, size);
        ApiResponse<PaginationResponse<UserResponse>> response = ApiResponse.success(users);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/company/{companyId}")
    @RequireAnyRole({UserRole.ADMIN, UserRole.MANAGER})
    public ResponseEntity<ApiResponse<PaginationResponse<UserResponse>>> getUsersByCompany(
            @PathVariable UUID companyId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        PaginationResponse<UserResponse> users = userService.getUsersByCompany(companyId, page, size);
        ApiResponse<PaginationResponse<UserResponse>> response = ApiResponse.success(users);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    @RequireAnyRole({UserRole.ADMIN, UserRole.MANAGER})
    public ResponseEntity<ApiResponse<UserResponse>> updateUser(
            @PathVariable UUID id,
            @Valid @RequestBody UserRequest request) {
        UserResponse user = userService.updateUser(id, request);
        ApiResponse<UserResponse> response = ApiResponse.success("User updated successfully", user);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @RequireAnyRole({UserRole.ADMIN, UserRole.MANAGER})
    public ResponseEntity<ApiResponse<Void>> deleteUser(@PathVariable UUID id) {
        userService.deleteUser(id);
        ApiResponse<Void> response = ApiResponse.successWithoutData("User deleted successfully");
        return ResponseEntity.ok(response);
    }
}
