package com.tuda.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GuestResponseDTO {
    private Long id;

    private EventResponseDTO event;

    private String fullName;

    private String mail;

    private boolean status;

    private String keyId;
}
