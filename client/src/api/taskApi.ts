import { useQuery, useMutation, useQueryClient } from "@tanstack/react-query";
import apiClient from "./client";
import type {
  TaskRequest,
  TaskResponse,
  ApiResponse,
  PaginationResponse,
  TaskFilters,
} from "../types";

// Task API endpoints
const TASK_ENDPOINT = "/tasks";

// Get all tasks with pagination and filters
export const useTasks = (filters?: TaskFilters) => {
  return useQuery({
    queryKey: ["tasks", filters],
    queryFn: async () => {
      const { data } = await apiClient.get<
        ApiResponse<PaginationResponse<TaskResponse>>
      >(TASK_ENDPOINT, { params: filters });
      return data.data;
    },
  });
};

// Get task by ID
export const useTask = (id: string) => {
  return useQuery({
    queryKey: ["tasks", id],
    queryFn: async () => {
      const { data } = await apiClient.get<ApiResponse<TaskResponse>>(
        `${TASK_ENDPOINT}/${id}`
      );
      return data.data;
    },
    enabled: !!id,
  });
};

// Get tasks by job
export const useTasksByJob = (jobId: string, params?: TaskFilters) => {
  return useQuery({
    queryKey: ["tasks", "job", jobId, params],
    queryFn: async () => {
      const { data } = await apiClient.get<
        ApiResponse<PaginationResponse<TaskResponse>>
      >(`${TASK_ENDPOINT}/job/${jobId}`, { params });
      return data.data;
    },
    enabled: !!jobId,
  });
};

// Get tasks by assignee
export const useTasksByAssignee = (
  assigneeId: string,
  params?: TaskFilters
) => {
  return useQuery({
    queryKey: ["tasks", "assignee", assigneeId, params],
    queryFn: async () => {
      const { data } = await apiClient.get<
        ApiResponse<PaginationResponse<TaskResponse>>
      >(`${TASK_ENDPOINT}/assignee/${assigneeId}`, { params });
      return data.data;
    },
    enabled: !!assigneeId,
  });
};

// Get tasks by status
export const useTasksByStatus = (status: string, params?: TaskFilters) => {
  return useQuery({
    queryKey: ["tasks", "status", status, params],
    queryFn: async () => {
      const { data } = await apiClient.get<
        ApiResponse<PaginationResponse<TaskResponse>>
      >(`${TASK_ENDPOINT}/status/${status}`, { params });
      return data.data;
    },
    enabled: !!status,
  });
};

// Create task
export const useCreateTask = () => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: async (taskData: TaskRequest) => {
      const { data } = await apiClient.post<ApiResponse<TaskResponse>>(
        TASK_ENDPOINT,
        taskData
      );
      return data.data;
    },
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ["tasks"] });
    },
  });
};

// Update task
export const useUpdateTask = () => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: async ({
      id,
      taskData,
    }: {
      id: string;
      taskData: Partial<TaskRequest>;
    }) => {
      const { data } = await apiClient.put<ApiResponse<TaskResponse>>(
        `${TASK_ENDPOINT}/${id}`,
        taskData
      );
      return data.data;
    },
    onSuccess: (_, variables) => {
      queryClient.invalidateQueries({ queryKey: ["tasks"] });
      queryClient.invalidateQueries({ queryKey: ["tasks", variables.id] });
    },
  });
};

// Delete task
export const useDeleteTask = () => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: async (id: string) => {
      await apiClient.delete<ApiResponse<void>>(`${TASK_ENDPOINT}/${id}`);
      return id;
    },
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ["tasks"] });
    },
  });
};
