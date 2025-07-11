package com.tuda.controller;

import com.tuda.data.entity.*;
import com.tuda.data.enums.EventStatus;
import com.tuda.data.enums.UserRole;
import com.tuda.dto.ApiResponse;
import com.tuda.dto.request.EventRequestDTO;
import com.tuda.dto.response.EventResponseDTO;
import com.tuda.dto.response.EventParticipantResponseDTO;
import com.tuda.service.EventService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/event")
@RestController
public class EventController extends EntityController<Event> {
    private final EventService eventService;

    private static final Class<EventResponseDTO> EVENT_RESPONSE_DTO_CLASS = EventResponseDTO.class;

    public EventController(ModelMapper modelMapper, EventService eventService) {
        super(modelMapper);
        this.eventService = eventService;
    }

    @GetMapping("/getAll")
    public ResponseEntity<ApiResponse<List<EventResponseDTO>>> getAllEvents() {
        List<Event> events = eventService.getAllEvents();
        List<EventResponseDTO> dtos = serialize(events, EVENT_RESPONSE_DTO_CLASS);
        return ResponseEntity.ok(new ApiResponse<>(dtos));
    }

    @GetMapping("/getById/{id}")
    public ResponseEntity<ApiResponse<EventResponseDTO>> getEventById(@PathVariable long id) {
        Event event = eventService.getEventById(id);
        EventResponseDTO dto = serialize(event, EVENT_RESPONSE_DTO_CLASS);
        return ResponseEntity.ok(new ApiResponse<>(dto));
    }

    @SecurityRequirement(name = "JWT")
    @GetMapping("/getByUserId/{id}")
    public ResponseEntity<ApiResponse<List<EventResponseDTO>>> getEventsByUserId(@PathVariable long id) {
        List<Event> events = eventService.getEventsByUserId(id);
        List<EventResponseDTO> dtos = serialize(events, EVENT_RESPONSE_DTO_CLASS);
        return ResponseEntity.ok(new ApiResponse<>(dtos));
    }

    @SecurityRequirement(name = "JWT")
    @PutMapping("/update")
    public ResponseEntity<ApiResponse<EventResponseDTO>> updateEvent(@RequestBody EventRequestDTO requestDTO, @RequestParam("id") long id) {
        Event updatedEvent = eventService.updateEvent(requestDTO, id);
        EventResponseDTO dto = serialize(updatedEvent, EVENT_RESPONSE_DTO_CLASS);
        return ResponseEntity.ok(new ApiResponse<>(dto));
    }

    @SecurityRequirement(name = "JWT")
    @PostMapping("/add")
    public ResponseEntity<ApiResponse<EventResponseDTO>> addEvent(@RequestBody EventRequestDTO requestDTO) {
        Event newEvent = eventService.addEvent(requestDTO);
        EventResponseDTO dto = serialize(newEvent, EVENT_RESPONSE_DTO_CLASS);
        return ResponseEntity.ok(new ApiResponse<>(dto));
    }

    @SecurityRequirement(name = "JWT")
    @GetMapping("/{id}/participants")
    public ResponseEntity<ApiResponse<List<EventParticipantResponseDTO>>> getParticipantsByEventId(@PathVariable("id") long eventId) {
        List<EventParticipantResponseDTO> participantsDTO = eventService.getAllParticipantsByEventId(eventId);
        return ResponseEntity.ok(new ApiResponse<>(participantsDTO));
    }

    @SecurityRequirement(name = "JWT")
    @GetMapping("/filterByStatusAndAppUserIdForUser")
    public ResponseEntity<ApiResponse<List<EventResponseDTO>>> getEventsByStatusAndAppUserIdForUser(@RequestParam EventStatus status, @RequestParam long appUserId) {
        List<Event> filteredEvents = eventService.getEventsByStatusAndAppUserIdForUser(status, appUserId);
        List<EventResponseDTO> dtos = serialize(filteredEvents, EVENT_RESPONSE_DTO_CLASS);
        return ResponseEntity.ok(new ApiResponse<>(dtos));
    }

    @SecurityRequirement(name = "JWT")
    @GetMapping("/getEventsByOrganizerId")
    public ResponseEntity<ApiResponse<List<EventResponseDTO>>> getOrganizationEventsByOrganizerId(@RequestParam long organizerId) {
        List<Event> organizerEvents = eventService.getOrganizationEventsByOrganizerId(organizerId);
        List<EventResponseDTO> dtos = serialize(organizerEvents, EVENT_RESPONSE_DTO_CLASS);
        return ResponseEntity.ok(new ApiResponse<>(dtos));
    }

    @SecurityRequirement(name = "JWT")
    @PutMapping("/markPresence")
    public ResponseEntity<ApiResponse<?>> markPresence(@RequestParam String key) {
        return ResponseEntity.ok(new ApiResponse<>(eventService.markPresence(key)));
    }

    @SecurityRequirement(name = "JWT")
    @GetMapping("/getEventsByNeededRoleForUser")
    public ResponseEntity<ApiResponse<List<EventResponseDTO>>> getEventsByNeededRoleForUser(@RequestParam UserRole role) {
        List<Event> eventsWithNeededRole = eventService.getEventsByNeededRoleForUser(role);
        List<EventResponseDTO> dtos = serialize(eventsWithNeededRole, EVENT_RESPONSE_DTO_CLASS);
        return ResponseEntity.ok(new ApiResponse<>(dtos));
    }

    @SecurityRequirement(name = "JWT")
    @GetMapping("/filterByStatusAndAppUserIdForOrganizer")
    public ResponseEntity<ApiResponse<List<EventResponseDTO>>> getEventsByStatusAndAppUserIdForOrganizer(@RequestParam EventStatus status, @RequestParam long appUserId) {
        List<Event> filteredEvents = eventService.getEventsByStatusAndAppUserIdForOrganizer(status, appUserId);
        List<EventResponseDTO> dtos = serialize(filteredEvents, EVENT_RESPONSE_DTO_CLASS);
        return ResponseEntity.ok(new ApiResponse<>(dtos));
    }

    @SecurityRequirement(name = "JWT")
    @GetMapping("/getEventsByNeededRoleForOrganizer")
    public ResponseEntity<ApiResponse<List<EventResponseDTO>>> getEventsByNeededRoleForOrganizer(@RequestParam UserRole role,  @RequestParam long appUserId) {
        List<Event> eventsWithNeededRole = eventService.getEventsByNeededRoleForOrganizer(role, appUserId);
        List<EventResponseDTO> dtos = serialize(eventsWithNeededRole, EVENT_RESPONSE_DTO_CLASS);
        return ResponseEntity.ok(new ApiResponse<>(dtos));
    }

}
