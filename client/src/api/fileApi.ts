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

export const deleteFile = async (url: string): Promise<void> => {
  try {
    const urlObj = new URL(url);
    // Get pathname and remove leading slash
    let path = urlObj.pathname;
    if (path.startsWith("/")) {
      path = path.substring(1);
    }

    if (!path) {
      throw new Error("Invalid S3 URL: cannot extract key");
    }

    // Extract filename (part after last "/")
    const lastSlashIndex = path.lastIndexOf("/");
    const fileName =
      lastSlashIndex >= 0 ? path.substring(lastSlashIndex + 1) : path;

    await apiClient.delete(`/files/${encodeURIComponent(fileName)}`);
    console.log("File delete request sent successfully");
  } catch (error) {
    console.error("Error deleting file:", error);
    throw new Error("Failed to delete file from S3");
  }
};
