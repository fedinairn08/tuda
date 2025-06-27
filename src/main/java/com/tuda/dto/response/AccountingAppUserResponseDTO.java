package com.tuda.dto.response;

import lombok.Data;

@Data
public class AccountingAppUserResponseDTO {
    private Long id;

    private EventResponseDTO event;

    private AppUserResponseDTO appUser;

    private boolean status;

    private String userRole;

    private String keyId;
}
