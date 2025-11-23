import { useQuery, useMutation, useQueryClient } from "@tanstack/react-query";
import apiClient from "./client";
import type {
  CompanyRequest,
  CompanyResponse,
  ApiResponse,
  PaginationResponse,
  PaginationParams,
} from "../types";

// Company API endpoints
const COMPANY_ENDPOINT = "/companies";

// Get all companies with pagination
export const useCompanies = (params?: PaginationParams) => {
  return useQuery({
    queryKey: ["companies", params],
    queryFn: async () => {
      const { data } = await apiClient.get<
        ApiResponse<PaginationResponse<CompanyResponse>>
      >(COMPANY_ENDPOINT, { params });
      return data.data;
    },
  });
};

// Get company by ID
export const useCompany = (id: string) => {
  return useQuery({
    queryKey: ["companies", id],
    queryFn: async () => {
      const { data } = await apiClient.get<ApiResponse<CompanyResponse>>(
        `${COMPANY_ENDPOINT}/${id}`
      );
      return data.data;
    },
    enabled: !!id,
  });
};

// Create company
export const useCreateCompany = () => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: async (companyData: CompanyRequest) => {
      const { data } = await apiClient.post<ApiResponse<CompanyResponse>>(
        COMPANY_ENDPOINT,
        companyData
      );
      return data.data;
    },
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ["companies"] });
    },
  });
};

// Update company
export const useUpdateCompany = () => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: async ({
      id,
      companyData,
    }: {
      id: string;
      companyData: CompanyRequest;
    }) => {
      const { data } = await apiClient.put<ApiResponse<CompanyResponse>>(
        `${COMPANY_ENDPOINT}/${id}`,
        companyData
      );
      return data.data;
    },
    onSuccess: (_, variables) => {
      queryClient.invalidateQueries({ queryKey: ["companies"] });
      queryClient.invalidateQueries({ queryKey: ["companies", variables.id] });
    },
  });
};

// Delete company
export const useDeleteCompany = () => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: async (id: string) => {
      await apiClient.delete<ApiResponse<void>>(`${COMPANY_ENDPOINT}/${id}`);
      return id;
    },
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ["companies"] });
    },
  });
};
