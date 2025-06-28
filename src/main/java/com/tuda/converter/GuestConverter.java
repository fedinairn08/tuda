package com.tuda.converter;


import com.tuda.dto.request.EventRequestDTO;
import com.tuda.dto.request.GuestRequestDTO;
import com.tuda.dto.response.EventResponseDTO;
import com.tuda.dto.response.GuestResponseDTO;
import com.tuda.dto.response.OrganizationResponseDTO;
import com.tuda.dto.response.PhotoResponseDTO;
import com.tuda.entity.Event;
import com.tuda.entity.Guest;
import com.tuda.exception.NotFoundException;
import com.tuda.repository.EventRepository;
import com.tuda.repository.GuestRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GuestConverter {
    private final ModelMapper modelMapper;
    private final EventRepository eventRepository;

    @PostConstruct
    public void register() {
        modelMapper
                .createTypeMap(Guest.class, GuestResponseDTO.class)
                .setPostConverter(getConverter());
        modelMapper
                .createTypeMap(GuestRequestDTO.class, Guest.class)
                .setConverter(getRequestToGuestConverter());
    }

    Converter<Guest, GuestResponseDTO> getConverter() {
        return it -> {
            var source = it.getSource();
            var destination = it.getDestination();

            if (source.getEvent() != null) {
                destination.setEvent(modelMapper.map(source.getEvent(), EventResponseDTO.class));
            }

            return destination;
        };
    }

    Converter<GuestRequestDTO, Guest> getRequestToGuestConverter() {
        return ctx -> {
            GuestRequestDTO source = ctx.getSource();
            Guest guest = new Guest();

            if (source.getEvent() != null) {
                Event event = eventRepository.findById(source.getEvent())
                        .orElseThrow(() -> new NotFoundException("Event не найден"));
                guest.setEvent(event);
            }

            guest.setFullName(source.getFullName());
            guest.setMail(source.getMail());
            guest.setStatus(source.getStatus());
            guest.setKeyId(source.getKeyId());

            return guest;
        };
    }
}
