package com.tuda.controller;

import com.tuda.entity.Event;
import com.tuda.entity.Guest;
import com.tuda.entity.Request;
import com.tuda.service.GuestService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/guest")
@RestController
@AllArgsConstructor
public class GuestController {
    private GuestService guestService;

    @PostMapping("/add")
    public Guest addGuest(@RequestBody Guest guest) {
        return guestService.addGuest(guest);
    }
}
