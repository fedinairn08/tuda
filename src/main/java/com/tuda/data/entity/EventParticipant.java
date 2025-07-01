package com.tuda.data.entity;

import com.tuda.data.enums.ParticipantType;
import com.tuda.data.enums.UserRole;
import lombok.*;
import lombok.experimental.Accessors;


@Accessors(chain = true)
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class EventParticipant {
    private Long id;

    private String fullName;

    private boolean status;

    private UserRole role;

    private ParticipantType type;
}
