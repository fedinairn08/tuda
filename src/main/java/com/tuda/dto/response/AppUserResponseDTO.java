package com.tuda.dto.response;

import lombok.Data;

@Data
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
