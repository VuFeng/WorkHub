import { useQuery, useMutation, useQueryClient } from "@tanstack/react-query";
import apiClient from "./client";
import type {
  UserRequest,
  UserResponse,
  ApiResponse,
  PaginationResponse,
  PaginationParams,
} from "../types";

// User API endpoints
const USER_ENDPOINT = "/users";

// Get all users with pagination
export const useUsers = (params?: PaginationParams) => {
  return useQuery({
    queryKey: ["users", params],
    queryFn: async () => {
      const { data } = await apiClient.get<
        ApiResponse<PaginationResponse<UserResponse>>
      >(USER_ENDPOINT, { params });
      return data.data;
    },
  });
};

// Get user by ID
export const useUser = (id: string) => {
  return useQuery({
    queryKey: ["users", id],
    queryFn: async () => {
      const { data } = await apiClient.get<ApiResponse<UserResponse>>(
        `${USER_ENDPOINT}/${id}`
      );
      return data.data;
    },
    enabled: !!id,
  });
};

// Get users by company
export const useUsersByCompany = (
  companyId: string,
  params?: PaginationParams
) => {
  return useQuery({
    queryKey: ["users", "company", companyId, params],
    queryFn: async () => {
      const { data } = await apiClient.get<
        ApiResponse<PaginationResponse<UserResponse>>
      >(`${USER_ENDPOINT}/company/${companyId}`, { params });
      return data.data;
    },
    enabled: !!companyId,
  });
};

// Create user
export const useCreateUser = () => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: async (userData: UserRequest) => {
      const { data } = await apiClient.post<ApiResponse<UserResponse>>(
        USER_ENDPOINT,
        userData
      );
      return data.data;
    },
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ["users"] });
    },
  });
};

// Update user
export const useUpdateUser = () => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: async ({
      id,
      userData,
    }: {
      id: string;
      userData: Partial<UserRequest>;
    }) => {
      const { data } = await apiClient.put<ApiResponse<UserResponse>>(
        `${USER_ENDPOINT}/${id}`,
        userData
      );
      return data.data;
    },
    onSuccess: (_, variables) => {
      queryClient.invalidateQueries({ queryKey: ["users"] });
      queryClient.invalidateQueries({ queryKey: ["users", variables.id] });
    },
  });
};

// Delete user
export const useDeleteUser = () => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: async (id: string) => {
      await apiClient.delete<ApiResponse<void>>(`${USER_ENDPOINT}/${id}`);
      return id;
    },
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ["users"] });
    },
  });
};
