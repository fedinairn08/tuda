package com.tuda.service;

import com.tuda.data.entity.Organization;
import com.tuda.dto.request.AppUserRequestDTO;
import com.tuda.dto.request.OrganizationRequestDTO;

public interface OrganizationService {
    Organization addOrganization(OrganizationRequestDTO organizationRequestDTO);
}
