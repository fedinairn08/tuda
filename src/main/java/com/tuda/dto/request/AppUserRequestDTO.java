package com.tuda.dto.request;

import lombok.Data;

@Data
public class AppUserRequestDTO {
    private String name;

    private String lastName;

    private String patronymic;

    private String login;

    private String password;

    private String phoneNumber;
}
