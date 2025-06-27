package com.tuda.dto.response;

import lombok.Data;

import java.util.UUID;

@Data
public class PhotoResponseDTO {
    private Long id;

    private UUID uploadId;

    private String filename;
}
