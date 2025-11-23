import { useQuery, useMutation, useQueryClient } from "@tanstack/react-query";
import apiClient from "./client";
import type {
  JobRequest,
  JobResponse,
  ApiResponse,
  PaginationResponse,
  JobFilters,
} from "../types";

// Job API endpoints
const JOB_ENDPOINT = "/jobs";

// Get all jobs with pagination and filters
export const useJobs = (filters?: JobFilters) => {
  return useQuery({
    queryKey: ["jobs", filters],
    queryFn: async () => {
      const { data } = await apiClient.get<
        ApiResponse<PaginationResponse<JobResponse>>
      >(JOB_ENDPOINT, { params: filters });
      return data.data;
    },
  });
};

// Get job by ID
export const useJob = (id: string) => {
  return useQuery({
    queryKey: ["jobs", id],
    queryFn: async () => {
      const { data } = await apiClient.get<ApiResponse<JobResponse>>(
        `${JOB_ENDPOINT}/${id}`
      );
      return data.data;
    },
    enabled: !!id,
  });
};

// Get jobs by company
export const useJobsByCompany = (companyId: string, params?: JobFilters) => {
  return useQuery({
    queryKey: ["jobs", "company", companyId, params],
    queryFn: async () => {
      const { data } = await apiClient.get<
        ApiResponse<PaginationResponse<JobResponse>>
      >(`${JOB_ENDPOINT}/company/${companyId}`, { params });
      return data.data;
    },
    enabled: !!companyId,
  });
};

// Get jobs by owner
export const useJobsByOwner = (ownerId: string, params?: JobFilters) => {
  return useQuery({
    queryKey: ["jobs", "owner", ownerId, params],
    queryFn: async () => {
      const { data } = await apiClient.get<
        ApiResponse<PaginationResponse<JobResponse>>
      >(`${JOB_ENDPOINT}/owner/${ownerId}`, { params });
      return data.data;
    },
    enabled: !!ownerId,
  });
};

// Get jobs by status
export const useJobsByStatus = (status: string, params?: JobFilters) => {
  return useQuery({
    queryKey: ["jobs", "status", status, params],
    queryFn: async () => {
      const { data } = await apiClient.get<
        ApiResponse<PaginationResponse<JobResponse>>
      >(`${JOB_ENDPOINT}/status/${status}`, { params });
      return data.data;
    },
    enabled: !!status,
  });
};

// Create job
export const useCreateJob = () => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: async (jobData: JobRequest) => {
      const { data } = await apiClient.post<ApiResponse<JobResponse>>(
        JOB_ENDPOINT,
        jobData
      );
      return data.data;
    },
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ["jobs"] });
    },
  });
};

// Update job
export const useUpdateJob = () => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: async ({
      id,
      jobData,
    }: {
      id: string;
      jobData: Partial<JobRequest>;
    }) => {
      const { data } = await apiClient.put<ApiResponse<JobResponse>>(
        `${JOB_ENDPOINT}/${id}`,
        jobData
      );
      return data.data;
    },
    onSuccess: (_, variables) => {
      queryClient.invalidateQueries({ queryKey: ["jobs"] });
      queryClient.invalidateQueries({ queryKey: ["jobs", variables.id] });
    },
  });
};

// Delete job
export const useDeleteJob = () => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: async (id: string) => {
      await apiClient.delete<ApiResponse<void>>(`${JOB_ENDPOINT}/${id}`);
      return id;
    },
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ["jobs"] });
    },
  });
};
