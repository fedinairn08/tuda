package com.tuda.data.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

import static com.tuda.data.entity.AbstractEntity.DEFAULT_GENERATOR;

@Accessors(chain = true)
@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Table(name = "app_users")
@SequenceGenerator(name = DEFAULT_GENERATOR, sequenceName = "app_users_seq")
@EqualsAndHashCode(callSuper = true)
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

    public String getFullName() {
        return this.lastName + " " + this.name + " " + this.patronymic;
    }
}
