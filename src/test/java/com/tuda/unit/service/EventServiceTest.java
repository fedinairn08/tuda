package com.tuda.unit.service;

import com.tuda.data.entity.Event;
import com.tuda.data.entity.Organization;
import com.tuda.data.entity.Photo;
import com.tuda.data.enums.EventStatus;
import com.tuda.dto.request.EventRequestDTO;
import com.tuda.exception.NotFoundException;
import com.tuda.repository.EventRepository;
import com.tuda.repository.PhotoRepository;
import com.tuda.repository.UserRepository;
import com.tuda.service.RequestService;
import com.tuda.service.file.FileService;
import com.tuda.service.impl.EventServiceImpl;
import com.tuda.unit.preparer.EntityPreparer;
import com.tuda.unit.preparer.RequestDTOPreparer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EventServiceTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private EventRepository eventRepository;

    @Mock
    private RequestService requestService;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private PhotoRepository photoRepository;

    @Mock
    private FileService fileService;

    @InjectMocks
    private EventServiceImpl eventService;

    @Test
    void whenNotGetAllEvents_thenReturnEmptyEventList() {
        List<Event> expectedEvents = new ArrayList<>();

        when(eventRepository.findAll()).thenReturn(expectedEvents);

        List<Event> actualEvents = eventService.getAllEvents();

        assertEquals(expectedEvents.size(), actualEvents.size());
        verify(eventRepository).findAll();
    }

    @Test
    void whenGetAllEvents_thenReturnEventList() {
        Organization organization = EntityPreparer.getTestOrganization();
        Photo photo = EntityPreparer.getTestPhoto();
        List<Event> expectedEvents = List.of(EntityPreparer.getTestEvent(photo, organization));

        when(eventRepository.findAll()).thenReturn(expectedEvents);

        List<Event> actualEvents = eventService.getAllEvents();

        verify(eventRepository).findAll();
        assertThat(actualEvents)
                .hasSize(expectedEvents.size())
                .containsExactlyInAnyOrderElementsOf(expectedEvents);
    }

    @Test
    void whenGetEventById_thenReturnEvent() {
        Organization organization = EntityPreparer.getTestOrganization();
        Photo photo = EntityPreparer.getTestPhoto();
        Event expectedEvent = EntityPreparer.getTestEvent(photo, organization);

        when(eventRepository.findById(expectedEvent.getId())).thenReturn(Optional.of(expectedEvent));

        Event actualEvent = eventService.getEventById(expectedEvent.getId());

        verify(eventRepository).findById(expectedEvent.getId());
        assertEquals(expectedEvent, actualEvent);
    }

    @Test
    void whenGetNotExistedEventId_thenReturnNotFoundEvent() {
        Long id = 999L;

        when(eventRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> eventService.getEventById(id));
        verify(eventRepository).findById(id);
    }

    @Test
    void whenGetExistedUserId_thenGetUserEventList() {
        Long expectedUserId = 1L;
        Organization organization = EntityPreparer.getTestOrganization();
        Photo photo = EntityPreparer.getTestPhoto();
        List<Event> expectedEvents = List.of(EntityPreparer.getTestEvent(photo, organization));

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
    void whenGetExistedUserId_thenReturnEmptyUserEventList() {
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
    void whenGetNotExistedUserId_thenReturnNotFoundUserEventList() {
        long notExistedUserId = 999L;

        when(userRepository.existsById(notExistedUserId)).
                thenReturn(false);

        assertThrows(NotFoundException.class, () -> eventService.getEventsByUserId(notExistedUserId));
        verify(userRepository).existsById(notExistedUserId);
    }

    @Test
    void whenGetEventIdAndEventRequestDTOAndEventWithoutPhoto_thenReturnUpdatedEventWithPhoto() {
        Event testEvent = EntityPreparer.getTestEvent(null, null);
        Long testEventId = 1L;
        testEvent.setId(testEventId);
        EventRequestDTO testEventRequestDTO =
                RequestDTOPreparer.getEventRequestDTO(EventStatus.WILL, "FunFest.png", UUID.randomUUID());
        Photo newPhoto = new Photo(testEventRequestDTO.getUploadId(), testEventRequestDTO.getFilename());

        when(eventRepository.findById(testEventId)).thenReturn(Optional.of(testEvent));
        mapFromEventRequestDTOToEvent(testEventRequestDTO, testEvent);
        when(photoRepository.save(any(Photo.class))).thenReturn(newPhoto);
        when(eventRepository.save(testEvent)).thenReturn(testEvent);

        Event actualEvent = eventService.updateEvent(testEventRequestDTO, testEventId);

        assertEquals(testEvent, actualEvent);
        verify(eventRepository).findById(testEventId);
        verify(modelMapper).map(testEventRequestDTO, testEvent);
        verify(photoRepository).save(any(Photo.class));
        verify(eventRepository).save(testEvent);
    }

    @Test
    void whenGetEventIdAndEventRequestDTOAndEventWithPhoto_thenReturnUpdatedEventWithoutPhoto() {
        Photo photo = EntityPreparer.getTestPhoto();
        Event testEvent = EntityPreparer.getTestEvent(photo, null);
        Long testEventId = 1L;
        testEvent.setId(testEventId);
        EventRequestDTO testEventRequestDTO =
                RequestDTOPreparer.getEventRequestDTO(EventStatus.WILL, null, null);

        when(eventRepository.findById(testEventId)).thenReturn(Optional.of(testEvent));
        mapFromEventRequestDTOToEvent(testEventRequestDTO, testEvent);
        doNothing().when(fileService).delete(testEvent.getPhoto().getFilename());
        doNothing().when(photoRepository).delete(testEvent.getPhoto());
        when(eventRepository.save(testEvent)).thenReturn(testEvent);

        Event actualEvent = eventService.updateEvent(testEventRequestDTO, testEventId);

        assertEquals(testEvent, actualEvent);
        verify(eventRepository).findById(testEventId);
        verify(modelMapper).map(testEventRequestDTO, testEvent);
        verify(eventRepository).save(testEvent);
    }

    @Test
    void whenGetEventIdAndEventRequestDTOAndEventWithPhoto_thenReturnUpdatedEventWithPhoto() {
        Photo photo = EntityPreparer.getTestPhoto();
        Event testEvent = EntityPreparer.getTestEvent(photo, null);
        Long testEventId = 1L;
        testEvent.setId(testEventId);
        EventRequestDTO testEventRequestDTO =
                RequestDTOPreparer.getEventRequestDTO(EventStatus.WILL, "Sample.png", UUID.randomUUID());

        when(eventRepository.findById(testEventId)).thenReturn(Optional.of(testEvent));
        mapFromEventRequestDTOToEvent(testEventRequestDTO, testEvent);
        doNothing().when(fileService).delete(testEvent.getPhoto().getFilename());
        when(photoRepository.findById(testEvent.getPhoto().getId())).thenReturn(Optional.of(photo));
        when(photoRepository.save(testEvent.getPhoto())).thenReturn(testEvent.getPhoto());
        when(eventRepository.save(testEvent)).thenReturn(testEvent);

        Event actualEvent = eventService.updateEvent(testEventRequestDTO, testEventId);

        assertEquals(testEvent, actualEvent);
        verify(eventRepository).findById(testEventId);
        verify(modelMapper).map(testEventRequestDTO, testEvent);
        verify(fileService).delete(testEvent.getPhoto().getFilename());
        verify(photoRepository).findById(testEvent.getPhoto().getId());
        verify(photoRepository).save(testEvent.getPhoto());
        verify(eventRepository).save(testEvent);
    }

    @Test
    void whenEventIdAndEventRequestDTOWithCancelStatus_thenCancelAllEventRequest() {
        Photo photo = EntityPreparer.getTestPhoto();
        Event testEvent = EntityPreparer.getTestEvent(photo, null);
        Long testEventId = 1L;
        testEvent.setId(testEventId);
        EventRequestDTO testEventRequestDTO =
                RequestDTOPreparer.getEventRequestDTO(EventStatus.CANCELLED, testEvent.getPhoto().getFilename(),
                        testEvent.getPhoto().getUploadId());

        when(eventRepository.findById(testEventId)).thenReturn(Optional.of(testEvent));
        doNothing().when(requestService).cancelAllEventRequests(testEvent.getId());
        mapFromEventRequestDTOToEvent(testEventRequestDTO, testEvent);
        when(eventRepository.save(testEvent)).thenReturn(testEvent);

        Event actualEvent = eventService.updateEvent(testEventRequestDTO, testEventId);

        assertEquals(testEvent, actualEvent);
        verify(eventRepository).findById(testEventId);
        verify(modelMapper).map(testEventRequestDTO, testEvent);
        verify(eventRepository).save(testEvent);
    }

    @Test
    void whenGetNotExistedEventIdAndEventRequestDTO_thenNotFoundEventId() {
        Event testEvent = EntityPreparer.getTestEvent(null, null);
        Long testEventId = 1L;
        testEvent.setId(testEventId);
        EventRequestDTO testEventRequestDTO =
                RequestDTOPreparer.getEventRequestDTO(EventStatus.WILL, "FunFest.png", UUID.randomUUID());


        when(eventRepository.findById(testEventId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> eventService.updateEvent(testEventRequestDTO, testEventId));
        verify(eventRepository).findById(testEventId);
    }

    @Test
    void whenGetEventIdAndEventRequestDTOAndOldNotExistedPhoto_thenReturnNotFoundPhoto() {
        Photo photo = EntityPreparer.getTestPhoto();
        Event testEvent = EntityPreparer.getTestEvent(photo, null);
        Long testEventId = 1L;
        testEvent.setId(testEventId);
        EventRequestDTO testEventRequestDTO =
                RequestDTOPreparer.getEventRequestDTO(EventStatus.WILL, "Sample.png", UUID.randomUUID());

        when(eventRepository.findById(testEventId)).thenReturn(Optional.of(testEvent));
        mapFromEventRequestDTOToEvent(testEventRequestDTO, testEvent);
        doNothing().when(fileService).delete(testEvent.getPhoto().getFilename());
        when(photoRepository.findById(testEvent.getPhoto().getId())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> eventService.updateEvent(testEventRequestDTO, testEventId));
        verify(eventRepository).findById(testEventId);
        verify(modelMapper).map(testEventRequestDTO, testEvent);
        verify(fileService).delete(testEvent.getPhoto().getFilename());
        verify(photoRepository).findById(testEvent.getPhoto().getId());
    }

    void mapFromEventRequestDTOToEvent(EventRequestDTO testEventRequestDTO, Event testEvent) {
        doAnswer(invocation -> {
            EventRequestDTO req = (EventRequestDTO) invocation.getArguments()[0];
            Event event = (Event) invocation.getArguments()[1];
            event.setCity(req.getCity());
            event.setDate(req.getDate());
            event.setTitle(req.getTitle());
            event.setDescription(req.getDescription());
            event.setParticipantsNumber(req.getParticipantsNumber());
            event.setVolunteersNumber(req.getVolunteersNumber());
            event.setEventStatus(EventStatus.valueOf(req.getEventStatus()));
            return null;
        }).when(modelMapper).map(testEventRequestDTO, testEvent);
    }
}