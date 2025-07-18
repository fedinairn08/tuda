package com.tuda.unit.service;

import com.tuda.data.entity.Event;
import com.tuda.exception.NotFoundException;
import com.tuda.repository.EventRepository;
import com.tuda.service.impl.EventServiceImpl;
import com.tuda.unit.preparer.EventPreparer;
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
        List<Event> expectedEvents = List.of(EventPreparer.getTestEvent());
        when(eventRepository.findAll()).thenReturn(expectedEvents);

        List<Event> actualEvents = eventService.getAllEvents();

        verify(eventRepository).findAll();
        assertThat(actualEvents)
                .hasSize(expectedEvents.size())
                .containsExactlyInAnyOrderElementsOf(expectedEvents);
    }

    @Test
    void whenGetEventById_thenReturnEvent() {
        Event expectedEvent = EventPreparer.getTestEvent();

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
}