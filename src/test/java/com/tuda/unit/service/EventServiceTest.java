package com.tuda.unit.service;

import com.tuda.data.entity.Event;
import com.tuda.exception.NotFoundException;
import com.tuda.repository.EventRepository;
import com.tuda.repository.UserRepository;
import com.tuda.service.impl.EventServiceImpl;
import com.tuda.unit.preparer.EntityPreparer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class EventServiceTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private EventRepository eventRepository;

    @InjectMocks
    private EventServiceImpl eventService;

    @Test
    void whenNotGetAllEvents_thenReturnEmptyList() {
        List<Event> expectedEvents = new ArrayList<>();

        when(eventRepository.findAll()).thenReturn(expectedEvents);

        List<Event> actualEvents = eventService.getAllEvents();

        assertEquals(expectedEvents.size(), actualEvents.size());
        verify(eventRepository).findAll();
    }

    @Test
    void whenGetAllEvents_thenReturnEventList() {
        List<Event> expectedEvents = List.of(EntityPreparer.getTestEvent());

        when(eventRepository.findAll()).thenReturn(expectedEvents);

        List<Event> actualEvents = eventService.getAllEvents();

        verify(eventRepository).findAll();
        assertThat(actualEvents)
                .hasSize(expectedEvents.size())
                .containsExactlyInAnyOrderElementsOf(expectedEvents);
    }

    @Test
    void whenGetEventById_thenReturnEvent() {
        Event expectedEvent = EntityPreparer.getTestEvent();

        when(eventRepository.findById(expectedEvent.getId())).thenReturn(Optional.of(expectedEvent));

        Event actualEvent = eventService.getEventById(expectedEvent.getId());

        verify(eventRepository).findById(expectedEvent.getId());
        assertEquals(expectedEvent, actualEvent);
    }

    @Test
    void whenGetNotExistedEventId_thenReturnNotFound() {
        Long id = 999L;

        when(eventRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> eventService.getEventById(id));
        verify(eventRepository).findById(id);
    }

    @Test
    void whenGetExistedUserId_thenGetEventList() {
        Long expectedUserId = 1L;
        List<Event> expectedEvents = List.of(EntityPreparer.getTestEvent());

        when(userRepository.existsById(expectedUserId)).thenReturn(true);
        when(eventRepository.getEventsByUserId(expectedUserId)).thenReturn(expectedEvents);

        List<Event> actualEvents = eventService.getEventsByUserId(expectedUserId);

        verify(userRepository).existsById(expectedUserId);
        verify(eventRepository).getEventsByUserId(expectedUserId);
        assertThat(actualEvents)
                .hasSize(expectedEvents.size())
                .containsExactlyInAnyOrderElementsOf(expectedEvents);
    }

    @Test
    void whenGetExistedUserId_thenReturnEmptyList() {
        Long expectedUserId = 1L;
        List<Event> expectedEvents = new ArrayList<>();

        when(userRepository.existsById(expectedUserId)).thenReturn(true);
        when(eventRepository.getEventsByUserId(expectedUserId)).thenReturn(expectedEvents);

        List<Event> actualEvents = eventService.getEventsByUserId(expectedUserId);

        verify(userRepository).existsById(expectedUserId);
        verify(eventRepository).getEventsByUserId(expectedUserId);
        assertEquals(expectedEvents.size(), actualEvents.size());
    }

    @Test
    void whenGetNotExistedUserId_thenReturnNotFound() {
        long notExistedUserId = 999L;

        when(userRepository.existsById(notExistedUserId)).
                thenReturn(false);

        assertThrows(NotFoundException.class, () -> eventService.getEventsByUserId(notExistedUserId));
        verify(userRepository).existsById(notExistedUserId);
    }
}