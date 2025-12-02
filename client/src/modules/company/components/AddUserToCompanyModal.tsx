import { useState } from "react";
import { UserPlus } from "lucide-react";
import type { User } from "../../../types";
import { Button, Dialog, Label, Select } from "../../../components/ui";

interface AddUserToCompanyModalProps {
  isOpen: boolean;
  onOpenChange: (open: boolean) => void;
  availableUsers: User[];
  onConfirm: (userId: string) => void;
  isAdding: boolean;
}

export const AddUserToCompanyModal = ({
  isOpen,
  onOpenChange,
  availableUsers,
  onConfirm,
  isAdding,
}: AddUserToCompanyModalProps) => {
  const [selectedUserId, setSelectedUserId] = useState<string>("");

  const handleOpenChange = (open: boolean) => {
    if (!open) {
      setSelectedUserId("");
    }
    onOpenChange(open);
  };

  const handleConfirm = () => {
    if (selectedUserId) {
      onConfirm(selectedUserId);
      setSelectedUserId("");
    }
  };

  return (
    <Dialog open={isOpen} onOpenChange={handleOpenChange}>
      <Dialog.Content className="max-w-md">
        <Dialog.Header>
          <Dialog.Title>
            <div className="flex items-center gap-2">
              <UserPlus size={20} />
              Add User to Company
            </div>
          </Dialog.Title>
        </Dialog.Header>
        <div className="space-y-4">
          <div className="space-y-2">
            <Label htmlFor="user">Select User *</Label>
            {availableUsers.length > 0 ? (
              <Select
                id="user"
                value={selectedUserId}
                onChange={(e) => setSelectedUserId(e.target.value)}
                options={[
                  { value: "", label: "-- Select a user --" },
                  ...availableUsers.map((user) => ({
                    value: user.id,
                    label: `${user.fullName} (${user.email}) - ${user.role}`,
                  })),
                ]}
              />
            ) : (
              <p className="text-sm text-muted-foreground">
                No available users to add. All users are already members of this company.
              </p>
            )}
          </div>

          <div className="flex gap-3 justify-end pt-4">
            <Button
              variant="outline"
              onClick={() => handleOpenChange(false)}
              disabled={isAdding}
              className="cursor-pointer"
            >
              Cancel
            </Button>
            <Button
              onClick={handleConfirm}
              disabled={!selectedUserId || isAdding}
              isLoading={isAdding}
              className="cursor-pointer gap-2"
            >
              <UserPlus size={16} />
              Add User
            </Button>
          </div>
        </div>
      </Dialog.Content>
    </Dialog>
  );
};
