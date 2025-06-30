package com.tuda.service.impl;

import com.tuda.data.entity.Event;
import com.tuda.data.entity.Photo;
import com.tuda.data.enums.EventStatus;
import com.tuda.dto.request.EventRequestDTO;
import com.tuda.exception.NotFoundException;
import com.tuda.repository.EventRepository;
import com.tuda.repository.PhotoRepository;
import com.tuda.repository.UserRepository;
import com.tuda.service.EventService;
import com.tuda.service.RequestService;
import com.tuda.service.file.FileService;
import io.micrometer.core.instrument.Counter;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {
    private final RequestService requestService;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final PhotoRepository photoRepository;
    private final FileService fileService;
    private final Counter eventCounter;

    @Override
    @Transactional(readOnly = true)
    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Event getEventById(long id) {
        return eventRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Event not found with id: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Event> getEventsByUserId(long userId) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException(String.format("User not found for id %d", userId));
        }

        return eventRepository.getEventsByUserId(userId);
    }

    @Override
    @Transactional
    public Event updateEvent(EventRequestDTO requestDTO, long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Event not found with id: " + eventId));

        if (requestDTO.getEventStatus().equals(EventStatus.CANCELLED.toString())) {
            requestService.cancelAllEventRequests(eventId);
        }

        modelMapper.map(requestDTO,event);

        if (requestDTO.getFilename() != null && requestDTO.getUuid() != null && Objects.isNull(event.getPhoto())) {
            Photo photo = new Photo(requestDTO.getUuid(), requestDTO.getFilename());
            photoRepository.save(photo);
            event.setPhoto(photo);
        } else if (requestDTO.getFilename() == null && requestDTO.getUuid() == null
                && Objects.nonNull(event.getPhoto())) {
            fileService.delete(event.getPhoto().getFilename());
            photoRepository.delete(event.getPhoto());
            event.setPhoto(null);
        } else if (requestDTO.getFilename() != null && requestDTO.getUuid() != null) {
            fileService.delete(event.getPhoto().getFilename());

            Photo photo = photoRepository.findById(event.getPhoto().getId())
                    .orElseThrow(() -> new NotFoundException("Photo not found with id: " + event.getPhoto().getId()));

            photo.setFilename(requestDTO.getFilename()).setUploadId(requestDTO.getUuid());
            photoRepository.save(photo);

            event.setPhoto(photo);
        }
        return eventRepository.save(event);
    }

    @Override
    @Transactional
    public Event addEvent(EventRequestDTO requestDTO) {
        Event event = new Event();
        modelMapper.map(requestDTO,event);

        event.setEventStatus(EventStatus.WILL);

        if (requestDTO.getFilename() != null && requestDTO.getUuid() != null) {
            Photo photo = new Photo(requestDTO.getUuid(), requestDTO.getFilename());
            photoRepository.save(photo);
            event.setPhoto(photo);
        }

        eventCounter.increment();

        return eventRepository.save(event);
    }
}