package com.tuda.service.file;

import com.tuda.s3storage.S3File;

public interface FileService {
    void save(S3File file);

    S3File get(String filename);

    void delete(String filename);
}
