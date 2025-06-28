package com.tuda.repository;

import com.tuda.entity.Request;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RequestRepository extends JpaRepository<Request, Long> {
    Optional<Request> findByEventIdAndAppUserLogin(Long eventId, String login);
}
