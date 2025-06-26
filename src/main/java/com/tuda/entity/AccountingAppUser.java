package com.tuda.entity;

import com.tuda.enums.Role;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.Accessors;

import static com.tuda.entity.AbstractEntity.DEFAULT_GENERATOR;

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

    private Role role;

    private String keyId;
}
