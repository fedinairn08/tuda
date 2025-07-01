package com.tuda.converter;

import com.tuda.data.entity.AppUser;
import com.tuda.data.entity.Guest;
import com.tuda.data.entity.Organization;
import com.tuda.dto.request.GuestRequestDTO;
import com.tuda.dto.request.JwtSignUpRequestDTO;
import com.tuda.dto.response.AppUserResponseDTO;
import com.tuda.dto.response.OrganizationResponseDTO;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AppUserConverter {
    private final ModelMapper modelMapper;

    @PostConstruct
    public void register() {
        modelMapper
                .createTypeMap(AppUser.class, AppUserResponseDTO.class)
                .setPostConverter(getConverter());
        modelMapper
                .createTypeMap(JwtSignUpRequestDTO.class, AppUser.class)
                .setConverter(getJwtSignUpRequestToAppUserConverter());
    }

    Converter<AppUser, AppUserResponseDTO> getConverter() {
        return it -> {
            var source = it.getSource();
            var destination = it.getDestination();

            if (source.getOrganization() != null) {
                destination.setOrganization(modelMapper.map(source.getOrganization(), OrganizationResponseDTO.class));
            }

            return destination;
        };
    }

    Converter<JwtSignUpRequestDTO, AppUser> getJwtSignUpRequestToAppUserConverter() {
        return context -> {
            JwtSignUpRequestDTO source = context.getSource();
            AppUser destination = new AppUser();

            destination.setLogin(source.getLogin());
            destination.setPassword(source.getPassword());
            destination.setName(source.getName());
            destination.setLastName(source.getLastName());
            destination.setPatronymic(source.getPatronymic());
            destination.setPhoneNumber(source.getPhoneNumber());
            if (source.getOrganizationId() != null) {
                destination.setOrganization(modelMapper.map(source.getOrganizationId(), Organization.class));
            }

            return destination;
        };
    }
}
