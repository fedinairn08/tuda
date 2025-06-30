package com.tuda.service;

import com.tuda.data.entity.Event;
import com.tuda.dto.request.EventRequestDTO;

import java.util.List;

public interface EventService {
    List<Event> getAllEvents();
    Event getEventById(long id);
    List<Event> getEventsByUserId(long id);
    Event updateEvent(EventRequestDTO requestDTO, long id);
    Event addEvent(EventRequestDTO requestDTO);
}
