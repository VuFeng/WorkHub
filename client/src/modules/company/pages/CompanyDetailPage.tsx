import { useState, useMemo } from "react";
import { Link, useParams, useNavigate } from "react-router-dom";
import {
  ArrowLeft,
  Edit2,
  MapPin,
  Briefcase,
  Users as UsersIcon,
  Calendar,
  UserPlus,
} from "lucide-react";
import type { CompanyRequest } from "../../../types";
import { UserRole } from "../../../types";
import { useAuthStore } from "../../../stores";
import { useCompanyDetail, useUpdateCompany, useAddUserToCompany } from "../hooks";
import { CompanyForm, AddUserToCompanyModal } from "../components";
import { useUsers } from "../../../api/userApi";
import {
  Button,
  Card,
  Badge,
  Label,
  Loading,
  Tabs,
  TabsList,
  TabsTrigger,
  TabsContent,
  Table,
} from "../../../components/ui";
import { ROUTES } from "../../../constants";

const CompanyDetailPage = () => {
  const { id } = useParams<{ id: string }>();
  const navigate = useNavigate();
  const [isFormOpen, setIsFormOpen] = useState(false);
  const [isAddUserModalOpen, setIsAddUserModalOpen] = useState(false);

  const canManageCompanies = useAuthStore(
    (state) => state.user?.role === UserRole.ADMIN
  );

  const { data: company, isLoading, error, refetch } = useCompanyDetail(id);
  const { data: allUsersData, isLoading: isLoadingUsers } = useUsers({ page: 0, size: 1000 });
  const updateCompany = useUpdateCompany();
  const addUserToCompany = useAddUserToCompany();

  // Filter users that are not already in the company
  const availableUsers = useMemo(() => {
    if (!allUsersData?.items || !company?.users) {
      console.log("Available users calculation:", {
        hasAllUsers: !!allUsersData?.items,
        allUsersCount: allUsersData?.items?.length,
        hasCompanyUsers: !!company?.users,
        companyUsersCount: company?.users?.length,
      });
      return [];
    }
    const companyUserIds = new Set(company.users.map((u) => u.id));
    const filtered = allUsersData.items.filter((user) => !companyUserIds.has(user.id));
    console.log("Available users:", filtered.length, "out of", allUsersData.items.length);
    return filtered;
  }, [allUsersData, company]);

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

  if (isLoading) {
    return (
      <div className="p-6">
        <Loading text="Loading company details..." />
      </div>
    );
  }

  if (error || !company) {
    return (
      <div className="p-6">
        <div className="text-center py-8">
          <p className="text-red-600 mb-4">
            {error instanceof Error ? error.message : "Company not found"}
          </p>
          <Button onClick={() => navigate(ROUTES.COMPANIES)}>
            Back to Companies
          </Button>
        </div>
      </div>
    );
  }

  const handleEditClick = () => {
    setIsFormOpen(true);
  };

  const handleFormSuccess = () => {
    refetch();
    setIsFormOpen(false);
  };

  const handleAddUserClick = () => {
    console.log("Add user clicked", {
      canManageCompanies,
      availableUsersCount: availableUsers.length,
      isLoadingUsers,
    });
    setIsAddUserModalOpen(true);
  };

  const handleAddUserConfirm = (userId: string) => {
    if (!id) return;
    console.log("Adding user to company:", { companyId: id, userId });
    addUserToCompany.mutate(
      { companyId: id, userId },
      {
        onSuccess: () => {
          console.log("User added successfully");
          refetch();
          setIsAddUserModalOpen(false);
        },
        onError: (error) => {
          console.error("Failed to add user:", error);
        },
      }
    );
  };

  const formatDate = (dateString: string) => {
    return new Date(dateString).toLocaleDateString("en-US", {
      year: "numeric",
      month: "long",
      day: "numeric",
    });
  };

  return (
    <div className="p-6 space-y-6">
      {/* Header */}
      <div className="flex items-center gap-4">
        <Link to={ROUTES.COMPANIES}>
          <Button variant="outline" size="sm" className="gap-2">
            <ArrowLeft size={16} />
            Back
          </Button>
        </Link>
        <div className="flex-1">
          <h1 className="text-3xl font-bold text-foreground">{company.name}</h1>
          <p className="text-muted-foreground mt-1">{company.address}</p>
        </div>
        {canManageCompanies && (
          <div className="flex gap-2">
            <Button onClick={handleEditClick} className="gap-2">
              <Edit2 size={16} />
              Edit
            </Button>
          </div>
        )}
      </div>

      {/* Quick Stats */}
      <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
        <Card className="p-6">
          <div className="flex items-start justify-between">
            <div>
              <p className="text-muted-foreground text-sm">Total Users</p>
              <p className="text-3xl font-bold mt-2">
                {company.users?.length ?? 0}
              </p>
            </div>
            <UsersIcon className="text-sidebar-primary" size={24} />
          </div>
        </Card>
        <Card className="p-6">
          <div className="flex items-start justify-between">
            <div>
              <p className="text-muted-foreground text-sm">Created Date</p>
              <p className="text-lg font-bold mt-2">
                {formatDate(company.createdAt)}
              </p>
            </div>
            <Calendar className="text-green-600" size={24} />
          </div>
        </Card>
        <Card className="p-6">
          <div className="flex items-start justify-between">
            <div>
              <p className="text-muted-foreground text-sm">Last Updated</p>
              <p className="text-lg font-bold mt-2">
                {company.updatedAt ? formatDate(company.updatedAt) : "-"}
              </p>
            </div>
            <Briefcase className="text-blue-600" size={24} />
          </div>
        </Card>
      </div>

      {/* Tabs Section */}
      <Tabs defaultValue="info" className="w-full">
        <TabsList className="grid w-full grid-cols-2">
          <TabsTrigger value="info">Company Info</TabsTrigger>
          <TabsTrigger value="users">Users</TabsTrigger>
        </TabsList>

        {/* Company Info Tab */}
        <TabsContent value="info" className="space-y-4">
          <Card className="p-6">
            <h3 className="text-lg font-bold mb-6">Contact Information</h3>
            <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
              <div className="space-y-2">
                <Label className="text-muted-foreground">Address</Label>
                <div className="flex items-start gap-2">
                  <MapPin
                    size={18}
                    className="text-muted-foreground mt-1 shrink-0"
                  />
                  <p className="text-foreground">{company.address}</p>
                </div>
              </div>
              {company.logoUrl && (
                <div className="space-y-2">
                  <Label className="text-muted-foreground">Logo</Label>
                  <div className="flex items-center gap-2">
                    <img
                      src={company.logoUrl}
                      alt={`${company.name} logo`}
                      className="h-16 w-16 object-cover rounded border border-gray-200"
                      onError={(e) => {
                        (e.target as HTMLImageElement).style.display = "none";
                      }}
                    />
                  </div>
                </div>
              )}
            </div>
          </Card>

          <Card className="p-6">
            <h3 className="text-lg font-bold mb-6">Business Information</h3>
            <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
              <div className="space-y-2">
                <Label className="text-muted-foreground">Company Name</Label>
                <p className="font-medium">{company.name}</p>
              </div>
              <div className="space-y-2">
                <Label className="text-muted-foreground">Created Date</Label>
                <p>{formatDate(company.createdAt)}</p>
              </div>
              {company.updatedAt && (
                <div className="space-y-2">
                  <Label className="text-muted-foreground">Last Updated</Label>
                  <p>{formatDate(company.updatedAt)}</p>
                </div>
              )}
            </div>
          </Card>
        </TabsContent>

        {/* Users Tab */}
        <TabsContent value="users">
          <Card className="p-6">
            <div className="flex items-center justify-between mb-6">
              <div className="flex items-center gap-3">
                <h3 className="text-lg font-bold">Company Users</h3>
                <Badge>{company.users?.length ?? 0} users</Badge>
              </div>
              {canManageCompanies && (
                <Button
                  onClick={handleAddUserClick}
                  className="gap-2 cursor-pointer"
                  size="sm"
                >
                  <UserPlus size={16} />
                  Add User
                </Button>
              )}
            </div>
            {company.users && company.users.length > 0 ? (
              <div className="overflow-x-auto">
                <Table>
                  <Table.Header>
                    <Table.Row>
                      <Table.Head>Name</Table.Head>
                      <Table.Head>Email</Table.Head>
                      <Table.Head>Role</Table.Head>
                      <Table.Head>Status</Table.Head>
                    </Table.Row>
                  </Table.Header>
                  <Table.Body>
                    {company.users.map((user) => (
                      <Table.Row key={user.id}>
                        <Table.Cell className="font-medium">
                          {user.fullName}
                        </Table.Cell>
                        <Table.Cell>{user.email}</Table.Cell>
                        <Table.Cell>
                          <Badge variant="info">{user.role}</Badge>
                        </Table.Cell>
                        <Table.Cell>
                          <Badge variant={user.isActive ? "success" : "danger"}>
                            {user.isActive ? "Active" : "Inactive"}
                          </Badge>
                        </Table.Cell>
                      </Table.Row>
                    ))}
                  </Table.Body>
                </Table>
              </div>
            ) : (
              <div className="text-center py-8 text-muted-foreground">
                No users found for this company.
              </div>
            )}
          </Card>
        </TabsContent>
      </Tabs>

      {/* Edit Form Modal */}
      {canManageCompanies && (
        <>
          <CompanyForm
            isOpen={isFormOpen}
            onOpenChange={setIsFormOpen}
            editingCompany={company}
            onSuccess={handleFormSuccess}
            onCreate={async () => {}}
            onUpdate={handleUpdate}
            isCreating={false}
            isUpdating={updateCompany.isPending}
          />
          <AddUserToCompanyModal
            isOpen={isAddUserModalOpen}
            onOpenChange={setIsAddUserModalOpen}
            availableUsers={availableUsers}
            onConfirm={handleAddUserConfirm}
            isAdding={addUserToCompany.isPending}
          />
        </>
      )}
    </div>
  );
};

export default CompanyDetailPage;
