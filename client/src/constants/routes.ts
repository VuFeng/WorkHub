// Route paths
export const ROUTES = {
  // Auth
  LOGIN: "/login",

  // Main
  DASHBOARD: "/dashboard",
  COMPANIES: "/companies",
  COMPANY_DETAIL: (id: string) => `/companies/${id}`,
  USERS: "/users",
  JOBS: "/jobs",
  TASKS: "/tasks",
  TASK_DETAIL: (id: string) => `/tasks/${id}`,
} as const;
