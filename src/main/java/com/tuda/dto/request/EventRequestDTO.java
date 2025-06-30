package com.tuda.dto.request;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
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

    private UUID uuid;
}
