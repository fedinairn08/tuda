package com.tuda.repository;

import com.tuda.data.entity.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<AppUser, Long> {
    Optional<AppUser> findByLogin(String login);
    Boolean existsByLogin(String login);

    Long getIdByLogin(String login);
}
