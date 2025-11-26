import { BrowserRouter, Routes, Route, Navigate } from "react-router-dom";
import { ROUTES } from "./constants";
import { ProtectedRoute } from "./components/ProtectedRoute";

// Layouts
import MainLayout from "./layouts/MainLayout";
import AuthLayout from "./layouts/AuthLayout";

// Pages
import DashboardPage from "./pages/DashboardPage";
import NotFoundPage from "./pages/NotFoundPage";
import { LoginPage } from "./modules/auth";
import { CompanyListPage } from "./modules/company";
import { UserManagementPage } from "./modules/user";
import { JobManagementPage } from "./modules/job";
import { TaskBoardPage, TaskDetailPage } from "./modules/task";
function App() {
  return (
    <BrowserRouter>
      <Routes>
        {/* Auth routes */}
        <Route element={<AuthLayout />}>
          <Route
            path={ROUTES.LOGIN}
            element={
              <ProtectedRoute requireAuth={false}>
                <LoginPage />
              </ProtectedRoute>
            }
          />
        </Route>

        {/* Protected routes */}
        <Route
          element={
            <ProtectedRoute>
              <MainLayout />
            </ProtectedRoute>
          }
        >
          <Route path={ROUTES.DASHBOARD} element={<DashboardPage />} />
          <Route path={ROUTES.COMPANIES} element={<CompanyListPage />} />
          <Route path={ROUTES.USERS} element={<UserManagementPage />} />
          <Route path={ROUTES.JOBS} element={<JobManagementPage />} />
          <Route path={ROUTES.TASKS} element={<TaskBoardPage />} />
          <Route path="/tasks/:id" element={<TaskDetailPage />} />
        </Route>

        {/* Redirect root to dashboard */}
        <Route path="/" element={<Navigate to={ROUTES.DASHBOARD} replace />} />

        {/* 404 */}
        <Route path="*" element={<NotFoundPage />} />
      </Routes>
    </BrowserRouter>
  );
}

export default App;
