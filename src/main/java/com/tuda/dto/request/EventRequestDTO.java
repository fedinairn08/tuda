package com.tuda.dto.request;

import com.tuda.entity.Organization;
import com.tuda.entity.Photo;
import com.tuda.enums.EventStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class EventRequestDTO {
    private Long organization;

    private String city;

    private LocalDateTime date;

    private String title;

    private String description;

    private int participantsNumber;

    private int volunteersNumber;

    private String eventStatus;

    private Long photo;
}
