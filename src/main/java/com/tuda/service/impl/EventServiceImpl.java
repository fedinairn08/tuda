package com.tuda.service.impl;

import com.tuda.data.entity.*;
import com.tuda.data.enums.AttendanceUserStatus;
import com.tuda.data.enums.EventStatus;
import com.tuda.data.enums.ParticipantType;
import com.tuda.data.enums.UserRole;
import com.tuda.dto.request.EventRequestDTO;
import com.tuda.dto.response.EventParticipantResponseDTO;
import com.tuda.exception.NotFoundException;
import com.tuda.repository.*;
import com.tuda.service.*;
import com.tuda.service.file.FileService;
import io.micrometer.core.instrument.Counter;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {
    private final RequestService requestService;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final PhotoRepository photoRepository;
    private final FileService fileService;
    private final KeyService keyService;
    private final Counter eventCounter;
    private final GuestRepository guestRepository;
    private final OrganizationRepository organizationRepository;
    private final AccountingUserRepository accountingUserRepository;
    private final GuestService guestService;
    private final AccountingUserService accountingUserService;

    @Override
    @Transactional(readOnly = true)
    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Event getEventById(long id) {
        return eventRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Event not found with id: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Event> getEventsByUserId(long userId) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException(String.format("User not found for id %d", userId));
        }

        return eventRepository.getEventsByUserId(userId);
    }

    @Override
    @Transactional
    public Event updateEvent(EventRequestDTO requestDTO, long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Event not found with id: " + eventId));

        if (requestDTO.getEventStatus().equals(EventStatus.CANCELLED.toString())) {
            requestService.cancelAllEventRequests(eventId);
        }

        modelMapper.map(requestDTO,event);

        if (requestDTO.getFilename() != null && requestDTO.getUploadId() != null && Objects.isNull(event.getPhoto())) {
            Photo photo = new Photo(requestDTO.getUploadId(), requestDTO.getFilename());
            photoRepository.save(photo);
            event.setPhoto(photo);
        } else if (requestDTO.getFilename() == null && requestDTO.getUploadId() == null
                && Objects.nonNull(event.getPhoto())) {
            fileService.delete(event.getPhoto().getFilename());
            photoRepository.delete(event.getPhoto());
            event.setPhoto(null);
        } else if (requestDTO.getFilename() != null && requestDTO.getUploadId() != null
                && !requestDTO.getFilename().equals(event.getPhoto().getFilename())
                && !requestDTO.getUploadId().equals(event.getPhoto().getUploadId())) {
            fileService.delete(event.getPhoto().getFilename());

            Photo photo = photoRepository.findById(event.getPhoto().getId())
                    .orElseThrow(() -> new NotFoundException("Photo not found with id: " + event.getPhoto().getId()));

            photo.setFilename(requestDTO.getFilename()).setUploadId(requestDTO.getUploadId());
            photoRepository.save(photo);

            event.setPhoto(photo);
        }
        return eventRepository.save(event);
    }

    @Override
    @Transactional
    public Event addEvent(EventRequestDTO requestDTO) {
        Event event = modelMapper.map(requestDTO, Event.class);

        event.setEventStatus(EventStatus.WILL);

        if (requestDTO.getOrganizationId() != null) {
            Organization organization = organizationRepository.findById(requestDTO.getOrganizationId())
                    .orElseThrow(() -> new NotFoundException("Organization not found with id " + requestDTO.getOrganizationId()));
            event.setOrganization(organization);
        }

        if (requestDTO.getFilename() != null && requestDTO.getUploadId() != null) {
            Photo photo = new Photo(requestDTO.getUploadId(), requestDTO.getFilename());
            photoRepository.save(photo);
            event.setPhoto(photo);
        }

        eventCounter.increment();

        return eventRepository.save(event);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EventParticipantResponseDTO> getAllParticipantsByEventId(long eventId) {
        if (!eventRepository.existsById(eventId)) {
            throw new NotFoundException("Event not found with id: " + eventId);
        }

        List<Guest> guests = guestRepository.findAllByEventId(eventId);
        List<AccountingAppUser> accountingAppUsers = accountingUserRepository.findAllByEventId(eventId);

        List<EventParticipantResponseDTO> participants = new ArrayList<>();
        for (AccountingAppUser accountingAppUser : accountingAppUsers) {
            EventParticipantResponseDTO participant = new EventParticipantResponseDTO(accountingAppUser.getId(),
                    accountingAppUser.getAppUser().getFullName(), accountingAppUser.isStatus(),
                    accountingAppUser.getUserRole(), ParticipantType.APP_USER);
            participants.add(participant);
        }
        for (Guest guest : guests) {
            EventParticipantResponseDTO participant = new EventParticipantResponseDTO(guest.getId(),
                    guest.getFullName(), guest.isStatus(), UserRole.PARTICIPANT, ParticipantType.GUEST);
            participants.add(participant);
        }

        return participants;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Event> getEventsByStatusAndAppUserIdForUser(EventStatus status, long appUserId) {
        if (!userRepository.existsById(appUserId)) {
            throw new NotFoundException(String.format("User with id: %s -- is not found", appUserId));
        }
        return eventRepository.findAllByStatusAndAppUserIdForUser(appUserId, status.toString());
    }

    @Override
    @Transactional(readOnly = true)
    public List<Event> getOrganizationEventsByOrganizerId(long organizerId) {
        if (!userRepository.existsById(organizerId)) {
            throw new NotFoundException(String.format("User with id: %s -- is not found", organizerId));
        }
        return eventRepository.findAllOrganizationEventsByOrganizerId(organizerId);
    }

    @Override
    public Optional<?> markPresence(String key) {
        return keyService.findEntityByKey(key).map(person -> {
            if (person instanceof Guest guest) {
                return guestService.markPresence(guest.getId());
            } else if (person instanceof AccountingAppUser user) {
                return accountingUserService.markPresence(
                        user.getEvent().getId(),
                        user.getAppUser().getLogin()
                );
            }
            throw new NotFoundException("Ключ не найден");
        });
    }

    @Override
    @Transactional(readOnly = true)
    public List<Event> getEventsByNeededRoleForUser(UserRole role) {
        List<Event> events = eventRepository.findAll();
        List<Event> eventsWithNeededRole = new ArrayList<>();
        for (Event event : events) {
            if (event.getEventStatus() != EventStatus.WILL) {
                continue;
            }

            addEventIfRolesRequired(role, eventsWithNeededRole, event);
        }
        return eventsWithNeededRole;

    }

    private void addEventIfRolesRequired(UserRole role, List<Event> eventsWithNeededRole, Event event) {
        long totalCount =
                eventRepository.findUserCountByCertainRoleAndEventId(role.ordinal(), event.getId()).orElse(0L);
        if (role == UserRole.PARTICIPANT) {
            totalCount += guestRepository.findGuestCountByEventId(event.getId()).orElse(0L);
        }

        if (role == UserRole.PARTICIPANT && totalCount < event.getParticipantsNumber()) {
            eventsWithNeededRole.add(event);
        } else if (role == UserRole.VOLUNTEER && totalCount < event.getVolunteersNumber()) {
            eventsWithNeededRole.add(event);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<Event> getEventsByStatusAndAppUserIdForOrganizer(EventStatus status, long appUserId) {
        if (!userRepository.existsById(appUserId)) {
            throw new NotFoundException(String.format("User with id: %s -- is not found", appUserId));
        }
        return eventRepository.findAllByStatusAndAppUserIdForOrganizer(appUserId, status.toString());
    }

    @Override
    @Transactional(readOnly = true)
    public List<Event>  getEventsByNeededRoleForOrganizer(UserRole role, long appUserId) {
        if (!userRepository.existsById(appUserId)) {
            throw new NotFoundException(String.format("User with id: %s -- is not found", appUserId));
        }

        List<Event> events = eventRepository.findAllOrganizationEventsByOrganizerId(appUserId);
        List<Event> eventsWithNeededRole = new ArrayList<>();
        for (Event event : events) {
            addEventIfRolesRequired(role, eventsWithNeededRole, event);
        }
        return eventsWithNeededRole;
    }

    @Override
    @Transactional(readOnly = true)
    public Long getUserCountWithCertainRoleOnEvent(UserRole role, long eventId) {
        if (!eventRepository.existsById(eventId)) {
            throw new NotFoundException(String.format("Event with id: %s -- is not found", eventId));
        }
        Optional<Long> autorizedUserCountOptional = eventRepository.findUserCountByCertainRoleAndEventId(role.ordinal(), eventId);
        long totalCount = autorizedUserCountOptional.orElse(0L);

        if (role == UserRole.PARTICIPANT) {
            Optional<Long> guestCountOptional = guestRepository.findGuestCountByEventId(eventId);
            totalCount += guestCountOptional.orElse(0L);
        }

        return totalCount;
    }

    @Override
    @Transactional(readOnly = true)
    public AppUser getContactPersonOfEvent(long eventId) {
        return eventRepository.findContactPersonByEventId(eventId).orElseThrow(
                () -> new NotFoundException(String.format("Event with id: %s -- is not found", eventId)));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Event> getEventsByAppUserIdAndAttendanceStatus(long appUserId, AttendanceUserStatus status) {
        if (!userRepository.existsById(appUserId)) {
            throw new NotFoundException(String.format("User with id: %s -- is not found", appUserId));
        }

        return eventRepository.findAllByAppUserIdAndAttendanceStatus(appUserId, status == AttendanceUserStatus.PRESENTED);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Event> getEventsByAppUserIdAndRole(long appUserId, UserRole role) {
        if (!userRepository.existsById(appUserId)) {
            throw new NotFoundException(String.format("User with id: %s -- is not found", appUserId));
        }

        return eventRepository.findAllByAppUserIdAndRole(appUserId, role.ordinal());
    }

}