import type { Company, User, Job, Task, TaskComment } from "./entities";

// API Response wrapper from backend
export interface ApiResponse<T> {
  success: boolean;
  message: string;
  data?: T;
  timestamp: string;
}

export interface ValidationError {
  field: string;
  message: string;
  rejectedValue?: unknown;
}

export interface ErrorResponse {
  error: string;
  message: string;
  status: number;
  path: string;
  timestamp?: string;
  validationErrors?: ValidationError[];
}

export interface ApiError {
  status: number;
  message: string;
  details?: ErrorResponse;
  raw?: unknown;
}

// Pagination Response
export interface PaginationResponse<T> {
  items: T[];
  page: number;
  size: number;
  totalElements: number;
  totalPages: number;
}

// Pagination Params
export interface PaginationParams {
  page?: number;
  size?: number;
  sort?: string;
}

// Request DTOs
export interface CompanyRequest {
  name: string;
  address: string;
  logoUrl?: string;
}

export interface UserRequest {
  companyId: string;
  fullName: string;
  email: string;
  password: string;
  avatarUrl?: string;
  role: string;
  isActive?: boolean;
}

export interface JobRequest {
  companyId: string;
  ownerId: string;
  title: string;
  description?: string;
  status: string;
  priority: string;
  deadline?: string;
}

export interface TaskRequest {
  companyId: string;
  jobId: string;
  assigneeId: string;
  title: string;
  description?: string;
  status: string;
  startDate?: string;
  dueDate?: string;
}

export interface TaskCommentRequest {
  taskId: string;
  userId: string;
  message: string;
}

// Response DTOs (same as entities but explicitly typed)
export type CompanyResponse = Company;
export type UserResponse = User;
export type JobResponse = Job;
export type TaskResponse = Task;
export type TaskCommentResponse = TaskComment;

// Filter params
export interface JobFilters extends PaginationParams {
  companyId?: string;
  ownerId?: string;
  status?: string;
}

export interface TaskFilters extends PaginationParams {
  jobId?: string;
  assigneeId?: string;
  status?: string;
  companyId?: string;
}
