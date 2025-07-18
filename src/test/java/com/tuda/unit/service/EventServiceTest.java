package com.tuda.unit.service;

import com.tuda.data.entity.Event;
import com.tuda.data.entity.Organization;
import com.tuda.data.entity.Photo;
import com.tuda.data.enums.EventStatus;
import com.tuda.repository.EventRepository;
import com.tuda.service.impl.EventServiceImpl;
import com.tuda.unit.preparer.EventPreparer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.LocalDateTime;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
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
}