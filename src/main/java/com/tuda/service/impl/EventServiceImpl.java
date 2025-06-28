package com.tuda.service.impl;

import com.tuda.dto.request.EventRequestDTO;
import com.tuda.entity.Event;
import com.tuda.enums.EventStatus;
import com.tuda.exception.NotFoundException;
import com.tuda.repository.EventRepository;
import com.tuda.repository.RequestRepository;
import com.tuda.repository.UserRepository;
import com.tuda.service.EventService;
import com.tuda.service.RequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.function.Consumer;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {
    private final RequestService requestService;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Event getEventById(long id) {
        return eventRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Event not found with id: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Event> getEventsByUserId(long userId) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException(String.format("User not found for id %d", userId));
        }

        return eventRepository.getEventsByUserId(userId);
    }

    @Override
    @Transactional
    public Event updateEvent(EventRequestDTO requestDTO, long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Event not found with id: " + eventId));

        updateEventFromDto(event, requestDTO);

        if (event.getEventStatus().equals(EventStatus.CANCELLED)) {
            requestService.cancelAllEventRequests(eventId);
        }

        return eventRepository.save(event);
    }

    @Override
    @Transactional
    public Event addEvent(EventRequestDTO requestDTO) {
        Event event = new Event();

        updateEventFromDto(event, requestDTO);
        event.setEventStatus(EventStatus.WILL);

        return eventRepository.save(event);
    }

    private void updateEventFromDto(Event event, EventRequestDTO dto) {
        updateIfNotBlank(event::setTitle, dto.getTitle());
        updateIfNotBlank(event::setCity, dto.getCity());
        updateIfNotBlank(event::setDescription, dto.getDescription());

        updateIfPositiveOrZero(event::setParticipantsNumber, dto.getParticipantsNumber());
        updateIfPositiveOrZero(event::setVolunteersNumber, dto.getVolunteersNumber());

        if (dto.getDate() != null) {
            event.setDate(dto.getDate());
        }
        if (dto.getEventStatus() != null) {
            event.setEventStatus(dto.getEventStatus());
        }
//        if (dto.getOrganization() != null) {
//            event.setOrganization(organizationRepository.findById(dto.getOrganization())
//                    .orElseThrow(() -> new NotFoundException(String.format("Organization not found for id %d", dto.getOrganization()))));
//        }
//        if (dto.getPhoto() != null) {
//            event.setPhoto(photoRepository.findById(dto.getPhoto())
//                    .orElseThrow(() -> new NotFoundException(String.format("Photo not found for id %d", dto.getPhoto()))));
//        }
    }

    private void updateIfNotBlank(Consumer<String> setter, String value) {
        if (value != null && !value.isBlank()) {
            setter.accept(value);
        }
    }

    private void updateIfPositiveOrZero(Consumer<Integer> setter, Integer value) {
        if (value != null && value >= 0) {
            setter.accept(value);
        }
    }


}