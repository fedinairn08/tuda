package com.tuda.service.file;

import com.tuda.s3storage.S3Client;
import com.tuda.s3storage.S3File;
import com.tuda.s3storage.config.S3Properties;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class S3FileService implements FileService {

    private final S3Client minioClient;

    private final S3Properties s3Properties;

    @Override
    @SneakyThrows
    public void save(S3File file) {
        minioClient.put(file, s3Properties.getBucket());
    }

    @Override
    public S3File get(String filename) {
        return new S3File(filename, minioClient.get(filename, s3Properties.getBucket()));
    }

    @Override
    public void delete(String filename) {
        minioClient.delete(filename, s3Properties.getBucket());
    }
}
