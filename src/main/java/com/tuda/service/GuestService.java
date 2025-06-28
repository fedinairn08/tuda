package com.tuda.service;

import com.tuda.dto.request.GuestRequestDTO;
import com.tuda.entity.Guest;

import java.util.List;

public interface GuestService {
    Guest addGuest(GuestRequestDTO guestRequestDTO);
}
