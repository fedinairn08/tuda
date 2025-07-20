package com.tuda.unit.preparer;

import com.tuda.data.enums.EventStatus;
import com.tuda.dto.request.EventRequestDTO;
import com.tuda.dto.request.GuestRequestDTO;
import com.tuda.dto.request.OrganizationRequestDTO;

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

    public static OrganizationRequestDTO getOrganizationRequestDTO() {
        return OrganizationRequestDTO.builder()
                .name("Test Org")
                .phoneNumber("123456789")
                .build();
    }

    public static GuestRequestDTO getGuestRequestDTO() {
        return GuestRequestDTO.builder()
                .event(1L)
                .fullName("Жаров Никита Игоревич")
                .mail("ivanov@mail.ru")
                .status(false)
                .keyId("1")
                .build();

    }
}
