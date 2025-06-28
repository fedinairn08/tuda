package com.tuda.dto.response;

import com.tuda.entity.Event;
import lombok.Data;

@Data
public class GuestResponseDTO {
    private Long id;

    private EventResponseDTO event;

    private String fullName;

    private String mail;

    private boolean status;

    private String keyId;
}
