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
import org.modelmapper.ModelMapper;
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
    private final ModelMapper modelMapper;

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

        if (requestDTO.getEventStatus().equals(EventStatus.CANCELLED.toString())) {
            requestService.cancelAllEventRequests(eventId);
        }

        modelMapper.map(requestDTO,event);


        return eventRepository.save(event);
    }

    @Override
    @Transactional
    public Event addEvent(EventRequestDTO requestDTO) {
        Event event = new Event();
        modelMapper.map(requestDTO,event);

        event.setEventStatus(EventStatus.WILL);

        return eventRepository.save(event);
    }


}