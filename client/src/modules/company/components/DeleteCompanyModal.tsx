import { AlertTriangle } from "lucide-react";
import { Button, Dialog } from "../../../components/ui";

interface DeleteCompanyModalProps {
  isOpen: boolean;
  onOpenChange: (open: boolean) => void;
  companyName: string;
  onConfirm: () => void;
  isDeleting: boolean;
}

export const DeleteCompanyModal = ({
  isOpen,
  onOpenChange,
  companyName,
  onConfirm,
  isDeleting,
}: DeleteCompanyModalProps) => {
  const handleConfirm = () => {
    onConfirm();
  };

  return (
    <Dialog open={isOpen} onOpenChange={onOpenChange}>
      <Dialog.Content
        size="md"
        className="flex flex-col items-center justify-center"
      >
        <Dialog.Header>
          <Dialog.Title>Delete Company</Dialog.Title>
        </Dialog.Header>
        <div className="space-y-4">
          <div className="flex items-center gap-4">
            <div className="flex shrink-0">
              <div className="flex h-10 w-10 items-center justify-center rounded-full bg-red-100">
                <AlertTriangle className="h-5 w-5 text-red-600" />
              </div>
            </div>
            <div className="flex-1">
              <h3 className="text-sm font-medium text-gray-900">
                Are you sure you want to delete this company?
              </h3>
              <div className="mt-2">
                <p className="text-sm text-gray-500">
                  This will permanently delete <strong>{companyName}</strong>.
                  This action cannot be undone.
                </p>
              </div>
            </div>
          </div>

          <div className="flex gap-3 justify-center pt-4">
            <Button
              variant="outline"
              onClick={() => onOpenChange(false)}
              disabled={isDeleting}
              className="cursor-pointer"
            >
              Cancel
            </Button>
            <Button
              variant="danger"
              onClick={handleConfirm}
              isLoading={isDeleting}
              disabled={isDeleting}
              className="cursor-pointer"
            >
              Delete
            </Button>
          </div>
        </div>
      </Dialog.Content>
    </Dialog>
  );
};
