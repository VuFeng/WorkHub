package com.workhub.server.service;

import com.workhub.server.dto.request.LoginRequest;
import com.workhub.server.dto.request.RegisterRequest;
import com.workhub.server.dto.response.LoginResponse;
import com.workhub.server.dto.response.RegisterResponse;

public interface AuthenticationService {
    LoginResponse login(LoginRequest request);
    RegisterResponse register(RegisterRequest request);
}

