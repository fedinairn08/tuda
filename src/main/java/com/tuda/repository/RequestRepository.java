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

    List<Request> findByEventIdAndStatusTrue(Long eventId);

    @Modifying
    void deleteAllByEventId(Long eventId);
}
