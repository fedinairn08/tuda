package com.tuda.dto.request;

import com.tuda.entity.AppUser;
import com.tuda.entity.Event;
import lombok.Data;

import java.time.LocalDate;

@Data
public class RequestRequestDTO {
    private AppUser appUser;

    private Event event;

    private boolean status;

    private LocalDate date;
}
