package com.tuda.service.impl;

import com.tuda.entity.Event;
import com.tuda.exception.BadRequestException;
import com.tuda.exception.NotFoundException;
import com.tuda.repository.EventRepository;
import com.tuda.service.EventService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;

    @Override
    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }

    @Override
    public Event getEventById(Long id) {
        if (id == null) {
            throw new BadRequestException("Event ID cannot be null");
        }
        return eventRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Event not found with id: " + id));
    }

    @Override
    public List<Event> getEventsByUserId(Long id) {
        if (id == null) {
            throw new BadRequestException("User ID cannot be null");
        }
        List<Event> events = eventRepository.getEventsByUserId(id);
        if (events == null) {
            throw new BadRequestException("Events list cannot be null");
        }
        return events;
    }

    @Override
    public Event updateEvent(Event event) {
        if (event == null || event.getId() == null) {
            throw new BadRequestException("Event cannot be null");
        }

        return eventRepository.save(event);
    }

    @Override
    public Event addEvent(Event event) {
        return eventRepository.save(event);
    }
}