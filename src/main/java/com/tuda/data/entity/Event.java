package com.tuda.data.entity;

import com.tuda.data.enums.EventStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

import static com.tuda.data.entity.AbstractEntity.DEFAULT_GENERATOR;

@SuperBuilder
@Accessors(chain = true)
@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "events", indexes = {
        @Index(name = "idx_event_date_status", columnList = "date, event_status")
})
@SequenceGenerator(name = DEFAULT_GENERATOR, sequenceName = "events_seq")
public class Event extends AbstractEntity {
    @ManyToOne
    @JoinColumn(name = "organization_id")
    private Organization organization;

    private String city;

    private LocalDateTime date;

    private String title;

    private String description;

    private int participantsNumber;

    private int volunteersNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "event_status")
    private EventStatus eventStatus;

    @OneToOne
    @JoinColumn(name = "photo_id")
    private Photo photo;
}
