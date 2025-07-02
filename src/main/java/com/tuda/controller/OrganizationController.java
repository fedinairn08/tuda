package com.tuda.controller;

import com.tuda.data.entity.Organization;
import com.tuda.dto.ApiResponse;
import com.tuda.dto.request.OrganizationRequestDTO;
import com.tuda.dto.response.OrganizationResponseDTO;
import com.tuda.service.OrganizationService;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/organization")
public class OrganizationController extends EntityController<Organization> {
    private final OrganizationService organizationService;

    private static final Class<OrganizationResponseDTO> ORGANIZATION_RESPONSE_DTO_CLASS = OrganizationResponseDTO.class;

    public OrganizationController(ModelMapper modelMapper, OrganizationService organizationService) {
        super(modelMapper);
        this.organizationService = organizationService;
    }

    @PostMapping("/add")
    public ResponseEntity<ApiResponse<OrganizationResponseDTO>> addOrganization(@RequestBody OrganizationRequestDTO requestDTO) {
        Organization organization = organizationService.addOrganization(requestDTO);
        OrganizationResponseDTO dto = serialize(organization, ORGANIZATION_RESPONSE_DTO_CLASS);
        return ResponseEntity.ok(new ApiResponse<>(dto));
    }
}
