import apiClient from "./client";

export interface FileUploadResponse {
  key: string;
  url: string;
  size: number;
  contentType: string;
}

export const uploadFile = async (file: File): Promise<FileUploadResponse> => {
  const formData = new FormData();
  formData.append("file", file);

  const { data } = await apiClient.post<FileUploadResponse>(
    "/files",
    formData,
    {
      headers: {
        "Content-Type": "multipart/form-data",
      },
    }
  );

  if (!data) {
    throw new Error("Failed to upload file: No data returned");
  }

  return data;
};
