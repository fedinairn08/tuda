package com.tuda.dto.request;

import lombok.Data;

@Data
public class JwtSignUpRequestDTO {
    private String login;
    private String password;

    private String name;
    private String lastName;
    private String patronymic;

    private String phoneNumber;

    private String organizationName;
    private String organizationPhoneNumber;

}
