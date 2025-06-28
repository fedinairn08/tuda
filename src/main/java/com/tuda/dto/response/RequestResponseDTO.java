package com.tuda.dto.response;

import com.tuda.entity.AppUser;
import com.tuda.entity.Event;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
