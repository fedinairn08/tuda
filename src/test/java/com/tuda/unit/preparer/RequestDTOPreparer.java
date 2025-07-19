package com.tuda.unit.preparer;

import com.tuda.data.enums.EventStatus;
import com.tuda.dto.request.EventRequestDTO;

import java.time.LocalDateTime;
import java.util.UUID;

public class RequestDTOPreparer {
    public static EventRequestDTO getEventRequestDTO(EventStatus eventStatus, String fileName, UUID photoUUID) {
        return EventRequestDTO.builder()
                .organizationId(1L)
                .city("Moscow")
                .date(LocalDateTime.now())
                .title("FunFest")
                .description("Some description")
                .participantsNumber(10)
                .volunteersNumber(10)
                .eventStatus(eventStatus.toString())
                .filename(fileName)
                .uploadId(photoUUID).build();
    }
}
