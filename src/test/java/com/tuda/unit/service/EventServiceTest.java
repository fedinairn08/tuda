package com.tuda.unit.service;

import com.tuda.data.entity.*;
import com.tuda.data.enums.EventStatus;
import com.tuda.data.enums.ParticipantType;
import com.tuda.data.enums.UserRole;
import com.tuda.dto.request.EventRequestDTO;
import com.tuda.dto.response.EventParticipantResponseDTO;
import com.tuda.exception.NotFoundException;
import com.tuda.repository.*;
import com.tuda.service.AccountingUserService;
import com.tuda.service.GuestService;
import com.tuda.service.KeyService;
import com.tuda.service.RequestService;
import com.tuda.service.file.FileService;
import com.tuda.service.impl.EventServiceImpl;
import com.tuda.unit.preparer.EntityPreparer;
import com.tuda.unit.preparer.RequestDTOPreparer;
import io.micrometer.core.instrument.Counter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.time.LocalDateTime;
import java.util.*;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
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

    @Mock
    private Counter eventCounter;

    @Mock
    private AccountingUserRepository accountingUserRepository;

    @Mock
    private GuestRepository guestRepository;

    @Mock
    private OrganizationRepository organizationRepository;

    @Mock
    private KeyService keyService;

    @Mock
    private GuestService guestService;

    @Mock
    private AccountingUserService accountingUserService;

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
                RequestDTOPreparer.getEventRequestDTO(EventStatus.WILL, "funFest.png", UUID.randomUUID());
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
                RequestDTOPreparer.getEventRequestDTO(EventStatus.WILL, "sample.png", UUID.randomUUID());

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
                RequestDTOPreparer.getEventRequestDTO(EventStatus.WILL, "funFest.png", UUID.randomUUID());


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
                RequestDTOPreparer.getEventRequestDTO(EventStatus.WILL, "sample.png", UUID.randomUUID());

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


    @Test
    void addEvent_shouldCreateCompleteEvent() {
        LocalDateTime testDate = LocalDateTime.now().plusDays(7);
        UUID uploadId = UUID.randomUUID();

        EventRequestDTO requestDTO = EventRequestDTO.builder()
                .title("Tech Conference")
                .description("Annual technology conference")
                .city("New York")
                .date(testDate)
                .participantsNumber(500)
                .volunteersNumber(50)
                .organizationId(1L)
                .filename("conference.jpg")
                .uploadId(uploadId)
                .build();

        Organization organization = Organization.builder()
                .id(1L)
                .name("Tech Org")
                .phoneNumber("+1234567890")
                .build();

        Photo photo = Photo.builder()
                .uploadId(uploadId)
                .filename("conference.jpg")
                .build();

        Event mappedEvent = Event.builder()
                .title("Tech Conference")
                .description("Annual technology conference")
                .city("New York")
                .date(testDate)
                .participantsNumber(500)
                .volunteersNumber(50)
                .build();

        Event savedEvent = Event.builder()
                .title("Tech Conference")
                .description("Annual technology conference")
                .city("New York")
                .date(testDate)
                .participantsNumber(500)
                .volunteersNumber(50)
                .organization(organization)
                .photo(photo)
                .eventStatus(EventStatus.WILL)
                .build();

        when(modelMapper.map(requestDTO, Event.class)).thenReturn(mappedEvent);
        when(organizationRepository.findById(1L)).thenReturn(Optional.of(organization));
        when(photoRepository.save(any(Photo.class))).thenReturn(photo);
        when(eventRepository.save(mappedEvent)).thenReturn(savedEvent);

        Event result = eventService.addEvent(requestDTO);

        assertNotNull(result);
        assertEquals("Tech Conference", result.getTitle());
        assertEquals("Annual technology conference", result.getDescription());
        assertEquals("New York", result.getCity());
        assertEquals(testDate, result.getDate());
        assertEquals(500, result.getParticipantsNumber());
        assertEquals(50, result.getVolunteersNumber());
        assertEquals(EventStatus.WILL, result.getEventStatus());

        assertNotNull(result.getOrganization());
        assertEquals(1L, result.getOrganization().getId());
        assertEquals("Tech Org", result.getOrganization().getName());

        assertNotNull(result.getPhoto());
        assertEquals(uploadId, result.getPhoto().getUploadId());
        assertEquals("conference.jpg", result.getPhoto().getFilename());

        verify(modelMapper).map(requestDTO, Event.class);
        verify(organizationRepository).findById(1L);
        verify(photoRepository).save(any(Photo.class));
        verify(eventRepository).save(mappedEvent);
        verify(eventCounter).increment();

    }

    @Test
    void addEvent_shouldHandlePhotoCreation() {
        UUID uploadId = UUID.randomUUID();
        EventRequestDTO requestDTO = EventRequestDTO.builder()
                .title("Event with Photo")
                .filename("event.jpg")
                .uploadId(uploadId)
                .build();

        Photo photo = Photo.builder()
                .uploadId(uploadId)
                .filename("event.jpg")
                .build();

        Event mappedEvent = Event.builder()
                .title("Event with Photo")
                .build();

        Event savedEvent = Event.builder()
                .title("Event with Photo")
                .photo(photo)
                .eventStatus(EventStatus.WILL)
                .build();

        when(modelMapper.map(requestDTO, Event.class)).thenReturn(mappedEvent);
        when(photoRepository.save(any(Photo.class))).thenReturn(photo);
        when(eventRepository.save(mappedEvent)).thenReturn(savedEvent);

        Event result = eventService.addEvent(requestDTO);

        assertNotNull(result);
        assertNotNull(result.getPhoto());
        assertEquals(uploadId, result.getPhoto().getUploadId());
        assertEquals("event.jpg", result.getPhoto().getFilename());

        verify(photoRepository).save(any(Photo.class));
    }

    @Test
    void addEvent_shouldThrowWhenOrganizationNotFound() {
        EventRequestDTO requestDTO = EventRequestDTO.builder()
                .title("Invalid Org Event")
                .organizationId(99L)
                .build();

        Event mappedEvent = new Event();
        when(modelMapper.map(requestDTO, Event.class)).thenReturn(mappedEvent);

        when(organizationRepository.findById(99L)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> eventService.addEvent(requestDTO));

        assertEquals("Organization not found with id 99", exception.getMessage());
        verify(organizationRepository).findById(99L);
        verifyNoInteractions(eventRepository, eventCounter);
    }


    @Test
    void getAllParticipantsByEventId_shouldReturnParticipants_whenEventExists() {
        long eventId = 1L;
        when(eventRepository.existsById(eventId)).thenReturn(true);

        Organization organization = new Organization();
        organization.setId(1L);
        organization.setName("Test Org");

        AppUser appUser1 = new AppUser();
        appUser1.setName("John");
        appUser1.setLastName("Doe");
        appUser1.setPatronymic("Smith");
        appUser1.setOrganization(organization);

        AppUser appUser2 = new AppUser();
        appUser2.setName("Jane");
        appUser2.setLastName("Smith");
        appUser2.setPatronymic("Doe");
        appUser2.setOrganization(organization);

        AccountingAppUser accountingUser1 = AccountingAppUser.builder()
                .id(1L)
                .appUser(appUser1)
                .status(true)
                .userRole(UserRole.VOLUNTEER)
                .build();

        AccountingAppUser accountingUser2 = AccountingAppUser.builder()
                .id(2L)
                .appUser(appUser2)
                .status(false)
                .userRole(UserRole.PARTICIPANT)
                .build();

        when(accountingUserRepository.findAllByEventId(eventId))
                .thenReturn(Arrays.asList(accountingUser1, accountingUser2));

        Guest guest1 = Guest.builder()
                .id(3L)
                .fullName("Guest One")
                .status(true)
                .build();

        Guest guest2 = Guest.builder()
                .id(4L)
                .fullName("Guest Two")
                .status(false)
                .build();

        when(guestRepository.findAllByEventId(eventId))
                .thenReturn(Arrays.asList(guest1, guest2));

        List<EventParticipantResponseDTO> result = eventService.getAllParticipantsByEventId(eventId);

        assertEquals(4, result.size());

        EventParticipantResponseDTO accUser1 = result.get(0);
        assertEquals(1L, accUser1.getId());
        assertEquals("Doe John Smith", accUser1.getFullName()); // Verify full name format
        assertTrue(accUser1.getStatus());
        assertEquals(UserRole.VOLUNTEER, accUser1.getRole());
        assertEquals(ParticipantType.APP_USER, accUser1.getType());

        EventParticipantResponseDTO accUser2 = result.get(1);
        assertEquals(2L, accUser2.getId());
        assertEquals("Smith Jane Doe", accUser2.getFullName()); // Verify full name format
        assertFalse(accUser2.getStatus());
        assertEquals(UserRole.PARTICIPANT, accUser2.getRole());
        assertEquals(ParticipantType.APP_USER, accUser2.getType());

        EventParticipantResponseDTO g1 = result.get(2);
        assertEquals(3L, g1.getId());
        assertEquals("Guest One", g1.getFullName());
        assertTrue(g1.getStatus());
        assertEquals(UserRole.PARTICIPANT, g1.getRole());
        assertEquals(ParticipantType.GUEST, g1.getType());

        EventParticipantResponseDTO g2 = result.get(3);
        assertEquals(4L, g2.getId());
        assertEquals("Guest Two", g2.getFullName());
        assertFalse(g2.getStatus());
        assertEquals(UserRole.PARTICIPANT, g2.getRole());
        assertEquals(ParticipantType.GUEST, g2.getType());

        verify(eventRepository).existsById(eventId);
        verify(accountingUserRepository).findAllByEventId(eventId);
        verify(guestRepository).findAllByEventId(eventId);
    }

    @Test
    void getAllParticipantsByEventId_shouldThrowNotFoundException_whenEventDoesNotExist() {
        long eventId = 99L;
        when(eventRepository.existsById(eventId)).thenReturn(false);

        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> eventService.getAllParticipantsByEventId(eventId));

        assertEquals("Event not found with id: " + eventId, exception.getMessage());
        verify(eventRepository).existsById(eventId);
        verifyNoInteractions(accountingUserRepository, guestRepository);
    }

    @Test
    void getAllParticipantsByEventId_shouldReturnEmptyList_whenNoParticipantsExist() {
        long eventId = 2L;
        when(eventRepository.existsById(eventId)).thenReturn(true);
        when(accountingUserRepository.findAllByEventId(eventId)).thenReturn(List.of());
        when(guestRepository.findAllByEventId(eventId)).thenReturn(List.of());

        List<EventParticipantResponseDTO> result = eventService.getAllParticipantsByEventId(eventId);

        assertTrue(result.isEmpty());
        verify(eventRepository).existsById(eventId);
        verify(accountingUserRepository).findAllByEventId(eventId);
        verify(guestRepository).findAllByEventId(eventId);
    }

    @Test
    void getEventsByStatusAndAppUserIdForUser_shouldReturnEventsWhenUserExists() {
        Event event1 = Event.builder()
                .id(1L)
                .title("Event 1")
                .eventStatus(EventStatus.WILL).build();
        Event event2 = Event.builder()
                .id(2L)
                .title("Event 2")
                .eventStatus(EventStatus.PASSED).build();

        when(userRepository.existsById(1L)).thenReturn(true);
        when(eventRepository.findAllByStatusAndAppUserIdForUser(1L, EventStatus.WILL.toString()))
                .thenReturn(Arrays.asList(event1, event2));

        List<Event> result = eventService.getEventsByStatusAndAppUserIdForUser(EventStatus.WILL, 1L);

        assertEquals(2, result.size());
        verify(userRepository, times(1)).existsById(1L);
        verify(eventRepository, times(1))
                .findAllByStatusAndAppUserIdForUser(1L, EventStatus.WILL.toString());
    }

    @Test
    void getEventsByStatusAndAppUserIdForUser_shouldReturnEmptyListWhenNoEventsFound() {
        when(userRepository.existsById(1L)).thenReturn(true);
        when(eventRepository.findAllByStatusAndAppUserIdForUser(1L, EventStatus.WILL.toString()))
                .thenReturn(Collections.emptyList());

        List<Event> result = eventService.getEventsByStatusAndAppUserIdForUser(EventStatus.WILL, 1L);

        assertTrue(result.isEmpty());
        verify(userRepository, times(1)).existsById(1L);
        verify(eventRepository, times(1))
                .findAllByStatusAndAppUserIdForUser(1L, EventStatus.WILL.toString());
    }

    @Test
    void getEventsByStatusAndAppUserIdForUser_shouldThrowNotFoundExceptionWhenUserNotFound() {
        when(userRepository.existsById(999L)).thenReturn(false);

        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            eventService.getEventsByStatusAndAppUserIdForUser(EventStatus.WILL, 999L);
        });

        assertEquals("User with id: 999 -- is not found", exception.getMessage());
        verify(userRepository, times(1)).existsById(999L);
        verify(eventRepository, never()).findAllByStatusAndAppUserIdForUser(anyLong(), anyString());
    }

    @Test
    void getEventsByStatusAndAppUserIdForUser_shouldFilterByCorrectStatus() {
        Event completedEvent = Event.builder()
                .id(2L)
                .title("Completed Event")
                .eventStatus(EventStatus.PASSED).build();

        when(userRepository.existsById(1L)).thenReturn(true);
        when(eventRepository.findAllByStatusAndAppUserIdForUser(1L, EventStatus.PASSED.toString()))
                .thenReturn(Collections.singletonList(completedEvent));

        List<Event> result = eventService.getEventsByStatusAndAppUserIdForUser(EventStatus.PASSED, 1L);

        assertEquals(1, result.size());
        assertEquals(EventStatus.PASSED, result.get(0).getEventStatus());
        verify(eventRepository, times(1))
                .findAllByStatusAndAppUserIdForUser(1L, EventStatus.PASSED.toString());
    }

    @Test
    void getOrganizationEventsByOrganizerId_shouldReturnEventsWhenOrganizerExists() {
        Event orgEvent1 = createTestEvent(1L, "Org Event 1");
        Event orgEvent2 = createTestEvent(2L, "Org Event 2");

        when(userRepository.existsById(1L)).thenReturn(true);
        when(eventRepository.findAllOrganizationEventsByOrganizerId(1L))
                .thenReturn(Arrays.asList(orgEvent1, orgEvent2));

        List<Event> result = eventService.getOrganizationEventsByOrganizerId(1L);

        assertEquals(2, result.size());
        verify(userRepository, times(1)).existsById(1L);
        verify(eventRepository, times(1)).findAllOrganizationEventsByOrganizerId(1L);
    }

    @Test
    void getOrganizationEventsByOrganizerId_shouldReturnEmptyListWhenNoEventsFound() {
        when(userRepository.existsById(1L)).thenReturn(true);
        when(eventRepository.findAllOrganizationEventsByOrganizerId(1L))
                .thenReturn(Collections.emptyList());

        List<Event> result = eventService.getOrganizationEventsByOrganizerId(1L);

        assertTrue(result.isEmpty());
        verify(userRepository, times(1)).existsById(1L);
        verify(eventRepository, times(1)).findAllOrganizationEventsByOrganizerId(1L);
    }

    @Test
    void getOrganizationEventsByOrganizerId_shouldThrowNotFoundExceptionWhenOrganizerNotFound() {
        when(userRepository.existsById(999L)).thenReturn(false);

        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            eventService.getOrganizationEventsByOrganizerId(999L);
        });

        assertEquals("User with id: 999 -- is not found", exception.getMessage());
        verify(userRepository, times(1)).existsById(999L);
        verify(eventRepository, never()).findAllOrganizationEventsByOrganizerId(anyLong());
    }

    @Test
    void getOrganizationEventsByOrganizerId_shouldReturnOnlyOrganizationEvents() {
        Event orgEvent1 = createTestEvent(1L, "Org Event 1");
        Event orgEvent2 = createTestEvent(2L, "Org Event 2");

        when(userRepository.existsById(1L)).thenReturn(true);
        when(eventRepository.findAllOrganizationEventsByOrganizerId(1L))
                .thenReturn(Arrays.asList(orgEvent1, orgEvent2));

        List<Event> result = eventService.getOrganizationEventsByOrganizerId(1L);

        assertEquals(2, result.size());
        assertEquals("Org Event 1", result.get(0).getTitle());
        assertEquals("Org Event 2", result.get(1).getTitle());
        verify(eventRepository, times(1)).findAllOrganizationEventsByOrganizerId(1L);
    }

    private Event createTestEvent(Long id, String title) {
        Event event = new Event();
        event.setId(id);
        event.setTitle(title);
        return event;
    }

    @Test
    void markPresence_shouldReturnEmptyOptionalWhenKeyNotFound() {
        // Arrange
        String invalidKey = "invalid-key";
        when(keyService.findEntityByKey(invalidKey)).thenReturn(Optional.empty());

        // Act
        Optional<?> result = eventService.markPresence(invalidKey);

        // Assert
        assertTrue(result.isEmpty());
        verify(keyService, times(1)).findEntityByKey(invalidKey);
        verify(guestService, never()).markPresence(anyLong());
        verify(accountingUserService, never()).markPresence(anyLong(), anyString());
    }





}