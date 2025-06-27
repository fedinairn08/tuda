package com.tuda.service.impl;

import com.tuda.dto.request.AppUserRequestDTO;
import com.tuda.entity.AppUser;
import com.tuda.exception.NotFoundException;
import com.tuda.repository.UserRepository;
import com.tuda.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public AppUser getById(long id) {
        return userRepository.findById(id)
                .orElseThrow(() ->
                        new NotFoundException(String.format("User with id: %s -- is not found", id))
        );
    }

    @Override
    public AppUser updateUser(AppUserRequestDTO requestDTO, String login) {
        AppUser user = userRepository.findByLogin(login).orElseThrow(() ->
                new NotFoundException(String.format("User with login: %s -- is not found", login)));

        user.setName(requestDTO.getName())
                .setLastName(requestDTO.getLastName())
                .setPatronymic(requestDTO.getPatronymic())
                .setLogin(requestDTO.getLogin())
                .setPassword(requestDTO.getPassword())
                .setPhoneNumber(requestDTO.getPhoneNumber());

        return userRepository.save(user);
    }

}
