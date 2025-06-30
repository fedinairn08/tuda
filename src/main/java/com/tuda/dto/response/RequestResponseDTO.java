package com.tuda.dto.response;

import com.tuda.data.entity.AppUser;
import com.tuda.data.entity.Event;
import lombok.Data;

import java.time.LocalDate;

@Data
public class RequestResponseDTO {
    private Long id;

    private AppUser appUser;

    private Event event;

    private boolean status;

    private LocalDate date;
}
