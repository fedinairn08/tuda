package com.tuda.data.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
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
@Table(name = "organizations")
@SequenceGenerator(name = DEFAULT_GENERATOR, sequenceName = "organizations_seq")
@EqualsAndHashCode(callSuper = true)
public class Organization extends AbstractEntity {
    private String name;

    private String phoneNumber;
}
