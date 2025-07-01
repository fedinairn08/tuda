package com.tuda.dto.request;

import lombok.Data;

@Data
public class JwtLoginRequestDTO {
    private String login;
    private String password;
}
