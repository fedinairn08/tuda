package com.tuda.dto.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class EventResponseDTO {
    private Long id;

    private OrganizationResponseDTO organization;

    private String city;

    private LocalDateTime date;

    private String title;

    private String description;

    private int participantsNumber;

    private int volunteersNumber;

    private String eventStatus;

    private PhotoResponseDTO photo;
}
