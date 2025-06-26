package com.tuda.service;

import com.tuda.entity.Event;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Optional;

public interface EventService {
    List<Event> getAllEvents();
    Event getEventById(Long id);
    List<Event> getEventsByUserId(Long id);
    Event updateEvent(Event event);
    Event addEvent(Event event);
}
