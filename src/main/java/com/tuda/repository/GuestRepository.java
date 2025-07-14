package com.tuda.repository;

import com.tuda.data.entity.Event;
import com.tuda.data.entity.Guest;
import org.springframework.data.domain.Limit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface GuestRepository extends JpaRepository<Guest, Long> {
    List<Guest> findAllByEventId(Long eventId);

    Optional<Guest> findByKeyId(String keyId);

    @Query(value = "SELECT COUNT(id) as guest_count FROM guests WHERE event_id = :eventId GROUP BY event_id", nativeQuery = true)
    Optional<Long> findGuestCountByEventId(@Param("eventId") long eventId);
}
