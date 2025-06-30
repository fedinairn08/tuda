package com.tuda.data.entity;

import com.tuda.data.enums.UserRole;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.Accessors;

import static com.tuda.data.entity.AbstractEntity.DEFAULT_GENERATOR;

@Accessors(chain = true)
@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "accounting_app_users")
@SequenceGenerator(name = DEFAULT_GENERATOR, sequenceName = "accounting_app_users_seq")
public class AccountingAppUser extends AbstractEntity {
    @ManyToOne
    @JoinColumn(name = "event_id")
    private Event event;

    @ManyToOne
    @JoinColumn(name = "app_user_id")
    private AppUser appUser;

    private boolean status;

    private UserRole userRole;

    private String keyId;
}
