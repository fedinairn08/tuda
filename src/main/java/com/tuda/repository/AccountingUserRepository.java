package com.tuda.repository;

import com.tuda.entity.AccountingAppUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountingUserRepository extends JpaRepository<AccountingAppUser, Long> {

}
