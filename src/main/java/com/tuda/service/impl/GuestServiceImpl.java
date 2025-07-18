package com.tuda.service.impl;

import com.tuda.data.entity.Guest;
import com.tuda.dto.request.GuestRequestDTO;
import com.tuda.exception.BadRequestException;
import com.tuda.exception.NotFoundException;
import com.tuda.repository.GuestRepository;
import com.tuda.service.GuestService;
import com.tuda.service.KeyService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@AllArgsConstructor
public class GuestServiceImpl implements GuestService {
    private GuestRepository guestRepository;
    private final ModelMapper modelMapper;
    private final EmailServiceImpl emailService;
    private final KeyService keyService;

    @Override
    @Transactional
    public Guest addGuest(GuestRequestDTO guestRequestDTO) {
        try {
            Guest guest = modelMapper.map(guestRequestDTO, Guest.class);
            String participationCode = keyService.generateKey();
            guest.setKeyId(participationCode);
            emailService.sendQrEmail(guest.getMail(), "QR-код для участия на мероприятии " + guest.getEvent().getTitle(), "Участвует " + guest.getFullName(), participationCode);

            return guestRepository.save(guest);
        } catch (Exception e) {
            throw new BadRequestException("Ошибка: " + e.getMessage());
        }

    }

    @Override
    public Guest markPresence(Long guestId) {
        Guest guest = guestRepository.findById(guestId).orElseThrow(() ->
                new NotFoundException(String.format("Guest with id: %s -- is not found", guestId)));

        guest.setStatus(true);
        return guestRepository.save(guest);
    }


}
