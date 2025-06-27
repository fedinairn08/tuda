package com.tuda.converter;

import com.tuda.dto.response.EventResponseDTO;
import com.tuda.dto.response.OrganizationResponseDTO;
import com.tuda.dto.response.PhotoResponseDTO;
import com.tuda.entity.Event;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EventConverter {

    private final ModelMapper modelMapper;

    @PostConstruct
    public void register() {
        modelMapper
                .createTypeMap(Event.class, EventResponseDTO.class)
                .setPostConverter(getConverter());
    }

    Converter<Event, EventResponseDTO> getConverter() {
        return it -> {
            var source = it.getSource();
            var destination = it.getDestination();

            if (source.getOrganization() != null) {
                destination.setOrganization(modelMapper.map(source.getOrganization(), OrganizationResponseDTO.class));
            }

            if (source.getPhoto() != null) {
                destination.setPhoto(modelMapper.map(source.getPhoto(), PhotoResponseDTO.class));
            }

            return destination;
        };
    }
}
