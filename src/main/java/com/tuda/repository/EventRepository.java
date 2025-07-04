package com.tuda.repository;

import com.tuda.data.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {
    @Query(value = "SELECT e.* FROM events e JOIN accounting_app_users ce ON e.id = ce.event_id WHERE ce.app_user_id = :id", nativeQuery = true)
    List<Event> getEventsByUserId(@Param("id") Long id);

    @Query(value = "SELECT e.* FROM events e JOIN accounting_app_users ce ON e.id = ce.event_id WHERE ce.app_user_id = :appUserId AND e.event_status = :status", nativeQuery = true)
    List<Event> findAllByStatusAndAppUserId(@Param("appUserId") long appUserId, @Param("status") String status);

    @Query(value = "SELECT e.* FROM events e JOIN organizations o ON e.organization_id = o.id JOIN app_users ap ON o.id = ap.organization_id WHERE ap.id = :organizerId", nativeQuery = true)
    List<Event> findAllOrganizationEventsByOrganizerId(@Param("organizerId") long organizerId);
}
