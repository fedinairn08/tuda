package com.tuda.controller;

import com.tuda.data.entity.Request;
import com.tuda.dto.ApiResponse;
import com.tuda.dto.response.RequestResponseDTO;
import com.tuda.service.RequestService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/request")
@RestController
@SecurityRequirement(name = "JWT")
public class RequestController extends EntityController<Request>{
    private final RequestService requestService;

    private static final Class<RequestResponseDTO> REQUEST_RESPONSE_DTO_CLASS = RequestResponseDTO.class;

    public RequestController(ModelMapper modelMapper, RequestService requestService) {
        super(modelMapper);
        this.requestService = requestService;
    }

    @GetMapping("/getAll")
    public ResponseEntity<ApiResponse<List<RequestResponseDTO>>> getAllEventRequests(Long eventId) {
        List<Request> requests = requestService.getAllEventRequests(eventId);
        List<RequestResponseDTO> dtos = serialize(requests, REQUEST_RESPONSE_DTO_CLASS);
        return ResponseEntity.ok(new ApiResponse<>(dtos));
    }

    @PostMapping("/add")
    public ResponseEntity<ApiResponse<RequestResponseDTO>> addRequest(@RequestParam Long eventId, String userLogin) {
        Request request = requestService.addRequest(eventId, userLogin);
        RequestResponseDTO dto = serialize(request, REQUEST_RESPONSE_DTO_CLASS);
        return ResponseEntity.ok(new ApiResponse<>(dto));
    }

    @DeleteMapping("/refuseToVolunteering")
    public ResponseEntity<ApiResponse<String>> deleteRequest(@RequestParam Long eventId, String userLogin) {
        requestService.deleteRequest(eventId, userLogin);
        return ResponseEntity.ok(new ApiResponse<>("Volunteering request successfully deletes"));
    }

    @PostMapping("/rejectVolunteerForEventById")
    public ResponseEntity<ApiResponse<RequestResponseDTO>> rejectRequest(@RequestParam Long eventId, String userLogin) {
        Request request = requestService.rejectRequest(eventId, userLogin);
        RequestResponseDTO dto = serialize(request, REQUEST_RESPONSE_DTO_CLASS);
        return ResponseEntity.ok(new ApiResponse<>(dto));
    }
}
