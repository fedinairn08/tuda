package com.tuda.service.impl;

import com.tuda.data.entity.AccountingAppUser;
import com.tuda.data.entity.EventParticipant;
import com.tuda.data.entity.Guest;
import com.tuda.data.enums.ParticipantType;
import com.tuda.repository.AccountingUserRepository;
import com.tuda.repository.GuestRepository;
import com.tuda.service.EventParticipantService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EventParticipantEventServiceImpl implements EventParticipantService {
    private final GuestRepository guestRepository;
    private final AccountingUserRepository accountingUserRepository;

    @Override
    @Transactional(readOnly = true)
    public List<EventParticipant> getAllParticipantsByEventId(long eventId) {
        List<Guest> guests = guestRepository.findAllByEventId(eventId);
        List<AccountingAppUser> accountingAppUsers = accountingUserRepository.findAllByEventId(eventId);

        List<EventParticipant> participants = new ArrayList<>();
        for (AccountingAppUser accountingAppUser : accountingAppUsers) {
            EventParticipant participant = new EventParticipant(accountingAppUser.getId(),
                    accountingAppUser.getAppUser().getFullName(), accountingAppUser.isStatus(),
                    accountingAppUser.getUserRole(), ParticipantType.APP_USER);
            participants.add(participant);
        }
        for (Guest guest : guests) {
            EventParticipant participant = new EventParticipant(guest.getId(),
                    guest.getFullName(), guest.isStatus(), null, ParticipantType.GUEST);
            participants.add(participant);
        }

        return participants;
    }


}
