package com.tuda.dto.request;

import lombok.Data;

@Data
public class JwtSignInRequestDTO {
    private String login;
    private String password;
}
