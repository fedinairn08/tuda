package com.tuda.unit.repository;

import com.tuda.data.entity.AccountingAppUser;
import com.tuda.data.entity.AppUser;
import com.tuda.data.entity.Event;
import com.tuda.data.enums.UserRole;
import com.tuda.repository.EventRepository;
import com.tuda.unit.preparer.EntityPreparer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@DataJpaTest
public class EventRepositoryTest {
    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    @Test
    void whenGetUserId_thenReturnUserEventList() {
        Event event = EntityPreparer.getTestEvent();
        event.setId(null);
        event.getOrganization().setId(null);
        testEntityManager.persist(event.getOrganization());
        testEntityManager.persist(event.getPhoto());
        event = testEntityManager.persist(event);

        AppUser appUser = EntityPreparer.getAppUser(null);
        appUser.setId(null);
        appUser = testEntityManager.persist(appUser);

        AccountingAppUser accountingAppUser =
                EntityPreparer.getTestAccountingAppUser(event, UserRole.PARTICIPANT, appUser, false);
        accountingAppUser.setId(null);

        List<Event> expectedEvents = List.of(event);

        List<Event> actualEvents = eventRepository.getEventsByUserId(appUser.getId());

        assertThat(actualEvents)
                .hasSize(expectedEvents.size())
                .containsExactlyInAnyOrderElementsOf(expectedEvents);
    }

    @Test
    void whenGetUserId_thenReturnEmptyUserEventList() {
        AppUser appUser = EntityPreparer.getAppUser(null);
        appUser.setId(null);
        appUser = testEntityManager.persist(appUser);

        Long eventListSize = 0L;
        List<Event> actualEvents = eventRepository.getEventsByUserId(appUser.getId());
        assertEquals(eventListSize, actualEvents.size());
    }

    @Test
    void whenGetNotExistedUserId_thenReturnEmptyUserEventList() {
        Long notExistedUserId = 0L;
        List<Event> actualEvents = eventRepository.getEventsByUserId(notExistedUserId);
        Long eventListSize = 0L;
        assertEquals(eventListSize, actualEvents.size());
    }
}
