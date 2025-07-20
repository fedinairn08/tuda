package com.tuda.data.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

import static com.tuda.data.entity.AbstractEntity.DEFAULT_GENERATOR;

@Accessors(chain = true)
@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Table(name = "guests")
@SequenceGenerator(name = DEFAULT_GENERATOR, sequenceName = "guests_seq")
@EqualsAndHashCode(callSuper = true)
public class Guest extends AbstractEntity {
    @ManyToOne
    @JoinColumn(name = "event_id")
    private Event event;

    private String fullName;

    private String mail;

    private Boolean status;

    private String keyId;
}
