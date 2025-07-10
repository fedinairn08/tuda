package com.tuda.repository;

import com.tuda.data.entity.AccountingAppUser;
import com.tuda.data.entity.AppUser;
import com.tuda.data.entity.Guest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AccountingUserRepository extends JpaRepository<AccountingAppUser, Long> {
    Optional<AccountingAppUser> findByAppUserAndEvent_Id(AppUser user, Long eventId);

    void deleteByAppUserAndEvent_Id(AppUser user, Long eventId);

    List<AccountingAppUser> findAllByEventId(Long eventId);

    Optional<AccountingAppUser> findByAppUserIdAndEventId(Long appUserId, Long eventId);

    Optional<AccountingAppUser> findByKeyId(String keyId);
}
