package com.tuda.repository;

import com.tuda.entity.Photo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface PhotoRepository extends JpaRepository<Photo, Long> {
    Optional<Photo> findByFilename(String filename);

    Optional<Photo> findByUploadId(UUID uploadId);
}
