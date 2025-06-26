package com.tuda.entity;

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
@Table(name = "app_users")
@SequenceGenerator(name = DEFAULT_GENERATOR, sequenceName = "app_users_seq")
public class AppUser extends AbstractEntity {
    private String name;

    private String lastName;

    private String patronymic;

    private String login;

    private String password;

    @ManyToOne
    @JoinColumn(name = "organization_id")
    private Organization organization;

    private String phoneNumber;
}
