package com.tuda.data.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

import static com.tuda.data.entity.AbstractEntity.DEFAULT_GENERATOR;

@Accessors(chain = true)
@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Table(name = "photos")
@EqualsAndHashCode(callSuper = true)
@SequenceGenerator(name = DEFAULT_GENERATOR, sequenceName = "photos_seq")
public class Photo extends AbstractEntity {
    private UUID uploadId;

    private String filename;
}
