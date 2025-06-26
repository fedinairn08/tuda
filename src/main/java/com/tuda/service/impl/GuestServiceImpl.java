package com.tuda.service.impl;

import com.tuda.entity.Guest;
import com.tuda.exception.BadRequestException;
import com.tuda.repository.GuestRepository;
import com.tuda.service.GuestService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class GuestServiceImpl implements GuestService {
    private GuestRepository guestRepository;

    @Override
    public Guest addGuest(Guest guest) {
        return guestRepository.save(guest);
    }
}
