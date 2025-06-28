package com.tuda.dto.response;

import lombok.Data;

import java.util.UUID;

@Data
public class PhotoResponseDTO {
    private UUID uuid;

    private String filename;
}
