package com.tuda.entity;

import com.tuda.enums.Status;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

import static com.tuda.entity.AbstractEntity.DEFAULT_GENERATOR;

@Accessors(chain = true)
@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "events")
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

    private Status status;

    @OneToOne
    @JoinColumn(name = "photo_id")
    private Photo photo;
}
