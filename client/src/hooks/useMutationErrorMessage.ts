import { useMemo } from "react";
import type { ApiError } from "../types";

export const useMutationErrorMessage = (
  error: unknown,
  fallbackMessage: string
) => {
  return useMemo(() => {
    if (!error) {
      return null;
    }

    if (error instanceof Error && error.message) {
      return error.message;
    }

    if (
      typeof error === "object" &&
      error !== null &&
      "message" in error &&
      typeof (error as Partial<ApiError>).message === "string"
    ) {
      return (error as ApiError).message ?? fallbackMessage;
    }

    return fallbackMessage;
  }, [error, fallbackMessage]);
};


