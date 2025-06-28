package com.tuda.service;

import com.tuda.dto.request.RequestRequestDTO;
import com.tuda.entity.Request;

import java.util.List;

public interface RequestService {
    List<Request> getAllRequests();
    Request addRequest(Long eventId, String login);
    void deleteRequest(Long eventId, String login);
    Request rejectRequest(Long requestId);

    void cancelAllEventRequests(long id);
}
