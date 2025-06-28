package com.tuda.service.impl;

import com.tuda.dto.request.RequestRequestDTO;
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
import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class RequestServiceImpl implements RequestService {
    private final RequestRepository requestRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final AccountingUserService accountingUserService;

    @Override
    public List<Request> getAllRequests() {
        return requestRepository.findAll();
    }

    @Override
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
            //accountingUserService.refuse(eventId, login);
        }

        requestRepository.delete(request);
    }

    @Override
    @Transactional
    public Request rejectRequest(Long requestId) {
        Request request = requestRepository.findById(requestId)
                .orElseThrow(() -> new NotFoundException(
                        String.format("Request not found for id %d",requestId)));

        request.setStatus(false);
        return requestRepository.save(request);
    }

    @Transactional
    public void cancelAllEventRequests(long eventId) {
        // 1. Удаляем все заявки
//        List<Request> requests = requestRepository.findAllByEventId(eventId);
//
//        // 2. Отменяем связанные аккаунтинги (если нужно)
//        requests.stream()
//                .filter(Request::isActive)
//                .forEach(request -> {
//                    accountingUserService.refuse(
//                            request.getEvent().getId(),
//                            request.getAppUser().getLogin()
//                    );
//                });
//
//        // 3. Массовое удаление
//        requestRepository.deleteAllByEventId(eventId);
    }
}
