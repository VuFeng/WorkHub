import axios from "axios";
import type {
  AxiosError,
  AxiosInstance,
  AxiosResponse,
  InternalAxiosRequestConfig,
} from "axios";
import { API_BASE_URL, ROUTES } from "../constants";
import type { ApiError, ApiResponse, ErrorResponse } from "../types";
import { useAuthStore } from "../stores";

const apiClient: AxiosInstance = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    "Content-Type": "application/json",
  },
  timeout: 10000,
});

const isApiResponse = (payload: unknown): payload is ApiResponse<unknown> => {
  return (
    typeof payload === "object" &&
    payload !== null &&
    "success" in payload &&
    "timestamp" in payload
  );
};

const isErrorResponse = (payload: unknown): payload is ErrorResponse => {
  return (
    typeof payload === "object" &&
    payload !== null &&
    "status" in payload &&
    "message" in payload &&
    "error" in payload
  );
};

const handleUnauthorized = () => {
  const { logout } = useAuthStore.getState();
  logout();
  window.location.assign(ROUTES.LOGIN);
};

const buildApiError = (
  error: AxiosError<ApiResponse<ErrorResponse>>
): ApiError => {
  if (error.response) {
    const { status, data } = error.response;
    const errorPayload = isApiResponse(data) ? data.data : data;
    const details = isErrorResponse(errorPayload) ? errorPayload : undefined;

    return {
      status: status ?? 0,
      message:
        details?.message ||
        data?.message ||
        error.message ||
        "Đã xảy ra lỗi không mong muốn.",
      details,
      raw: data,
    };
  }

  if (error.request) {
    return {
      status: 0,
      message: "Không thể kết nối máy chủ. Vui lòng kiểm tra mạng.",
      raw: error.request,
    };
  }

  return {
    status: 0,
    message: error.message || "Đã xảy ra lỗi không xác định.",
  };
};

apiClient.interceptors.request.use(
  (config: InternalAxiosRequestConfig) => {
    const { token } = useAuthStore.getState();
    const fallbackToken = localStorage.getItem("authToken");
    const authToken = token || fallbackToken;

    if (authToken && config.headers) {
      config.headers.Authorization = `Bearer ${authToken}`;
    }
    return config;
  },
  (error) => Promise.reject(error)
);

apiClient.interceptors.response.use(
  (response: AxiosResponse<ApiResponse<unknown>>) => {
    if (isApiResponse(response.data)) {
      return {
        ...response,
        data: response.data.data ?? null,
      };
    }
    return response;
  },
  (error: AxiosError<ApiResponse<ErrorResponse>>) => {
    const apiError = buildApiError(error);

    if (apiError.status === 401) {
      handleUnauthorized();
    }

    if (apiError.status === 403) {
      console.warn("Access denied:", apiError.message);
    }

    return Promise.reject(apiError);
  }
);

export default apiClient;
