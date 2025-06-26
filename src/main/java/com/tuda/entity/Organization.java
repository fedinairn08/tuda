package com.tuda.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.Accessors;

import static com.tuda.entity.AbstractEntity.DEFAULT_GENERATOR;

@Accessors(chain = true)
@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "organizations")
@SequenceGenerator(name = DEFAULT_GENERATOR, sequenceName = "organizations_seq")
public class Organization extends AbstractEntity {
    private String name;

    private String phoneNumber;
}
