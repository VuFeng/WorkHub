import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { z } from "zod";
import { useLogin } from "../api/authApi";
import { Input, Button } from "../components/ui";
import Card from "../components/ui/Card";
import { useMutationErrorMessage } from "../hooks";

const loginSchema = z.object({
  email: z.string().email("Please enter a valid email"),
  password: z.string().min(6, "Password must be at least 6 characters"),
});

type LoginFormData = z.infer<typeof loginSchema>;

const LoginPage = () => {
  const loginMutation = useLogin();
  const {
    register,
    handleSubmit,
    formState: { errors },
  } = useForm<LoginFormData>({
    resolver: zodResolver(loginSchema),
  });

  const formError = useMutationErrorMessage(
    loginMutation.error,
    "Sign in failed. Please try again."
  );

  const onSubmit = (data: LoginFormData) => {
    loginMutation.mutate(data);
  };

  return (
    <div className="min-h-screen flex items-center justify-center bg-linear-to-br from-slate-50 to-blue-50 py-12 px-4 sm:px-6 lg:px-8">
      <div className="max-w-md w-full space-y-8">
        <div className="text-center">
          <p className="text-sm font-semibold text-blue-600 uppercase tracking-wide">
            WorkHub
          </p>
          <h1 className="mt-2 text-3xl font-semibold text-slate-900">
            Welcome back
          </h1>
          <p className="mt-2 text-sm text-slate-600">
            Sign in to stay synced with your team
          </p>
        </div>

        <Card>
          <form
            className="space-y-6"
            onSubmit={handleSubmit(onSubmit)}
            noValidate
          >
            {formError && (
              <div
                role="alert"
                className="rounded-lg border border-red-200 bg-red-50 px-4 py-2 text-sm text-red-600"
              >
                {formError}
              </div>
            )}

            <Input
              id="email"
              label="Email"
              type="email"
              autoComplete="email"
              placeholder="you@workhub.com"
              className="text-black"
              error={errors.email?.message}
              {...register("email")}
              required
            />

            <Input
              id="password"
              label="Password"
              type="password"
              autoComplete="current-password"
              placeholder="••••••••"
              className="text-black"
              error={errors.password?.message}
              {...register("password")}
              required
            />

            <Button
              type="submit"
              variant="primary"
              size="lg"
              className="w-full cursor-pointer"
              isLoading={loginMutation.isPending}
              disabled={loginMutation.isPending}
            >
              Sign in
            </Button>
          </form>

          <p className="mt-6 text-center text-sm text-slate-500">
            Need an account? Contact your WorkHub administrator.
          </p>
        </Card>
      </div>
    </div>
  );
};

export default LoginPage;
