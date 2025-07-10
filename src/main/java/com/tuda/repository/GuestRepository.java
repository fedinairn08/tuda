package com.tuda.repository;

import com.tuda.data.entity.Event;
import com.tuda.data.entity.Guest;
import org.springframework.data.domain.Limit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface GuestRepository extends JpaRepository<Guest, Long> {
    List<Guest> findAllByEventId(Long eventId);

    Optional<Guest> findByKeyId(String keyId);
}
