package com.tuda.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AppUserResponseDTO {
    private Long id;

    private String name;

    private String lastName;

    private String patronymic;

    private String login;

    private String password;

    private OrganizationResponseDTO organization;

    private String phoneNumber;
}
