package com.tuda.controller;

import com.tuda.data.entity.Guest;
import com.tuda.dto.ApiResponse;
import com.tuda.dto.request.GuestRequestDTO;
import com.tuda.dto.response.GuestResponseDTO;
import com.tuda.service.GuestService;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
