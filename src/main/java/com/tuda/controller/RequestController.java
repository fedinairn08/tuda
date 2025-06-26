package com.tuda.controller;

import com.tuda.entity.Request;
import com.tuda.service.RequestService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/request")
@RestController
@AllArgsConstructor
public class RequestController {
    private final RequestService requestService;

    @GetMapping("/getAll")
    public List<Request> getAllRequests() {
        return requestService.getAllRequests();
    }

    @PostMapping("/add")
    public Request addRequest(@RequestBody Request request) {
        return requestService.addRequest(request);
    }
}
