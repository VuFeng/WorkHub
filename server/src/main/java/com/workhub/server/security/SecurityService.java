package com.workhub.server.security;

import com.workhub.server.constant.UserRole;
import com.workhub.server.entity.User;
import com.workhub.server.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

@Service("securityService")
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SecurityService {

    private final UserRepository userRepository;

    public Optional<User> getCurrentUser() {
        String email = getCurrentUserEmail();
        if (!StringUtils.hasText(email)) {
            return Optional.empty();
        }
        return userRepository.findByEmail(email);
    }

    public Optional<UUID> getCurrentUserId() {
        return getCurrentUser().map(User::getId);
    }

    public Optional<UUID> getCurrentCompanyId() {
        return getCurrentUser()
                .map(User::getCompany)
                .map(company -> company.getId());
    }

    public boolean hasRole(UserRole role) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }
        return authentication.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_" + role.name()));
    }

    public boolean hasAnyRole(UserRole... roles) {
        if (roles == null || roles.length == 0) {
            return false;
        }
        return Arrays.stream(roles).anyMatch(this::hasRole);
    }

    public boolean isCompanyMember(UUID companyId) {
        if (companyId == null) {
            return false;
        }
        return getCurrentCompanyId()
                .map(companyId::equals)
                .orElse(false);
    }

    private String getCurrentUserEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }
        Object principal = authentication.getPrincipal();
        if (principal instanceof org.springframework.security.core.userdetails.UserDetails userDetails) {
            return userDetails.getUsername();
        }
        if (principal instanceof String username) {
            return username;
        }
        return null;
    }
}


