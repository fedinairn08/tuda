package com.tuda.service;

import com.tuda.entity.AccountingAppUser;

public interface AccountingUserService {
    AccountingAppUser createAccountingUserAsParticipant(Long eventId, String clientLogin);

    void refuse(Long eventId, String userLogin);

    AccountingAppUser createAccountingUserAsVolunteer(Long eventId, String clientLogin);

    AccountingAppUser markPresence(Long eventId, String login);
}
