package com.tuda.service;

import com.tuda.entity.AccountingAppUser;

public interface AccountingUserService {
    AccountingAppUser createAccountingUserAsParticipant(Long eventId, String clientLogin);

    void refuseToParticipate(Long id);

    AccountingAppUser createAccountingUserAsVolunteer(Long eventId, String clientLogin);
}
