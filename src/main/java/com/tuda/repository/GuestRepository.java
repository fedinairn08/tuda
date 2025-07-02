package com.tuda.repository;

import com.tuda.data.entity.Guest;
import org.springframework.data.domain.Limit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GuestRepository extends JpaRepository<Guest, Long> {
    List<Guest> findAllByEventId(Long eventId);

}
