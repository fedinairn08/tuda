package com.tuda.dto.response;


import com.tuda.data.enums.ParticipantType;
import com.tuda.data.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EventParticipantResponseDTO {
    private Long id;

    private String fullName;

    private Boolean status;

    private UserRole role;

    private ParticipantType type;

}
