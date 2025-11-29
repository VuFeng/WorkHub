import { useMemo, useState } from "react";
import { Plus } from "lucide-react";
import type { CompanyRequest, CompanyResponse } from "../../../types";
import { UserRole } from "../../../types";
import { useAuthStore } from "../../../stores";
import {
  useCompanyList,
  useCreateCompany,
  useDeleteCompany,
  useUpdateCompany,
} from "../hooks";
import { CompanyForm, CompanyTable, DeleteCompanyModal } from "../components";
import { Button, Loading } from "../../../components/ui";

const CompanyListPage = () => {
  const [isFormOpen, setIsFormOpen] = useState(false);
  const [editingCompany, setEditingCompany] = useState<CompanyResponse | null>(
    null
  );
  const [deletingCompany, setDeletingCompany] =
    useState<CompanyResponse | null>(null);

  const canManageCompanies = useAuthStore(
    (state) => state.user?.role === UserRole.ADMIN
  );

  const { data, isLoading, error, refetch, isError } = useCompanyList({
    page: 0,
    size: 100,
  });

  const createCompany = useCreateCompany();
  const updateCompany = useUpdateCompany();
  const deleteCompany = useDeleteCompany();

  const companies = useMemo(() => data?.items ?? [], [data]);

  const handleAddClick = () => {
    setEditingCompany(null);
    setIsFormOpen(true);
  };

  const handleEditClick = (company: CompanyResponse) => {
    setEditingCompany(company);
    setIsFormOpen(true);
  };

  const handleDeleteClick = (id: string) => {
    const company = companies.find((c) => c.id === id);
    if (!company) return;
    setDeletingCompany(company);
  };

  const handleConfirmDelete = () => {
    if (!deletingCompany) return;
    deleteCompany.mutate(deletingCompany.id, {
      onSuccess: () => {
        setDeletingCompany(null);
        refetch();
      },
    });
  };

  const handleCreate = async (payload: CompanyRequest) => {
    return new Promise<void>((resolve, reject) => {
      createCompany.mutate(payload, {
        onSuccess: () => {
          refetch();
          resolve();
        },
        onError: reject,
      });
    });
  };

  const handleUpdate = async (id: string, payload: CompanyRequest) => {
    return new Promise<void>((resolve, reject) => {
      updateCompany.mutate(
        { id, payload },
        {
          onSuccess: () => {
            refetch();
            resolve();
          },
          onError: reject,
        }
      );
    });
  };

  const handleFormSuccess = () => {
    setEditingCompany(null);
  };

  if (isLoading) {
    return (
      <div className="p-6">
        <Loading text="Loading companies..." />
      </div>
    );
  }

  if (isError) {
    return (
      <div className="p-6">
        <div className="text-center py-8">
          <p className="text-red-600 mb-4">
            {error instanceof Error
              ? error.message
              : "Unable to load companies"}
          </p>
          <Button onClick={() => refetch()}>Retry</Button>
        </div>
      </div>
    );
  }

  return (
    <div className="p-6 space-y-8">
      <div className="flex items-center justify-between">
        <div>
          <h1 className="text-3xl font-bold text-foreground">
            Company Management
          </h1>
          <p className="text-muted-foreground mt-2">
            Manage your repair shop company information
          </p>
        </div>
        {canManageCompanies && (
          <Button onClick={handleAddClick} className="gap-2 cursor-pointer">
            <Plus size={20} />
            Add Company
          </Button>
        )}
      </div>

      {canManageCompanies && (
        <CompanyForm
          isOpen={isFormOpen}
          onOpenChange={setIsFormOpen}
          editingCompany={editingCompany}
          onSuccess={handleFormSuccess}
          onCreate={handleCreate}
          onUpdate={handleUpdate}
          isCreating={createCompany.isPending}
          isUpdating={updateCompany.isPending}
        />
      )}

      <CompanyTable
        companies={companies}
        canManage={canManageCompanies}
        onEdit={handleEditClick}
        onDelete={handleDeleteClick}
      />

      {canManageCompanies && deletingCompany && (
        <DeleteCompanyModal
          isOpen={!!deletingCompany}
          onOpenChange={(open) => !open && setDeletingCompany(null)}
          companyName={deletingCompany.name}
          onConfirm={handleConfirmDelete}
          isDeleting={deleteCompany.isPending}
        />
      )}
    </div>
  );
};

export default CompanyListPage;
