package com.tuda.dto.request;

import lombok.Data;

@Data
public class JwtRequestDTO {
    private String login;
    private String password;
}
