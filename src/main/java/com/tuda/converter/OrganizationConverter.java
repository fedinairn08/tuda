package com.tuda.converter;

import com.tuda.data.entity.AppUser;
import com.tuda.data.entity.Organization;
import com.tuda.dto.request.JwtSignUpRequestDTO;
import com.tuda.dto.response.AppUserResponseDTO;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrganizationConverter {
    private final ModelMapper modelMapper;

    @PostConstruct
    public void register() {
        modelMapper.typeMap(JwtSignUpRequestDTO.class, Organization.class)
                .addMappings(mapper -> mapper.skip(Organization::setId));
    }
}
