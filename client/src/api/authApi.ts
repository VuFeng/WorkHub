import { useMutation } from "@tanstack/react-query";
import { useNavigate } from "react-router-dom";
import apiClient from "./client";
import type { User, UserRole } from "../types";
import { useAuthStore } from "../stores";
import { ROUTES } from "../constants";
import toast from "react-hot-toast";

interface LoginRequest {
  email: string;
  password: string;
}

interface RegisterRequest {
  companyId: string;
  fullName: string;
  email: string;
  password: string;
  role: UserRole;
}

interface LoginResponse {
  token: string;
  type: string;
  user: User;
}

interface RegisterResponse {
  token: string;
  type: string;
  user: User;
}

// Login mutation
export const useLogin = () => {
  const navigate = useNavigate();
  const { login } = useAuthStore();

  return useMutation({
    mutationFn: async (credentials: LoginRequest) => {
      const { data } = await apiClient.post<LoginResponse>(
        "/auth/login",
        credentials
      );
      return data;
    },
    onSuccess: (data) => {
      login(data.user, data.token);
      toast.success("Login successful!");
      navigate(ROUTES.DASHBOARD);
    },
    onError: (error: Error | { message?: string }) => {
      toast.error(
        error instanceof Error
          ? error.message
          : error.message || "Login failed. Please check your credentials."
      );
    },
  });
};

// Register mutation
export const useRegister = () => {
  const navigate = useNavigate();
  const { login } = useAuthStore();

  return useMutation({
    mutationFn: async (request: RegisterRequest) => {
      const { data } = await apiClient.post<RegisterResponse>(
        "/auth/register",
        request
      );
      return data;
    },
    onSuccess: (data) => {
      login(data.user, data.token);
      toast.success("Registration successful!");
      navigate(ROUTES.DASHBOARD);
    },
    onError: (error: Error | { message?: string }) => {
      toast.error(
        error instanceof Error
          ? error.message
          : error.message || "Registration failed. Please try again."
      );
    },
  });
};
