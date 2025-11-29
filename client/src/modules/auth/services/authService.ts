import apiClient from "../../../api/client";
import type { User, UserRole } from "../../../types";

export interface LoginPayload {
  email: string;
  password: string;
}

export interface RegisterPayload {
  companyId: string;
  fullName: string;
  email: string;
  password: string;
  role: UserRole;
}

export interface AuthResponse {
  token: string;
  type: string;
  user: User;
  refreshToken?: string;
}

const AUTH_ENDPOINT = "/auth";

export const login = async (payload: LoginPayload): Promise<AuthResponse> => {
  const { data } = await apiClient.post<AuthResponse>(
    `${AUTH_ENDPOINT}/login`,
    payload
  );
  return data;
};

export const register = async (
  payload: RegisterPayload
): Promise<AuthResponse> => {
  const { data } = await apiClient.post<AuthResponse>(
    `${AUTH_ENDPOINT}/register`,
    payload
  );
  return data;
};
