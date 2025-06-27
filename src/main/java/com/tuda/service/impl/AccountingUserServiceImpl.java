package com.tuda.service.impl;

import com.tuda.entity.AccountingAppUser;
import com.tuda.entity.AppUser;
import com.tuda.entity.Event;
import com.tuda.enums.UserRole;
import com.tuda.exception.NotFoundException;
import com.tuda.repository.AccountingUserRepository;
import com.tuda.repository.EventRepository;
import com.tuda.repository.UserRepository;
import com.tuda.service.AccountingUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountingUserServiceImpl implements AccountingUserService {

    private final UserRepository userRepository;

    private final EventRepository eventRepository;

    private final AccountingUserRepository accountingUserRepository;

    @Override
    public AccountingAppUser createAccountingUserAsParticipant(Long eventId, String login) {
        AppUser user = userRepository.findByLogin(login).orElseThrow(() ->
                new NotFoundException(String.format("User with login: %s -- is not found", login)));

        Event event = eventRepository.findById(eventId).orElseThrow(() ->
                new NotFoundException(String.format("Event with id: %s -- is not found", eventId)));

        AccountingAppUser accounting = AccountingAppUser.builder()
                .appUser(user)
                .event(event)
                .userRole(UserRole.PARTICIPANT)
                .status(false)
                .keyId("")
                .build();

        return accountingUserRepository.save(accounting);
    }

    @Override
    public void refuseToParticipate(Long id) {
        accountingUserRepository.deleteById(id);
    }

    @Override
    public AccountingAppUser createAccountingUserAsVolunteer(Long eventId, String login) {
        AppUser user = userRepository.findByLogin(login).orElseThrow(() ->
                new NotFoundException(String.format("User with login: %s -- is not found", login)));

        Event event = eventRepository.findById(eventId).orElseThrow(() ->
                new NotFoundException(String.format("Event with id: %s -- is not found", eventId)));

        AccountingAppUser accounting = AccountingAppUser.builder()
                .appUser(user)
                .event(event)
                .userRole(UserRole.VOLUNTEER)
                .status(false)
                .keyId("")
                .build();

        return accountingUserRepository.save(accounting);
    }
}
