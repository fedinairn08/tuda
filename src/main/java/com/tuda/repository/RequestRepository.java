package com.tuda.repository;

import com.tuda.entity.Event;
import com.tuda.entity.Request;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface RequestRepository extends JpaRepository<Request, Long> {
    Optional<Request> findByEventIdAndAppUserLogin(Long eventId, String login);
    List<Request> findAllByEventId(Long eventId);

    @Query("SELECT r FROM Request r WHERE r.event.id = :eventId AND r.status = true")
    List<Request> findActiveByEventId(@Param("eventId") Long eventId);

    @Modifying
    @Query("DELETE FROM Request r WHERE r.event.id = :eventId")
    void deleteAllByEventId(@Param("eventId") Long eventId);
}
