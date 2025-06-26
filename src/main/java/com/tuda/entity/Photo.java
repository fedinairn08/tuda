package com.tuda.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.Accessors;

import java.util.UUID;

import static com.tuda.entity.AbstractEntity.DEFAULT_GENERATOR;

@Accessors(chain = true)
@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "photos")
@SequenceGenerator(name = DEFAULT_GENERATOR, sequenceName = "photos_seq")
public class Photo extends AbstractEntity {
    private UUID uploadId;

    private String filename;
}
