package com.tuda.dto.request;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@SuperBuilder
@NoArgsConstructor
public class EventRequestDTO {
    private Long organizationId;

    private String city;

    private LocalDateTime date;

    private String title;

    private String description;

    private int participantsNumber;

    private int volunteersNumber;

    private String eventStatus;

    private String filename;

    private UUID uploadId;
}
