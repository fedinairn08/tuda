package com.tuda.dto.response;

import com.tuda.data.entity.AppUser;
import com.tuda.data.entity.Event;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RequestResponseDTO {
    private Long id;

    private AppUser appUser;

    private Event event;

    private boolean status;

    private LocalDate date;
}
