package com.tuda.service.impl;

import com.tuda.data.entity.Guest;
import com.tuda.dto.request.GuestRequestDTO;
import com.tuda.repository.GuestRepository;
import com.tuda.service.GuestService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
