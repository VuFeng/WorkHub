import type { ChangeEvent } from "react";
import { Input, Button } from "../../../components/ui";

interface CompanyToolbarProps {
  searchValue: string;
  onSearchChange: (value: string) => void;
  onCreateClick: () => void;
  canCreate: boolean;
}

const CompanyToolbar = ({
  searchValue,
  onSearchChange,
  onCreateClick,
  canCreate,
}: CompanyToolbarProps) => {
  const handleSearch = (event: ChangeEvent<HTMLInputElement>) => {
    onSearchChange(event.target.value);
  };

  return (
    <div className="flex flex-col gap-4 md:flex-row md:items-center md:justify-between">
      <Input
        id="company-search"
        placeholder="Search by company name..."
        value={searchValue}
        onChange={handleSearch}
        aria-label="Search company"
      />
      <Button variant="primary" onClick={onCreateClick} disabled={!canCreate}>
        Add company
      </Button>
    </div>
  );
};

export default CompanyToolbar;
