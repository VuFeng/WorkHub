import { Edit2, Trash2, Eye } from "lucide-react";
import { useNavigate } from "react-router-dom";
import type { CompanyResponse } from "../../../types";
import { Button, Card, Table } from "../../../components/ui";
import { LogoImage } from "./LogoImage";
import { ROUTES } from "../../../constants";

interface CompanyTableProps {
  companies: CompanyResponse[];
  canManage: boolean;
  onEdit: (company: CompanyResponse) => void;
  onDelete: (id: string) => void;
}

export const CompanyTable = ({
  companies,
  canManage,
  onEdit,
  onDelete,
}: CompanyTableProps) => {
  const navigate = useNavigate();

  const handleViewClick = (id: string) => {
    navigate(ROUTES.COMPANY_DETAIL(id));
  };

  if (companies.length === 0) {
    return (
      <Card className="p-6">
        <div className="text-center py-8 text-muted-foreground">
          No companies found. Click "Add Company" to create your first company.
        </div>
      </Card>
    );
  }

  return (
    <Card className="p-6">
      <div className="overflow-x-auto">
        <Table>
          <Table.Header>
            <Table.Row>
              <Table.Head>Company Name</Table.Head>
              <Table.Head>Address</Table.Head>
              <Table.Head>Logo</Table.Head>
              <Table.Head>Users</Table.Head>
              <Table.Head>Actions</Table.Head>
            </Table.Row>
          </Table.Header>
          <Table.Body>
            {companies.map((company) => (
              <Table.Row key={company.id}>
                <Table.Cell className="font-bold">{company.name}</Table.Cell>
                <Table.Cell>{company.address}</Table.Cell>
                <Table.Cell className="flex items-center gap-2">
                  {company.logoUrl ? (
                    <LogoImage
                      url={company.logoUrl}
                      alt={`${company.name} logo`}
                    />
                  ) : (
                    <span className="text-muted-foreground">-</span>
                  )}
                </Table.Cell>
                <Table.Cell>
                  <span className="text-muted-foreground">
                    {company.users?.length ?? 0} user
                    {company.users?.length !== 1 ? "s" : ""}
                  </span>
                </Table.Cell>
                <Table.Cell>
                  <div className="flex gap-2">
                    <Button
                      size="sm"
                      variant="ghost"
                      className="text-sidebar-primary hover:text-white hover:bg-green-600 cursor-pointer"
                      onClick={() => handleViewClick(company.id)}
                      title="View Details"
                    >
                      <Eye size={16} />
                    </Button>
                    {canManage && (
                      <>
                        <Button
                          size="sm"
                          variant="ghost"
                          className="text-sidebar-primary hover:text-white hover:bg-green-600 cursor-pointer"
                          onClick={() => onEdit(company)}
                          title="Edit"
                        >
                          <Edit2 size={16} />
                        </Button>
                        <Button
                          size="sm"
                          variant="ghost"
                          className="text-red-600 hover:bg-red-600 hover:text-white cursor-pointer"
                          onClick={() => onDelete(company.id)}
                          title="Delete"
                        >
                          <Trash2 size={16} />
                        </Button>
                      </>
                    )}
                  </div>
                </Table.Cell>
              </Table.Row>
            ))}
          </Table.Body>
        </Table>
      </div>
    </Card>
  );
};
