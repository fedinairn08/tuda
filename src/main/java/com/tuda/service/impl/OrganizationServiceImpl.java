package com.tuda.service.impl;

import com.tuda.data.entity.AppUser;
import com.tuda.data.entity.Organization;
import com.tuda.dto.request.AppUserRequestDTO;
import com.tuda.dto.request.OrganizationRequestDTO;
import com.tuda.exception.NotFoundException;
import com.tuda.repository.OrganizationRepository;
import com.tuda.service.OrganizationService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrganizationServiceImpl implements OrganizationService {
    private final OrganizationRepository organizationRepository;

    @Override
    @Transactional
    public Organization addOrganization(OrganizationRequestDTO organizationRequestDTO) {
        Organization organization = new Organization();
        organization.setName(organizationRequestDTO.getName());
        organization.setPhoneNumber(organizationRequestDTO.getPhoneNumber());
        return organizationRepository.save(organization);
    }

    @Override
    @Transactional
    public Organization updateOrganization(OrganizationRequestDTO organizationRequestDTO, Long organizationId) {
        Organization organization = organizationRepository.findById(organizationId).orElseThrow(() ->
                new NotFoundException(String.format("Organization with id: %s -- is not found", organizationId)));

        organization.setName(organizationRequestDTO.getName())
                .setPhoneNumber(organizationRequestDTO.getPhoneNumber());

        return organizationRepository.save(organization);
    }

    @Override
    public Optional<Organization> getByName(String organizationName) {
        return organizationRepository.findByName(organizationName);
    }
}
