package com.tuda.repository;

import com.tuda.data.entity.AppUser;
import com.tuda.data.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface EventRepository extends JpaRepository<Event, Long> {
    @Query(value = "SELECT e.* FROM events e JOIN accounting_app_users ce ON e.id = ce.event_id WHERE ce.app_user_id = :id", nativeQuery = true)
    List<Event> getEventsByUserId(@Param("id") Long id);

    @Query(value = "SELECT e.* FROM events e JOIN accounting_app_users ce ON e.id = ce.event_id WHERE ce.app_user_id = :appUserId AND e.event_status = :status", nativeQuery = true)
    List<Event> findAllByStatusAndAppUserIdForUser(@Param("appUserId") long appUserId, @Param("status") String status);

    @Query(value = "SELECT e.* FROM events e JOIN organizations o ON e.organization_id = o.id JOIN app_users ap ON o.id = ap.organization_id WHERE ap.id = :organizerId", nativeQuery = true)
    List<Event> findAllOrganizationEventsByOrganizerId(@Param("organizerId") long organizerId);

    @Query(value = "SELECT e.* FROM events e LEFT JOIN (SELECT ac.event_id, COUNT(ac.id) as participant_count FROM accounting_app_users ac WHERE ac.user_role = :userRole GROUP BY ac.event_id) acc ON e.id = acc.event_id WHERE (e.participants_number > acc.participant_count OR acc.participant_count is NULL) AND e.event_status = 'WILL'", nativeQuery = true)
    List<Event> findAllByNeededParticipantForUser(@Param("userRole") long userRole);

    @Query(value = "SELECT e.* FROM events e LEFT JOIN (SELECT ac.event_id, COUNT(ac.id) as volunteer_count FROM accounting_app_users ac WHERE ac.user_role = :userRole GROUP BY ac.event_id) acc ON e.id = acc.event_id WHERE (e.volunteers_number > acc.volunteer_count OR acc.volunteer_count is NULL) AND e.event_status = 'WILL'", nativeQuery = true)
    List<Event> findAllByNeededVolunteersForUser(@Param("userRole") long userRole);

    @Query(value = "SELECT e.* FROM events e JOIN organizations o ON e.organization_id = o.id JOIN app_users app ON o.id = app.organization_id WHERE app.id = :appUserId AND e.event_status = :status", nativeQuery = true)
    List<Event> findAllByStatusAndAppUserIdForOrganizer(@Param("appUserId") long appUserId, @Param("status") String status);

    @Query(value = "SELECT e.* FROM events e JOIN organizations o ON e.organization_id = o.id JOIN app_users app ON o.id = app.organization_id WHERE app.id = :appUserId INTERSECT SELECT e.* FROM events e LEFT JOIN (SELECT ac.event_id, COUNT(ac.id) as participant_count FROM accounting_app_users ac WHERE ac.user_role = :userRole GROUP BY ac.event_id) acc ON e.id = acc.event_id WHERE e.participants_number > acc.participant_count OR acc.participant_count is NULL", nativeQuery = true)
    List<Event> findAllByNeededParticipantForOrganizer(@Param("appUserId") long appUserId, @Param("userRole") long userRole);

    @Query(value = "SELECT e.* FROM events e JOIN organizations o ON e.organization_id = o.id JOIN app_users app ON o.id = app.organization_id WHERE app.id = :appUserId INTERSECT SELECT e.* FROM events e LEFT JOIN (SELECT ac.event_id, COUNT(ac.id) as volunteer_count FROM accounting_app_users ac WHERE ac.user_role = :userRole GROUP BY ac.event_id) acc ON e.id = acc.event_id WHERE e.volunteers_number > acc.volunteer_count OR acc.volunteer_count is NULL", nativeQuery = true)
    List<Event> findAllByNeededVolunteersForOrganizer(@Param("appUserId") long appUserId, @Param("userRole") long userRole);

    @Query(value = "SELECT COUNT(acc.id) as user_count FROM accounting_app_users acc WHERE acc.user_role = :userRole AND acc.event_id = :eventId GROUP BY acc.event_id\n", nativeQuery = true)
    Optional<Long> findUserCountByCertainRoleAndEventId(@Param("userRole") long userRole, @Param("eventId")  long eventId);

    @Query(value = "SELECT app.* FROM events e JOIN organizations o ON e.organization_id = o.id JOIN app_users app ON o.id = app.organization_id WHERE e.id = :eventId", nativeQuery = true)
    Optional<AppUser> findContactPersonByEventId(long eventId);
}
