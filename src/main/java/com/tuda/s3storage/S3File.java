package com.tuda.s3storage;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class S3File {
    private String filename;
    private UUID uuid;
    private byte[] content;

    public S3File(String filename, byte[] content) {
        this.filename = filename;
        this.content = content;
        this.uuid = UUID.randomUUID();
    }

    public S3File(String filename) {
        this.filename = filename;
    }

    public S3File(byte[] content) {
        this.content = content;
    }
}
