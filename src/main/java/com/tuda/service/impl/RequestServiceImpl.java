package com.tuda.service.impl;

import com.tuda.entity.Request;
import com.tuda.repository.RequestRepository;
import com.tuda.service.RequestService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class RequestServiceImpl implements RequestService {
    private final RequestRepository requestRepository;

    @Override
    public List<Request> getAllRequests() {
        return requestRepository.findAll();
    }

    @Override
    public Request addRequest(Request request) {
        return requestRepository.save(request);
    }
}
