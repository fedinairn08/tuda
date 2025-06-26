package com.tuda.service;

import com.tuda.entity.Request;

import java.util.List;

public interface RequestService {
    List<Request> getAllRequests();
    Request addRequest(Request request);
}
