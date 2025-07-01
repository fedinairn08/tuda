package com.tuda.service;

import com.tuda.data.entity.AppUser;
import com.tuda.dto.request.AppUserRequestDTO;
import com.tuda.dto.request.JwtSignUpRequestDTO;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Optional;

public interface UserService extends UserDetailsService {
    AppUser getById(long id);

    AppUser updateUser(AppUserRequestDTO requestDTO, String login);

}
