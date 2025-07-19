package com.tuda.data.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

import static com.tuda.data.entity.AbstractEntity.DEFAULT_GENERATOR;

@Accessors(chain = true)
@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Table(name = "requests")
@SequenceGenerator(name = DEFAULT_GENERATOR, sequenceName = "requests_seq")
@EqualsAndHashCode(callSuper = true)
public class Request extends AbstractEntity {
    @ManyToOne
    @JoinColumn(name = "app_user_id")
    private AppUser appUser;

    @ManyToOne
    @JoinColumn(name = "event_id")
    private Event event;

    private boolean status;

    private LocalDate date;
}
