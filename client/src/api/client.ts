import axios from "axios";
import type {
  AxiosError,
  AxiosInstance,
  InternalAxiosRequestConfig,
} from "axios";
import { API_BASE_URL } from "../constants";
import type { ApiResponse } from "../types";

// Create axios instance
const apiClient: AxiosInstance = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    "Content-Type": "application/json",
  },
  timeout: 10000,
});

// Request interceptor
apiClient.interceptors.request.use(
  (config: InternalAxiosRequestConfig) => {
    // Add auth token if available
    const token = localStorage.getItem("authToken");
    if (token && config.headers) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

// Response interceptor
apiClient.interceptors.response.use(
  (response) => {
    // Backend returns ApiResponse<T> wrapper
    return response;
  },
  (error: AxiosError<ApiResponse<unknown>>) => {
    // Handle errors
    if (error.response) {
      // Server responded with error status
      const { status, data } = error.response;

      // Handle 401 Unauthorized - redirect to login
      if (status === 401) {
        localStorage.removeItem("authToken");
        window.location.href = "/login";
      }

      // Return error with message from backend
      return Promise.reject({
        status,
        message: data?.message || error.message,
        data: data?.data,
      });
    } else if (error.request) {
      // Request was made but no response received
      return Promise.reject({
        status: 0,
        message: "Network error. Please check your connection.",
      });
    } else {
      // Something else happened
      return Promise.reject({
        status: 0,
        message: error.message || "An unexpected error occurred",
      });
    }
  }
);

export default apiClient;
