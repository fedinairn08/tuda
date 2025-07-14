package com.tuda.service;

import com.tuda.data.entity.Event;
import com.tuda.data.enums.EventStatus;
import com.tuda.data.enums.UserRole;
import com.tuda.dto.request.EventRequestDTO;
import com.tuda.dto.response.EventParticipantResponseDTO;

import java.util.List;
import java.util.Optional;

public interface EventService {
    List<Event> getAllEvents();
    Event getEventById(long id);
    List<Event> getEventsByUserId(long id);
    Event updateEvent(EventRequestDTO requestDTO, long id);
    Event addEvent(EventRequestDTO requestDTO);
    List<EventParticipantResponseDTO> getAllParticipantsByEventId(long id);
    List<Event> getEventsByStatusAndAppUserIdForUser(EventStatus status, long appUserId);
    List<Event> getOrganizationEventsByOrganizerId(long organizerId);
    Optional<?> markPresence(String key);
    List<Event> getEventsByNeededRoleForUser(UserRole role);
    List<Event> getEventsByStatusAndAppUserIdForOrganizer(EventStatus status, long appUserId);
    List<Event> getEventsByNeededRoleForOrganizer(UserRole role, long appUserId);
    Long getUserCountWithCertainRoleOnEvent(UserRole role, long eventId);
}
