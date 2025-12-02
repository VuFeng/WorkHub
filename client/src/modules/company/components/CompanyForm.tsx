import { useState, useRef, useEffect } from "react";
import { Upload, X } from "lucide-react";
import type { CompanyRequest, CompanyResponse } from "../../../types";
import { uploadFile, deleteFile } from "../../../api/fileApi";
import { Button, Dialog, Label, Input } from "../../../components/ui";
import toast from "react-hot-toast";

interface CompanyFormProps {
  isOpen: boolean;
  onOpenChange: (open: boolean) => void;
  editingCompany?: CompanyResponse | null;
  onSuccess: () => void;
  onCreate: (payload: CompanyRequest) => Promise<void>;
  onUpdate: (id: string, payload: CompanyRequest) => Promise<void>;
  isCreating: boolean;
  isUpdating: boolean;
}

const MAX_FILE_SIZE = 5 * 1024 * 1024; // 5MB

export const CompanyForm = ({
  isOpen,
  onOpenChange,
  editingCompany,
  onSuccess,
  onCreate,
  onUpdate,
  isCreating,
  isUpdating,
}: CompanyFormProps) => {
  const [formData, setFormData] = useState<Partial<CompanyRequest>>({
    name: editingCompany?.name || "",
    address: editingCompany?.address || "",
    logoUrl: editingCompany?.logoUrl,
  });
  const [selectedFile, setSelectedFile] = useState<File | null>(null);
  const [previewUrl, setPreviewUrl] = useState<string | null>(
    editingCompany?.logoUrl || null
  );
  const [originalLogoUrl, setOriginalLogoUrl] = useState<string | null>(
    editingCompany?.logoUrl || null
  );
  const [isUploading, setIsUploading] = useState(false);
  const fileInputRef = useRef<HTMLInputElement>(null);

  useEffect(() => {
    if (isOpen) {
      setFormData({
        name: editingCompany?.name || "",
        address: editingCompany?.address || "",
        logoUrl: editingCompany?.logoUrl,
      });
      setSelectedFile(null);
      setPreviewUrl(editingCompany?.logoUrl || null);
      setOriginalLogoUrl(editingCompany?.logoUrl || null);
      if (fileInputRef.current) {
        fileInputRef.current.value = "";
      }
    }
  }, [isOpen, editingCompany]);

  const resetForm = () => {
    setFormData({
      name: editingCompany?.name || "",
      address: editingCompany?.address || "",
      logoUrl: editingCompany?.logoUrl,
    });
    setSelectedFile(null);
    setPreviewUrl(editingCompany?.logoUrl || null);
    if (fileInputRef.current) {
      fileInputRef.current.value = "";
    }
  };

  const handleFileChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const file = e.target.files?.[0];
    if (!file) return;

    if (!file.type.startsWith("image/")) {
      toast.error("Please select an image file");
      return;
    }

    if (file.size > MAX_FILE_SIZE) {
      toast.error("File size must be less than 5MB");
      return;
    }

    setSelectedFile(file);
    const reader = new FileReader();
    reader.onloadend = () => {
      setPreviewUrl(reader.result as string);
    };
    reader.readAsDataURL(file);
  };

  const handleRemoveFile = () => {
    setSelectedFile(null);
    setPreviewUrl(null);
    setFormData({ ...formData, logoUrl: undefined });
    if (fileInputRef.current) {
      fileInputRef.current.value = "";
    }
  };

  const handleUploadFile = async (): Promise<string | null> => {
    if (!selectedFile) return null;

    setIsUploading(true);
    try {
      const response = await uploadFile(selectedFile);
      return response.url;
    } catch (error) {
      console.error("Error uploading file:", error);
      toast.error("Error uploading file. Please try again.");
      return null;
    } finally {
      setIsUploading(false);
    }
  };

  const handleSave = async () => {
    let logoUrl: string | undefined = formData.logoUrl;
    let oldLogoUrlToDelete: string | undefined = undefined;

    // If editing and logo was removed (no preview and no new file), delete old file
    if (!previewUrl && editingCompany && originalLogoUrl) {
      logoUrl = undefined;
      oldLogoUrlToDelete = originalLogoUrl;
    }

    // If uploading new file, delete old file if exists
    if (selectedFile) {
      const uploadedUrl = await handleUploadFile();
      if (!uploadedUrl) return;
      logoUrl = uploadedUrl;
      // If there was an original logo, delete it
      if (originalLogoUrl && originalLogoUrl !== uploadedUrl) {
        oldLogoUrlToDelete = originalLogoUrl;
      }
    }

    // Delete old file from S3 if needed
    if (oldLogoUrlToDelete && oldLogoUrlToDelete.startsWith("https://")) {
      try {
        await deleteFile(oldLogoUrlToDelete);
      } catch (error) {
        console.error("Error deleting old file from S3:", error);
        // Continue with save even if delete fails
      }
    }

    const payload: CompanyRequest = {
      name: formData.name || "",
      address: formData.address || "",
      logoUrl,
    };

    try {
      if (editingCompany) {
        await onUpdate(editingCompany.id, payload);
      } else {
        await onCreate(payload);
      }
      resetForm();
      onSuccess();
      onOpenChange(false);
    } catch (error) {
      // Error handling is done in mutations
      console.error("Error saving company:", error);
      toast.error("Unable to save company. Please try again.");
    }
  };

  const handleClose = () => {
    resetForm();
    onOpenChange(false);
  };

  if (!isOpen) return null;

  return (
    <Dialog open={isOpen} onOpenChange={onOpenChange}>
      <Dialog.Content className="max-w-2xl">
        <Dialog.Header>
          <Dialog.Title>
            {editingCompany ? "Edit Company" : "Add New Company"}
          </Dialog.Title>
        </Dialog.Header>
        <div className="space-y-4">
          <div className="space-y-2">
            <Label htmlFor="name">Company Name *</Label>
            <Input
              id="name"
              value={formData.name || ""}
              onChange={(e) =>
                setFormData({ ...formData, name: e.target.value })
              }
              placeholder="Enter company name"
              required
            />
          </div>

          <div className="space-y-2">
            <Label htmlFor="address">Address *</Label>
            <Input
              id="address"
              value={formData.address || ""}
              onChange={(e) =>
                setFormData({ ...formData, address: e.target.value })
              }
              placeholder="Enter address"
              required
            />
          </div>

          <div className="space-y-2">
            <Label htmlFor="logo">Company Logo</Label>
            <div className="space-y-3">
              <div className="flex items-center gap-3">
                <input
                  ref={fileInputRef}
                  type="file"
                  id="logo"
                  accept="image/*"
                  onChange={handleFileChange}
                  className="hidden"
                />
                <Button
                  type="button"
                  variant="outline"
                  onClick={() => fileInputRef.current?.click()}
                  className="gap-2 cursor-pointer"
                  disabled={isUploading}
                >
                  <Upload size={16} />
                  {selectedFile ? "Change Logo" : "Upload Logo"}
                </Button>
                {previewUrl && (
                  <Button
                    type="button"
                    variant="outline"
                    size="sm"
                    onClick={handleRemoveFile}
                    className="gap-2 text-red-600 hover:text-red-700 cursor-pointer"
                  >
                    <X size={16} />
                    Remove
                  </Button>
                )}
              </div>
              {previewUrl && (
                <div className="mt-2">
                  <img
                    src={previewUrl}
                    alt="Logo preview"
                    className="h-32 w-32 object-cover rounded-lg border border-gray-200"
                  />
                  {selectedFile && (
                    <p className="text-sm text-muted-foreground mt-2">
                      {selectedFile.name} (
                      {(selectedFile.size / 1024).toFixed(2)} KB)
                    </p>
                  )}
                </div>
              )}
              {!previewUrl && !selectedFile && (
                <p className="text-sm text-muted-foreground">
                  Chọn file ảnh để upload logo công ty (JPG, PNG, max 5MB)
                </p>
              )}
            </div>
          </div>

          <div className="flex gap-3 justify-end pt-4">
            <Button
              variant="outline"
              className="cursor-pointer"
              onClick={handleClose}
            >
              Cancel
            </Button>
            <Button
              onClick={handleSave}
              isLoading={
                isUploading || (editingCompany ? isUpdating : isCreating)
              }
              disabled={isUploading}
              className="cursor-pointer"
            >
              Save Company
            </Button>
          </div>
        </div>
      </Dialog.Content>
    </Dialog>
  );
};
