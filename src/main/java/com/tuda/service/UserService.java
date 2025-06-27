package com.tuda.service;

import com.tuda.dto.request.AppUserRequestDTO;
import com.tuda.entity.AppUser;

public interface UserService {
    AppUser getById(long id);

    AppUser updateUser(AppUserRequestDTO requestDTO, String login);
}
