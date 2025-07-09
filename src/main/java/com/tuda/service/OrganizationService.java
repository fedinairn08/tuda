package com.tuda.service;

import com.tuda.data.entity.Organization;
import com.tuda.dto.request.AppUserRequestDTO;
import com.tuda.dto.request.OrganizationRequestDTO;

import java.util.Optional;

public interface OrganizationService {
    Organization addOrganization(OrganizationRequestDTO organizationRequestDTO);
    Organization updateOrganization(OrganizationRequestDTO organizationRequestDTO, Long organizationId);
    Optional<Organization> getByName(String organizationName);
}
