package com.tuda.service.impl;

import com.tuda.data.entity.AccountingAppUser;
import com.tuda.data.entity.AppUser;
import com.tuda.data.entity.Request;
import com.tuda.data.enums.EventUserStatus;
import com.tuda.data.enums.UserRole;
import com.tuda.dto.request.AppUserRequestDTO;
import com.tuda.exception.NotFoundException;
import com.tuda.repository.AccountingUserRepository;
import com.tuda.repository.RequestRepository;
import com.tuda.repository.UserRepository;
import com.tuda.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final AccountingUserRepository accountingUserRepository;
    private final RequestRepository requestRepository;
    private final PasswordEncoder passwordEncoder;

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
                .setPassword(passwordEncoder.encode(requestDTO.getPassword()))
                .setPhoneNumber(requestDTO.getPhoneNumber());

        return userRepository.save(user);
    }

    @Override
    @Transactional(readOnly = true)
    public EventUserStatus getUserEventStatusByAppUserIdAndEventId(long appUserId, long eventId) {
        Optional<AccountingAppUser> accountingAppUserOptional =
                accountingUserRepository.findByAppUserIdAndEventId(appUserId, eventId);

        if (accountingAppUserOptional.isPresent() &&
                accountingAppUserOptional.get().getUserRole() == UserRole.VOLUNTEER) {
            return  EventUserStatus.VOLUNTEER;
        } else if (accountingAppUserOptional.isPresent() &&
                accountingAppUserOptional.get().getUserRole() == UserRole.PARTICIPANT) {
            return EventUserStatus.PARTICIPANT;
        }

        Optional<Request> requestOptional =
                requestRepository.findByAppUserIdAndEventId(appUserId, eventId);
        if (requestOptional.isPresent()) {
            return EventUserStatus.USER_WITH_REQUEST;
        } else {
            return EventUserStatus.USER;
        }
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) {
        AppUser appUser = getByLogin(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return User.builder()
                .username(appUser.getLogin())
                .password(appUser.getPassword())
                .authorities(getAuthorities(appUser))
                .build();
    }

    private Optional<AppUser> getByLogin(String login) {
        return userRepository.findByLogin(login);
    }

    private Collection<? extends GrantedAuthority> getAuthorities(AppUser user) {
        String role = user.getOrganization() != null ? "ROLE_ORGANIZER" : "ROLE_USER";
        return Collections.singletonList(new SimpleGrantedAuthority(role));
    }
}
