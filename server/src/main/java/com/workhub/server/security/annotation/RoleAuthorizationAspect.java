package com.workhub.server.security.annotation;

import com.workhub.server.constant.UserRole;
import com.workhub.server.security.SecurityService;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
@RequiredArgsConstructor
public class RoleAuthorizationAspect {

    private final SecurityService securityService;

    @Before("@within(requireRole)")
    public void checkClassLevelRole(RequireRole requireRole) {
        enforceRole(requireRole.value());
    }

    @Before("@annotation(requireRole)")
    public void checkMethodLevelRole(RequireRole requireRole) {
        enforceRole(requireRole.value());
    }

    @Before("@within(requireAnyRole)")
    public void checkClassLevelAnyRole(RequireAnyRole requireAnyRole) {
        enforceAnyRole(requireAnyRole.value());
    }

    @Before("@annotation(requireAnyRole)")
    public void checkMethodLevelAnyRole(RequireAnyRole requireAnyRole) {
        enforceAnyRole(requireAnyRole.value());
    }

    private void enforceRole(UserRole role) {
        if (role == null || securityService.hasRole(role)) {
            return;
        }
        throw new AccessDeniedException("Insufficient role to perform this action");
    }

    private void enforceAnyRole(UserRole[] roles) {
        if (roles == null || roles.length == 0) {
            throw new AccessDeniedException("No role configured for this action");
        }
        if (Arrays.stream(roles).anyMatch(securityService::hasRole)) {
            return;
        }
        throw new AccessDeniedException("Insufficient role to perform this action");
    }
}


