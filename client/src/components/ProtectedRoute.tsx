import { Navigate, useLocation } from "react-router-dom";
import { useAuthStore } from "../stores";
import { ROUTES } from "../constants";
import type { UserRole } from "../types";

interface ProtectedRouteProps {
  children: React.ReactNode;
  requiredRole?: UserRole[];
  requireAuth?: boolean; // If false, redirect to dashboard if already authenticated
}

export const ProtectedRoute = ({
  children,
  requiredRole,
  requireAuth = true,
}: ProtectedRouteProps) => {
  const { isAuthenticated, user, hasAnyRole } = useAuthStore();
  const location = useLocation();

  // For auth pages (login), redirect to dashboard if already authenticated
  if (!requireAuth && isAuthenticated) {
    return <Navigate to={ROUTES.DASHBOARD} replace />;
  }

  // Redirect to login if not authenticated
  if (requireAuth && !isAuthenticated) {
    return <Navigate to={ROUTES.LOGIN} state={{ from: location }} replace />;
  }

  // Check role if required
  if (requireAuth && requiredRole && user && !hasAnyRole(requiredRole)) {
    // Redirect to dashboard if user doesn't have required role
    return <Navigate to={ROUTES.DASHBOARD} replace />;
  }

  return <>{children}</>;
};
