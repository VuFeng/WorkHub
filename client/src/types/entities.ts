import { UserRole, JobStatus, JobPriority, TaskStatus } from './enums';

// Base entity with common fields
export interface BaseEntity {
  id: string;
  createdAt: string;
  updatedAt?: string;
}

// Company
export interface Company extends BaseEntity {
  name: string;
  address: string;
  logoUrl?: string;
}

// User
export interface User extends BaseEntity {
  companyId: string;
  companyName?: string;
  fullName: string;
  email: string;
  avatarUrl?: string;
  role: UserRole;
  isActive: boolean;
}

// Job
export interface Job extends BaseEntity {
  companyId: string;
  companyName?: string;
  ownerId: string;
  ownerName?: string;
  title: string;
  description?: string;
  status: JobStatus;
  priority: JobPriority;
  deadline?: string;
}

// Task
export interface Task extends BaseEntity {
  companyId: string;
  companyName?: string;
  jobId: string;
  jobTitle?: string;
  assigneeId: string;
  assigneeName?: string;
  title: string;
  description?: string;
  status: TaskStatus;
  startDate?: string;
  dueDate?: string;
}

// Task Comment
export interface TaskComment {
  id: string;
  taskId: string;
  taskTitle?: string;
  userId: string;
  userName?: string;
  message: string;
  createdAt: string;
}

