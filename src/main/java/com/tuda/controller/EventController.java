package com.tuda.controller;

import com.tuda.entity.Event;
import com.tuda.entity.Request;
import com.tuda.service.EventService;
import com.tuda.service.RequestService;
import lombok.AllArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/event")
@RestController
@AllArgsConstructor
public class EventController {
    private final EventService eventService;

    @GetMapping("/getAll")
    public List<Event> getAllEvents() {
        return eventService.getAllEvents();
    }

    @GetMapping("/getById/{id}")
    public Event getEventById(@PathVariable Long id) {
        return eventService.getEventById(id);
    }

    @GetMapping("/getByUserId")
    public List<Event> getEventsByUserId(@Param("id") Long id) {
        return eventService.getEventsByUserId(id);
    }

    @PutMapping("/update")
    public Event updateEvent(@RequestBody Event event) {
        return eventService.updateEvent(event);
    }

    @PostMapping("/add")
    public Event addEvent(@RequestBody Event event) {
        return eventService.addEvent(event);
    }
}
