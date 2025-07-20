package com.tuda.dto.request;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@SuperBuilder
public class GuestRequestDTO {
    private Long event;

    private String fullName;

    private String mail;

    private Boolean status;

    private String keyId;
}
