package com.tuda.repository;

import com.tuda.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {
    @Query(value = "SELECT e.* FROM events e JOIN accounting_app_users ce ON e.id = ce.event_id WHERE ce.app_user_id = :id", nativeQuery = true)
    List<Event> getEventsByUserId(@Param("id") Long id);
}
