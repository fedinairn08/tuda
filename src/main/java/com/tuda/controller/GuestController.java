package com.tuda.controller;

import com.tuda.dto.ApiResponse;
import com.tuda.dto.request.GuestRequestDTO;
import com.tuda.dto.response.GuestResponseDTO;
import com.tuda.dto.response.RequestResponseDTO;
import com.tuda.entity.Event;
import com.tuda.entity.Guest;
import com.tuda.entity.Request;
import com.tuda.service.GuestService;
import com.tuda.service.RequestService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/guest")
@RestController
public class GuestController extends EntityController<Guest> {
    private final GuestService guestService;

    private static final Class<GuestResponseDTO> GUEST_RESPONSE_DTO_CLASS = GuestResponseDTO.class;

    public GuestController(ModelMapper modelMapper, GuestService guestService) {
        super(modelMapper);
        this.guestService = guestService;
    }

    @PostMapping("/add")
    public ResponseEntity<ApiResponse<GuestResponseDTO>> addGuest(@RequestBody GuestRequestDTO guestRequestDTO) {
        Guest guest = guestService.addGuest(guestRequestDTO);
        GuestResponseDTO dto = serialize(guest, GUEST_RESPONSE_DTO_CLASS);
        return ResponseEntity.ok(new ApiResponse<>(dto));
    }
}
