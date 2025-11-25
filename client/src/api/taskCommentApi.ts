import { useQuery, useMutation, useQueryClient } from "@tanstack/react-query";
import apiClient from "./client";
import type {
  TaskCommentRequest,
  TaskCommentResponse,
  PaginationResponse,
  PaginationParams,
} from "../types";

// Task Comment API endpoints
const TASK_COMMENT_ENDPOINT = "/task-comments";

// Get all task comments with pagination
export const useTaskComments = (params?: PaginationParams) => {
  return useQuery({
    queryKey: ["task-comments", params],
    queryFn: async () => {
      const { data } = await apiClient.get<
        PaginationResponse<TaskCommentResponse>
      >(TASK_COMMENT_ENDPOINT, { params });
      return data;
    },
  });
};

// Get task comment by ID
export const useTaskComment = (id: string) => {
  return useQuery({
    queryKey: ["task-comments", id],
    queryFn: async () => {
      const { data } = await apiClient.get<TaskCommentResponse>(
        `${TASK_COMMENT_ENDPOINT}/${id}`
      );
      return data;
    },
    enabled: !!id,
  });
};

// Get comments by task
export const useTaskCommentsByTask = (
  taskId: string,
  params?: PaginationParams
) => {
  return useQuery({
    queryKey: ["task-comments", "task", taskId, params],
    queryFn: async () => {
      const { data } = await apiClient.get<
        PaginationResponse<TaskCommentResponse>
      >(`${TASK_COMMENT_ENDPOINT}/task/${taskId}`, { params });
      return data;
    },
    enabled: !!taskId,
  });
};

// Get comments by user
export const useTaskCommentsByUser = (
  userId: string,
  params?: PaginationParams
) => {
  return useQuery({
    queryKey: ["task-comments", "user", userId, params],
    queryFn: async () => {
      const { data } = await apiClient.get<
        PaginationResponse<TaskCommentResponse>
      >(`${TASK_COMMENT_ENDPOINT}/user/${userId}`, { params });
      return data;
    },
    enabled: !!userId,
  });
};

// Create task comment
export const useCreateTaskComment = () => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: async (commentData: TaskCommentRequest) => {
      const { data } = await apiClient.post<TaskCommentResponse>(
        TASK_COMMENT_ENDPOINT,
        commentData
      );
      return data;
    },
    onSuccess: (data) => {
      queryClient.invalidateQueries({ queryKey: ["task-comments"] });
      queryClient.invalidateQueries({
        queryKey: ["task-comments", "task", data.taskId],
      });
    },
  });
};

// Delete task comment
export const useDeleteTaskComment = () => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: async (id: string) => {
      await apiClient.delete<void>(`${TASK_COMMENT_ENDPOINT}/${id}`);
      return id;
    },
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ["task-comments"] });
    },
  });
};
