package com.tuda.repository;

import com.tuda.entity.AccountingAppUser;
import com.tuda.entity.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountingUserRepository extends JpaRepository<AccountingAppUser, Long> {
    Optional<AccountingAppUser> findByAppUserAndEvent_Id(AppUser user, Long eventId);

    void deleteByAppUserAndEvent_Id(AppUser user, Long eventId);
}
