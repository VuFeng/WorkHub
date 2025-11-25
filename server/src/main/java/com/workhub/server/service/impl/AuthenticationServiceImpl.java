package com.workhub.server.service.impl;

import com.workhub.server.dto.request.LoginRequest;
import com.workhub.server.dto.request.RegisterRequest;
import com.workhub.server.dto.response.LoginResponse;
import com.workhub.server.dto.response.RegisterResponse;
import com.workhub.server.dto.response.UserResponse;
import com.workhub.server.entity.Company;
import com.workhub.server.entity.User;
import com.workhub.server.exception.custom.CompanyNotFoundException;
import com.workhub.server.exception.custom.DuplicateEmailException;
import com.workhub.server.mapper.UserMapper;
import com.workhub.server.repository.CompanyRepository;
import com.workhub.server.repository.UserRepository;
import com.workhub.server.security.CustomUserDetailsService;
import com.workhub.server.security.JwtTokenProvider;
import com.workhub.server.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@SuppressWarnings("null")
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepository userRepository;
    private final CompanyRepository companyRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;
    private final CustomUserDetailsService userDetailsService;

    @Override
    @Transactional
    public LoginResponse login(LoginRequest request) {
        try {
            // Authenticate user
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("Invalid email or password");
        }

        // Load user details
        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getEmail());
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new BadCredentialsException("User not found"));

        // Generate token with extra claims
        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("userId", user.getId().toString());
        extraClaims.put("role", user.getRole().name());
        extraClaims.put("companyId", user.getCompany().getId().toString());

        String token = jwtTokenProvider.generateToken(userDetails, extraClaims);

        UserResponse userResponse = userMapper.toResponse(user);

        return LoginResponse.builder()
                .token(token)
                .type("Bearer")
                .user(userResponse)
                .build();
    }

    @Override
    @Transactional
    public RegisterResponse register(RegisterRequest request) {
        // Check if email already exists
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateEmailException(request.getEmail());
        }

        // Check if company exists
        Company company = companyRepository.findById(request.getCompanyId())
                .orElseThrow(() -> new CompanyNotFoundException(request.getCompanyId()));

        // Create new user
        User user = new User();
        user.setCompany(company);
        user.setFullName(request.getFullName());
        user.setEmail(request.getEmail());
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        user.setRole(request.getRole());
        user.setIsActive(true);

        User savedUser = userRepository.save(user);

        // Generate token
        UserDetails userDetails = userDetailsService.loadUserByUsername(savedUser.getEmail());
        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("userId", savedUser.getId().toString());
        extraClaims.put("role", savedUser.getRole().name());
        extraClaims.put("companyId", savedUser.getCompany().getId().toString());

        String token = jwtTokenProvider.generateToken(userDetails, extraClaims);

        UserResponse userResponse = userMapper.toResponse(savedUser);

        return RegisterResponse.builder()
                .token(token)
                .type("Bearer")
                .user(userResponse)
                .build();
    }
}

