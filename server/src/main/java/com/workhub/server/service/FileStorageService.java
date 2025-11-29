package com.workhub.server.service;

import org.springframework.web.multipart.MultipartFile;

public interface FileStorageService {

    /**
     * Upload file to underlying storage.
     *
     * @param file   multipart file
     * @param folder logical folder/prefix in bucket, e.g. "avatars" or "attachments"
     * @return storage key (path inside the bucket)
     */
    String upload(MultipartFile file, String folder);

    /**
     * Delete file by storage key.
     *
     * @param key object key in bucket
     */
    void delete(String key);
}


