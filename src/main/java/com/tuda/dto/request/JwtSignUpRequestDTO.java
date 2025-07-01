package com.tuda.dto.request;

import com.tuda.data.entity.Organization;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Data
public class JwtSignUpRequestDTO {
    private String login;
    private String password;

    private String name;
    private String lastName;
    private String patronymic;

    private Long organizationId;
    private String phoneNumber;
}
