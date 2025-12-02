package com.workhub.server.controller;

import com.workhub.server.dto.response.ApiResponse;
import com.workhub.server.dto.response.FileUploadResponse;
import com.workhub.server.security.annotation.RequireAnyRole;
import com.workhub.server.constant.UserRole;
import com.workhub.server.service.FileStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import lombok.extern.slf4j.Slf4j;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

@Slf4j
@RestController
@RequestMapping("/api/files")
@RequiredArgsConstructor
@Validated
public class FileController {

    private final FileStorageService fileStorageService;

    @Value("${aws.s3.bucket}")
    private String bucketName;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @RequireAnyRole({UserRole.ADMIN, UserRole.MANAGER, UserRole.STAFF})
    public ResponseEntity<ApiResponse<FileUploadResponse>> uploadFile(
            @RequestPart("file") MultipartFile file
    ) {
        String fullKey = fileStorageService.upload(file, "uploads");
        String url = buildFileUrl(fullKey);
        
        // Extract filename from key (remove "uploads/" prefix)
        String fileName = extractFileName(fullKey);

        FileUploadResponse payload = FileUploadResponse.builder()
                .key(fileName)
                .url(url)
                .size(file.getSize())
                .contentType(file.getContentType())
                .build();

        ApiResponse<FileUploadResponse> response =
                ApiResponse.success("File uploaded successfully", payload);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{key}")
    @RequireAnyRole({UserRole.ADMIN, UserRole.MANAGER, UserRole.STAFF})
    public ResponseEntity<ApiResponse<Void>> deleteFile(@PathVariable String key) {
        try {
            // URL decode the key in case it was encoded
            String decodedKey = URLDecoder.decode(key, StandardCharsets.UTF_8);
            
            // If key doesn't contain "/", assume it's just filename and add "uploads/" prefix
            String fullKey = decodedKey.contains("/") ? decodedKey : "uploads/" + decodedKey;
            
            fileStorageService.delete(fullKey);
            
            ApiResponse<Void> response = ApiResponse.success("File deleted successfully", null);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error deleting file from S3 with key: {}", key, e);
            throw new RuntimeException("Failed to delete file from S3", e);
        }
    }

    private String buildFileUrl(String key) {
        return String.format("https://%s.s3.amazonaws.com/%s", bucketName, key);
    }

    private String extractFileName(String fullKey) {
        // Extract filename from key like "uploads/uuid.ext" -> "uuid.ext"
        if (fullKey == null || fullKey.isEmpty()) {
            return fullKey;
        }
        int lastSlashIndex = fullKey.lastIndexOf("/");
        if (lastSlashIndex >= 0 && lastSlashIndex < fullKey.length() - 1) {
            return fullKey.substring(lastSlashIndex + 1);
        }
        return fullKey;
    }
}


