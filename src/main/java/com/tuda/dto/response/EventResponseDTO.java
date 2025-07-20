package com.tuda.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
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
