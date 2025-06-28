package com.tuda.service.impl;

import com.tuda.entity.AppUser;
import com.tuda.entity.Event;
import com.tuda.entity.Request;
import com.tuda.exception.NotFoundException;
import com.tuda.repository.EventRepository;
import com.tuda.repository.RequestRepository;
import com.tuda.repository.UserRepository;
import com.tuda.service.AccountingUserService;
import com.tuda.service.RequestService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@AllArgsConstructor
public class RequestServiceImpl implements RequestService {
    private final RequestRepository requestRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final AccountingUserService accountingUserService;

    @Override
    @Transactional(readOnly = true)
    public List<Request> getAllEventRequests(Long eventId) {
        if (!eventRepository.existsById(eventId)) {
            throw new NotFoundException(String.format("Event not found for id %d", eventId));
        }

        return requestRepository.findAllByEventId(eventId);
    }

    @Override
    @Transactional
    public Request addRequest(Long eventId, String login) {
        AppUser user = userRepository.findByLogin(login).orElseThrow(() ->
                new NotFoundException(String.format("User with login: %s -- is not found", login)));

        Event event = eventRepository.findById(eventId).orElseThrow(() ->
                new NotFoundException(String.format("Event with id: %s -- is not found", eventId)));

        Request request = Request.builder()
                .appUser(user)
                .event(event)
                .status(false)
                .date(LocalDate.now())
                .build();

        return requestRepository.save(request);
    }

    @Override
    @Transactional
    public void deleteRequest(Long eventId, String login) {
        Request request = requestRepository.findByEventIdAndAppUserLogin(eventId, login)
                .orElseThrow(() -> new NotFoundException(
                        String.format("Request not found for event %d and user %s", eventId, login)));

        if (request.isStatus()) {
            accountingUserService.refuse(eventId, login);
        }

        requestRepository.delete(request);
    }

    @Override
    @Transactional
    public Request rejectRequest(Long eventId, String login) {
        Request request = requestRepository.findByEventIdAndAppUserLogin(eventId, login)
                .orElseThrow(() -> new NotFoundException(
                        String.format("Request not found for event %d and user %s", eventId, login)));

        request.setStatus(false);
        return requestRepository.save(request);
    }

    @Transactional
    public void cancelAllEventRequests(long eventId) {
        if (!eventRepository.existsById(eventId)) {
            throw new NotFoundException(String.format("Event not found for id %d", eventId));
        }

        List<Request> activeRequests = requestRepository.findByEventIdAndStatusTrue(eventId);

        activeRequests.forEach(request ->
                accountingUserService.refuse(eventId, request.getAppUser().getLogin())
        );

        requestRepository.deleteAllByEventId(eventId);
    }
}
