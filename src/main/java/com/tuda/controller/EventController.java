package com.tuda.controller;

import com.tuda.data.entity.*;
import com.tuda.dto.ApiResponse;
import com.tuda.dto.request.EventRequestDTO;
import com.tuda.dto.response.EventResponseDTO;
import com.tuda.dto.response.EventParticipantResponseDTO;
import com.tuda.service.EventParticipantService;
import com.tuda.service.EventService;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/event")
@RestController
public class EventController extends EntityController<Event> {
    private final EventService eventService;
    private final EventParticipantService eventParticipantService;

    private static final Class<EventResponseDTO> EVENT_RESPONSE_DTO_CLASS = EventResponseDTO.class;
    private static final Class<EventParticipantResponseDTO> EVENT_PARTICIPANT_RESPONSE_DTO_CLASS = EventParticipantResponseDTO.class;


    public EventController(ModelMapper modelMapper, EventService eventService,
                           EventParticipantService eventParticipantService) {
        super(modelMapper);
        this.eventService = eventService;
        this.eventParticipantService = eventParticipantService;
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

    @GetMapping("/getByUserId/{id}")
    public ResponseEntity<ApiResponse<List<EventResponseDTO>>> getEventsByUserId(@PathVariable long id) {
        List<Event> events = eventService.getEventsByUserId(id);
        List<EventResponseDTO> dtos = serialize(events, EVENT_RESPONSE_DTO_CLASS);
        return ResponseEntity.ok(new ApiResponse<>(dtos));
    }

    @PutMapping("/update")
    public ResponseEntity<ApiResponse<EventResponseDTO>> updateEvent(@RequestBody EventRequestDTO requestDTO, @RequestParam("id") long id) {
        Event updatedEvent = eventService.updateEvent(requestDTO, id);
        EventResponseDTO dto = serialize(updatedEvent, EVENT_RESPONSE_DTO_CLASS);
        return ResponseEntity.ok(new ApiResponse<>(dto));
    }

    @PostMapping("/add")
    public ResponseEntity<ApiResponse<EventResponseDTO>> addEvent(@RequestBody EventRequestDTO requestDTO) {
        Event updatedEvent = eventService.addEvent(requestDTO);
        EventResponseDTO dto = serialize(updatedEvent, EVENT_RESPONSE_DTO_CLASS);
        return ResponseEntity.ok(new ApiResponse<>(dto));
    }

    @GetMapping("/{id}/participants")
    public ResponseEntity<ApiResponse<List<EventParticipantResponseDTO>>> getParticipantsByEventId(@PathVariable("id") long eventId) {
        List<EventParticipant> participants = eventParticipantService.getAllParticipantsByEventId(eventId);
        List<EventParticipantResponseDTO> eventParticipantResponseDTOS =
                participants.stream().map(participant ->
                        modelMapper.map(participant, EVENT_PARTICIPANT_RESPONSE_DTO_CLASS)).toList();
        return ResponseEntity.ok(new ApiResponse<>(eventParticipantResponseDTOS));
    }
}
