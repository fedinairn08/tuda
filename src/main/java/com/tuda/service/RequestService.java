package com.tuda.service;

import com.tuda.data.entity.Request;

import java.util.List;

public interface RequestService {
    List<Request> getAllEventRequests(Long eventId);
    Request addRequest(Long eventId, String login);
    void deleteRequest(Long eventId, String login);
    Request rejectRequest(Long eventId, String login);
    void cancelAllEventRequests(long id);
}
