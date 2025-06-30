package com.tuda.service.impl;

import com.tuda.data.entity.Photo;
import com.tuda.exception.NotFoundException;
import com.tuda.repository.PhotoRepository;
import com.tuda.s3storage.S3File;
import com.tuda.service.ImageService;
import com.tuda.service.file.FileService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RequiredArgsConstructor
@Service
public class ImageServiceImpl implements ImageService {

    private final FileService fileService;

    private final PhotoRepository photoRepository;

    @Override
    @SneakyThrows
    public S3File upload(MultipartFile file) {
        S3File s3File = new S3File(file.getOriginalFilename(), file.getBytes());
        fileService.save(s3File);
        return s3File;
    }

    @Override
    public void delete(String filename) {
        fileService.delete(filename);
    }

    @Override
    public S3File getMediaFileByUuid(UUID uuid) {
        Photo photo = photoRepository.findByUploadId(uuid).orElseThrow(() ->
                new NotFoundException(String.format("Photo with uuid: %s -- is not found", uuid)));
        return fileService.get(photo.getFilename());
    }
}
