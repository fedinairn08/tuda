package com.tuda.controller;

import com.tuda.dto.ApiResponse;
import com.tuda.dto.response.PhotoResponseDTO;
import com.tuda.s3storage.S3File;
import com.tuda.service.ImageService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/image")
@AllArgsConstructor
public class ImageController {

    private final ImageService imageService;

    private final ModelMapper modelMapper;

    private static final Class<PhotoResponseDTO> PHOTO_RESPONSE_DTO_CLASS = PhotoResponseDTO.class;

    @SecurityRequirement(name = "JWT")
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<PhotoResponseDTO>> uploadImage(
            @RequestPart("file") MultipartFile file) {
        S3File s3File = imageService.upload(file);
        return ResponseEntity.ok(new ApiResponse<>(modelMapper.map(s3File, PHOTO_RESPONSE_DTO_CLASS)));
    }

    @SecurityRequirement(name = "JWT")
    @PostMapping(value = "/delete")
    public void deleteImage(@RequestParam String filename) {
        imageService.delete(filename);
    }

//    @GetMapping(value = "/get/{uuid}", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
//    public ResponseEntity<String> getImageByUuid(@PathVariable UUID uuid) {
//        S3File image = imageService.getMediaFileByUuid(uuid);
//
//        String base64Image = Base64.encodeBase64String(image.getContent());
//
//        return ResponseEntity
//                .ok()
//                .contentType(MediaType.TEXT_PLAIN)
//                .body(base64Image);
//    }
}
