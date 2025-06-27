package com.tuda.converter;

import com.tuda.dto.response.AccountingAppUserResponseDTO;
import com.tuda.dto.response.AppUserResponseDTO;
import com.tuda.dto.response.EventResponseDTO;
import com.tuda.entity.AccountingAppUser;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AccountingAppUserConverter {

    private final ModelMapper modelMapper;

    @PostConstruct
    public void register() {
        modelMapper
                .createTypeMap(AccountingAppUser.class, AccountingAppUserResponseDTO.class)
                .setPostConverter(getConverter());
    }

    Converter<AccountingAppUser, AccountingAppUserResponseDTO> getConverter() {
        return it -> {
            var source = it.getSource();
            var destination = it.getDestination();

            if (source.getEvent() != null) {
                destination.setEvent(modelMapper.map(source.getEvent(), EventResponseDTO.class));
            }

            if (source.getAppUser() != null) {
                destination.setAppUser(modelMapper.map(source.getAppUser(), AppUserResponseDTO.class));
            }

            return destination;
        };
    }
}
