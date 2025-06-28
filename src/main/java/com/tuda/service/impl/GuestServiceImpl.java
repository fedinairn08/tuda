package com.tuda.service.impl;

import com.tuda.dto.request.EventRequestDTO;
import com.tuda.dto.request.GuestRequestDTO;
import com.tuda.entity.Event;
import com.tuda.entity.Guest;
import com.tuda.enums.EventStatus;
import com.tuda.exception.BadRequestException;
import com.tuda.exception.NotFoundException;
import com.tuda.repository.EventRepository;
import com.tuda.repository.GuestRepository;
import com.tuda.repository.UserRepository;
import com.tuda.service.GuestService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.function.Consumer;

@Service
@AllArgsConstructor
public class GuestServiceImpl implements GuestService {
    private GuestRepository guestRepository;
    private EventRepository eventRepository;

    @Override
    @Transactional
    public Guest addGuest(GuestRequestDTO guestRequestDTO) {
        Guest guest = new Guest();

        updateGuestFromDto(guest, guestRequestDTO);

        return guestRepository.save(guest);
    }

    private void updateGuestFromDto(Guest guest, GuestRequestDTO dto) {
        updateIfNotBlank(guest::setFullName, dto.getFullName());
        updateIfNotBlank(guest::setMail, dto.getMail());
        updateIfNotBlank(guest::setKeyId, dto.getKeyId());

        if (dto.getEvent() != null) {
            guest.setEvent(eventRepository.findById(dto.getEvent())
                    .orElseThrow(() -> new NotFoundException(String.format("Event not found for id %d", dto.getEvent()))));
        }
        if (dto.getStatus() != null) {
            guest.setStatus(dto.getStatus());
        }
    }

    private void updateIfNotBlank(Consumer<String> setter, String value) {
        if (value != null && !value.isBlank()) {
            setter.accept(value);
        }
    }
}
