import { useMutation, useQueryClient } from "@tanstack/react-query";
import toast from "react-hot-toast";
import type { CompanyRequest } from "../../../types";
import {
  createCompany,
  updateCompany,
  deleteCompany,
  addUserToCompany,
} from "../services/companyService";
import { companyKeys } from "../constants";

export const useCreateCompany = () => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: (payload: CompanyRequest) => createCompany(payload),
    onSuccess: () => {
      toast.success("Company created successfully");
      queryClient.invalidateQueries({ queryKey: companyKeys.all });
    },
    onError: (error) => {
      const defaultMessage = "Unable to create company";
      const message = error instanceof Error ? error.message : defaultMessage;
      toast.error(message || defaultMessage);
    },
  });
};

export const useUpdateCompany = () => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: ({ id, payload }: { id: string; payload: CompanyRequest }) =>
      updateCompany({ id, payload }),
    onSuccess: () => {
      toast.success("Company updated successfully");
      queryClient.invalidateQueries({ queryKey: companyKeys.all });
    },
    onError: (error) => {
      const defaultMessage = "Unable to update company";
      const message = error instanceof Error ? error.message : defaultMessage;
      toast.error(message || defaultMessage);
    },
  });
};

export const useDeleteCompany = () => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: (id: string) => deleteCompany(id),
    onSuccess: () => {
      toast.success("Company deleted successfully");
      queryClient.invalidateQueries({ queryKey: companyKeys.all });
    },
    onError: (error) => {
      const defaultMessage = "Unable to delete company";
      const message = error instanceof Error ? error.message : defaultMessage;
      toast.error(message || defaultMessage);
    },
  });
};

export const useAddUserToCompany = () => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: ({ companyId, userId }: { companyId: string; userId: string }) =>
      addUserToCompany(companyId, userId),
    onSuccess: () => {
      toast.success("User added to company successfully");
      queryClient.invalidateQueries({ queryKey: companyKeys.all });
    },
    onError: (error) => {
      const defaultMessage = "Unable to add user to company";
      const message = error instanceof Error ? error.message : defaultMessage;
      toast.error(message || defaultMessage);
    },
  });
};
