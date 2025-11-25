import { useQuery, useMutation, useQueryClient } from "@tanstack/react-query";
import apiClient from "./client";
import type {
  CompanyRequest,
  CompanyResponse,
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
        PaginationResponse<CompanyResponse>
      >(COMPANY_ENDPOINT, { params });
      return data;
    },
  });
};

// Get company by ID
export const useCompany = (id: string) => {
  return useQuery({
    queryKey: ["companies", id],
    queryFn: async () => {
      const { data } = await apiClient.get<CompanyResponse>(
        `${COMPANY_ENDPOINT}/${id}`
      );
      return data;
    },
    enabled: !!id,
  });
};

// Create company
export const useCreateCompany = () => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: async (companyData: CompanyRequest) => {
      const { data } = await apiClient.post<CompanyResponse>(
        COMPANY_ENDPOINT,
        companyData
      );
      return data;
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
      const { data } = await apiClient.put<CompanyResponse>(
        `${COMPANY_ENDPOINT}/${id}`,
        companyData
      );
      return data;
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
      await apiClient.delete<void>(`${COMPANY_ENDPOINT}/${id}`);
      return id;
    },
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ["companies"] });
    },
  });
};
