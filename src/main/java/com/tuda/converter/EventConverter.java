package com.tuda.converter;

import com.tuda.data.entity.Event;
import com.tuda.dto.request.EventRequestDTO;
import com.tuda.dto.response.EventResponseDTO;
import com.tuda.dto.response.OrganizationResponseDTO;
import com.tuda.dto.response.PhotoResponseDTO;
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
        modelMapper.typeMap(EventRequestDTO.class, Event.class)
                .addMappings(mapper -> mapper.skip(Event::setId));

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
