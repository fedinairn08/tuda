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
    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }

    @Override
    public Event getEventById(long id) {
        return eventRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Event not found with id: " + id));
    }

    @Override
    public List<Event> getEventsByUserId(long id) {
        userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found with id: " + id));

        return eventRepository.getEventsByUserId(id);
    }

    @Override
    @Transactional
    public Event updateEvent(EventRequestDTO requestDTO, long id) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Event not found with id: " + id));

        updateEventFromDto(event, requestDTO);

        if (event.getEventStatus() == EventStatus.CANCELLED) {
            requestService.cancelAllEventRequests(id); // Каскадное удаление заявок
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
        if (dto.getPhoto() != null) {
            event.setPhoto(dto.getPhoto());
        }
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