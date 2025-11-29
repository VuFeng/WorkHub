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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

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
        String key = fileStorageService.upload(file, "uploads");
        String url = buildFileUrl(key);

        FileUploadResponse payload = FileUploadResponse.builder()
                .key(key)
                .url(url)
                .size(file.getSize())
                .contentType(file.getContentType())
                .build();

        ApiResponse<FileUploadResponse> response =
                ApiResponse.success("File uploaded successfully", payload);

        return ResponseEntity.ok(response);
    }

    private String buildFileUrl(String key) {
        return String.format("https://%s.s3.amazonaws.com/%s", bucketName, key);
    }
}


