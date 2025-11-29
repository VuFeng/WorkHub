import { keepPreviousData, useQuery } from "@tanstack/react-query";
import type {
  CompanyResponse,
  PaginationParams,
  PaginationResponse,
} from "../../../types";
import { fetchCompanies, fetchCompanyById } from "../services/companyService";
import { companyKeys } from "../constants";

export const useCompanyList = (params?: PaginationParams) => {
  return useQuery<PaginationResponse<CompanyResponse>>({
    queryKey: companyKeys.list(params),
    queryFn: () => fetchCompanies(params),
    placeholderData: keepPreviousData,
  });
};

export const useCompanyDetail = (id?: string) => {
  return useQuery<CompanyResponse>({
    queryKey: companyKeys.detail(id ?? ""),
    queryFn: () => {
      if (!id) {
        throw new Error("Company id is required");
      }
      return fetchCompanyById(id);
    },
    enabled: Boolean(id),
  });
};
