package com.tuda.dto.request;

import com.tuda.entity.Event;
import lombok.Data;

@Data
public class GuestRequestDTO {
    private Long event;

    private String fullName;

    private String mail;

    private Boolean status;

    private String keyId;
}
