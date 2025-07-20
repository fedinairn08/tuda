package com.tuda.dto.request;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Data
@NoArgsConstructor
public class OrganizationRequestDTO {
    private String name;
    private String phoneNumber;
}
