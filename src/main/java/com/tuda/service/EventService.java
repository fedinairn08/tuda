package com.tuda.service;

import com.tuda.dto.request.EventRequestDTO;
import com.tuda.entity.Event;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Optional;

public interface EventService {
    List<Event> getAllEvents();
    Event getEventById(long id);
    List<Event> getEventsByUserId(long id);
    Event updateEvent(EventRequestDTO requestDTO, long id);
    Event addEvent(EventRequestDTO requestDTO);
}
