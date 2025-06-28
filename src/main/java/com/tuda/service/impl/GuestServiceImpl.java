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
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.function.Consumer;

@Service
@AllArgsConstructor
public class GuestServiceImpl implements GuestService {
    private GuestRepository guestRepository;
    private final ModelMapper modelMapper;

    @Override
    @Transactional
    public Guest addGuest(GuestRequestDTO guestRequestDTO) {
        Guest guest = modelMapper.map(guestRequestDTO, Guest.class);
        return guestRepository.save(guest);
    }


}
