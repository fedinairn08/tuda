package com.tuda.manager;

import com.tuda.data.enums.EventStatus;
import com.tuda.repository.EventRepository;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Component
public class EventStatusManager {

    private final EventRepository eventRepository;
    private final ApplicationContext applicationContext;

    public EventStatusManager(EventRepository eventRepository,
                              ApplicationContext applicationContext) {
        this.eventRepository = eventRepository;
        this.applicationContext = applicationContext;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void onStartup() {
        applicationContext.getBean(EventStatusManager.class).updateExpiredEvents();
    }

    @Scheduled(fixedRate = 10 * 60 * 1000)
    @Transactional
    public void updateExpiredEvents() {
        LocalDateTime now = LocalDateTime.now();
        var events = eventRepository.findAllByDateBeforeAndEventStatusNot(
                now,
                EventStatus.PASSED.toString()
        );

        events.forEach(event -> event.setEventStatus(EventStatus.PASSED));
        eventRepository.saveAll(events);

        events.forEach(event ->
                System.out.println("Updated event status to PASSED: " + event.getId())
        );
    }
}