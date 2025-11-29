import { useMutation } from "@tanstack/react-query";
import { useNavigate } from "react-router-dom";
import toast from "react-hot-toast";
import { ROUTES } from "../../../constants";
import { useAuthStore } from "../../../stores";
import { login, type LoginPayload } from "../services/authService";

export const useLogin = () => {
  const navigate = useNavigate();
  const { login: persistLogin } = useAuthStore();

  return useMutation({
    mutationFn: (payload: LoginPayload) => login(payload),
    onSuccess: (data) => {
      persistLogin(data.user, data.token);
      toast.success("Login successful!");
      navigate(ROUTES.DASHBOARD);
    },
    onError: (error) => {
      const defaultMessage = "Login failed. Please check your credentials.";
      const message = error instanceof Error ? error.message : defaultMessage;
      toast.error(message || defaultMessage);
    },
  });
};
