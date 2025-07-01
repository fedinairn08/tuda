package com.tuda.service;

import com.tuda.data.entity.EventParticipant;

import java.util.List;

public interface EventParticipantService {
    List<EventParticipant> getAllParticipantsByEventId(long id);
}
