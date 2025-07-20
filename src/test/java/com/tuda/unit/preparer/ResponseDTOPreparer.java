package com.tuda.unit.preparer;

import com.tuda.data.entity.Event;
import com.tuda.data.enums.EventStatus;
import com.tuda.dto.response.EventResponseDTO;
import com.tuda.dto.response.OrganizationResponseDTO;
import com.tuda.dto.response.PhotoResponseDTO;

public class ResponseDTOPreparer {
    public static EventResponseDTO getTestEventResponseDTO(Event testEvent) {
        OrganizationResponseDTO organizationResponseDTO = OrganizationResponseDTO.builder()
                .id(1L)
                .name("Ventum")
                .phoneNumber("+79156745656").build();
        PhotoResponseDTO photoResponseDTO = PhotoResponseDTO.builder()
                .uploadId(testEvent.getPhoto().getUploadId())
                .filename("funFest.png").build();
        return EventResponseDTO.builder()
                .id(1L)
                .organization(organizationResponseDTO)
                .city("Moscow")
                .date(testEvent.getDate())
                .title("FunFest")
                .description("Some description")
                .participantsNumber(10)
                .volunteersNumber(10)
                .eventStatus(EventStatus.WILL.toString())
                .photo(photoResponseDTO).build();
    }
}
