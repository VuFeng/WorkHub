// Route paths
export const ROUTES = {
  // Auth
  LOGIN: "/login",

  // Main
  DASHBOARD: "/dashboard",
  COMPANIES: "/companies",
  USERS: "/users",
  JOBS: "/jobs",
  TASKS: "/tasks",
  TASK_DETAIL: (id: string) => `/tasks/${id}`,
} as const;
