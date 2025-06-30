package com.tuda.service.impl;

import com.tuda.data.entity.AccountingAppUser;
import com.tuda.data.entity.AppUser;
import com.tuda.data.entity.Event;
import com.tuda.data.entity.Request;
import com.tuda.data.enums.UserRole;
import com.tuda.exception.NotFoundException;
import com.tuda.repository.AccountingUserRepository;
import com.tuda.repository.EventRepository;
import com.tuda.repository.RequestRepository;
import com.tuda.repository.UserRepository;
import com.tuda.service.AccountingUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AccountingUserServiceImpl implements AccountingUserService {

    private final UserRepository userRepository;

    private final EventRepository eventRepository;

    private final AccountingUserRepository accountingUserRepository;

    private final RequestRepository requestRepository;

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
    @Transactional
    public void refuse(Long eventId, String login) {
        AppUser user = userRepository.findByLogin(login).orElseThrow(() ->
                new NotFoundException(String.format("User with login: %s -- is not found", login)));

        accountingUserRepository.deleteByAppUserAndEvent_Id(user, eventId);
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

        AccountingAppUser savedVolunteer = accountingUserRepository.save(accounting);

        Request request = requestRepository.findByEventIdAndAppUserLogin(eventId, login).orElseThrow(() ->
                new NotFoundException("Request -- is not found"));
        request.setStatus(true);
        requestRepository.save(request);

        return savedVolunteer;
    }

    @Override
    public AccountingAppUser markPresence(Long eventId, String login) {
        AppUser user = userRepository.findByLogin(login).orElseThrow(() ->
                new NotFoundException(String.format("User with login: %s -- is not found", login)));

        AccountingAppUser accountingAppUser = accountingUserRepository
                .findByAppUserAndEvent_Id(user, eventId).orElseThrow(() ->
                        new NotFoundException(String.format(
                                "Accounting app user with eventId: %s -- is not found", eventId)));

        accountingAppUser.setStatus(true);

        return accountingUserRepository.save(accountingAppUser);
    }
}
