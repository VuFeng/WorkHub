import apiClient from "../../../api/client";
import type {
  CompanyRequest,
  CompanyResponse,
  PaginationParams,
  PaginationResponse,
} from "../../../types";

const COMPANY_ENDPOINT = "/companies";

export const fetchCompanies = async (
  params?: PaginationParams
): Promise<PaginationResponse<CompanyResponse>> => {
  const { data } = await apiClient.get<PaginationResponse<CompanyResponse>>(
    COMPANY_ENDPOINT,
    { params }
  );
  return data;
};

export const fetchCompanyById = async (
  id: string
): Promise<CompanyResponse> => {
  const { data } = await apiClient.get<CompanyResponse>(
    `${COMPANY_ENDPOINT}/${id}`
  );
  return data;
};

export const createCompany = async (
  payload: CompanyRequest
): Promise<CompanyResponse> => {
  const { data } = await apiClient.post<CompanyResponse>(
    COMPANY_ENDPOINT,
    payload
  );
  return data;
};

export const updateCompany = async ({
  id,
  payload,
}: {
  id: string;
  payload: CompanyRequest;
}): Promise<CompanyResponse> => {
  const { data } = await apiClient.put<CompanyResponse>(
    `${COMPANY_ENDPOINT}/${id}`,
    payload
  );
  return data;
};

export const deleteCompany = async (id: string): Promise<void> => {
  await apiClient.delete(`${COMPANY_ENDPOINT}/${id}`);
};
