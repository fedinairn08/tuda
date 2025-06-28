package com.tuda.controller;

import com.tuda.dto.ApiResponse;
import com.tuda.dto.request.EventRequestDTO;
import com.tuda.dto.response.EventResponseDTO;
import com.tuda.entity.AppUser;
import com.tuda.entity.Event;
import com.tuda.service.EventService;
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

    @GetMapping("/getByUserId")
    public ResponseEntity<ApiResponse<List<EventResponseDTO>>> getEventsByUserId(@RequestParam("id") long id) {
        List<Event> events = eventService.getEventsByUserId(id);
        List<EventResponseDTO> dtos = serialize(events, EVENT_RESPONSE_DTO_CLASS);
        return ResponseEntity.ok(new ApiResponse<>(dtos));
    }

    @PutMapping("/update")
    public ResponseEntity<ApiResponse<EventResponseDTO>> updateEvent(@RequestBody EventRequestDTO requestDTO, @PathVariable("id") long id) {
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
}
