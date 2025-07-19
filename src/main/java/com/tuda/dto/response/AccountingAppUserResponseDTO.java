package com.tuda.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AccountingAppUserResponseDTO {
    private Long id;

    private EventResponseDTO event;

    private AppUserResponseDTO appUser;

    private boolean status;

    private String userRole;

    private String keyId;
}
