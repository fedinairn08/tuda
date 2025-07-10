package com.tuda.service;

import com.tuda.data.entity.AccountingAppUser;
import com.tuda.data.entity.Guest;
import com.tuda.dto.request.GuestRequestDTO;

public interface GuestService {
    Guest addGuest(GuestRequestDTO guestRequestDTO);
    Guest markPresence(Long guestId);
}
