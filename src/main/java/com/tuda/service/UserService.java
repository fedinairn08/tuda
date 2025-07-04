package com.tuda.service;

import com.tuda.data.entity.AppUser;
import com.tuda.data.enums.EventUserStatus;
import com.tuda.dto.request.AppUserRequestDTO;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    AppUser getById(long id);

    AppUser updateUser(AppUserRequestDTO requestDTO, String login);

    EventUserStatus getUserEventStatusByAppUserIdAndEventId(long appUserId, long eventId);
}
