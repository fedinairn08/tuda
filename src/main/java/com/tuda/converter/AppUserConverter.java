package com.tuda.converter;

import com.tuda.dto.response.AppUserResponseDTO;
import com.tuda.dto.response.OrganizationResponseDTO;
import com.tuda.entity.AppUser;
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
}
