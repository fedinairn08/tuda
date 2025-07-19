package com.tuda.unit.preparer;

import com.tuda.data.entity.*;
import com.tuda.data.enums.EventStatus;
import com.tuda.data.enums.UserRole;

import java.time.LocalDateTime;
import java.util.UUID;

public class EntityPreparer {
    public static Event getTestEvent(Photo photo, Organization organization) {
        return Event.builder()
                .id(1L)
                .organization(organization)
                .city("Moscow")
                .date(LocalDateTime.now())
                .title("FunFest")
                .description("Some description")
                .participantsNumber(10)
                .volunteersNumber(10)
                .eventStatus(EventStatus.WILL)
                .photo(photo).build();
    }

    public static Photo getTestPhoto() {
        return Photo.builder()
                .id(1L)
                .uploadId(UUID.randomUUID())
                .filename("funFest.png").build();
    }

    public static Organization getTestOrganization() {
        return Organization.builder()
                .id(1L)
                .name("Ventum")
                .phoneNumber("+79156745656").build();
    }

    public static AppUser getAppUser(Organization organization) {
        return AppUser.builder()
                .id(1L)
                .name("Ivan")
                .lastName("Ivanov")
                .patronymic("Ivanovich")
                .login("ivanov@mail.ru")
                .password("1")
                .organization(organization)
                .phoneNumber("+79456788765")
                .build();
    }

    public static AccountingAppUser getTestAccountingAppUser(Event testEvent, UserRole userRole,
                                                             AppUser appUser, boolean status) {
        return AccountingAppUser.builder()
                .id(1L)
                .event(testEvent)
                .appUser(appUser)
                .status(status)
                .userRole(userRole)
                .keyId("87384").build();

    }


}
