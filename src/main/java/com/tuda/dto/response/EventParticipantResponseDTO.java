package com.tuda.dto.response;


import com.tuda.data.enums.ParticipantType;
import com.tuda.data.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EventParticipantResponseDTO {
    private Long id;

    private String fullName;

    private boolean status;

    private UserRole role;

    private ParticipantType type;

}
