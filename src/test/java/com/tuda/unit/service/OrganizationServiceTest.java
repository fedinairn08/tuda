package com.tuda.unit.service;

import com.tuda.data.entity.Organization;
import com.tuda.dto.request.OrganizationRequestDTO;
import com.tuda.exception.NotFoundException;
import com.tuda.repository.OrganizationRepository;
import com.tuda.service.impl.OrganizationServiceImpl;
import com.tuda.unit.preparer.EntityPreparer;
import com.tuda.unit.preparer.RequestDTOPreparer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrganizationServiceTest {

    @Mock
    private OrganizationRepository organizationRepository;

    @InjectMocks
    private OrganizationServiceImpl organizationService;

    @Test
    void addOrganization_shouldSaveAndReturnOrganization() {
        // given
        OrganizationRequestDTO dto = RequestDTOPreparer.getOrganizationRequestDTO();

        Organization saved = new Organization();
        saved.setName(dto.getName());
        saved.setPhoneNumber(dto.getPhoneNumber());

        when(organizationRepository.save(any(Organization.class))).thenReturn(saved);

        // when
        Organization result = organizationService.addOrganization(dto);

        // then
        assertNotNull(result);
        assertEquals("Test Org", result.getName());
        assertEquals("123456789", result.getPhoneNumber());
        verify(organizationRepository).save(any(Organization.class));
    }

    @Test
    void updateOrganization_shouldUpdateAndReturnOrganization() {
        // given
        Long orgId = 1L;

        OrganizationRequestDTO dto = RequestDTOPreparer.getOrganizationRequestDTO();

        Organization existing = new Organization();
        existing.setId(orgId);
        existing.setName("Old Org");
        existing.setPhoneNumber("000");

        when(organizationRepository.findById(orgId)).thenReturn(Optional.of(existing));
        when(organizationRepository.save(any(Organization.class))).thenReturn(existing);

        // when
        Organization result = organizationService.updateOrganization(dto, orgId);

        // then
        assertNotNull(result);
        assertEquals("Test Org", result.getName());
        assertEquals("123456789", result.getPhoneNumber());
        verify(organizationRepository).findById(orgId);
        verify(organizationRepository).save(existing);
    }

    @Test
    void updateOrganization_notFound_shouldThrowException() {
        // given
        Long orgId = 2L;
        OrganizationRequestDTO dto = RequestDTOPreparer.getOrganizationRequestDTO();


        when(organizationRepository.findById(orgId)).thenReturn(Optional.empty());

        // when
        NotFoundException ex = assertThrows(NotFoundException.class, () ->
                organizationService.updateOrganization(dto, orgId));

        // then
        assertTrue(ex.getMessage().contains("Organization with id: 2"));
        verify(organizationRepository).findById(orgId);
        verify(organizationRepository, never()).save(any());
    }

    @Test
    void getByName_shouldReturnOptional() {
        // given
        Organization org = EntityPreparer.getTestOrganization();

        when(organizationRepository.findByName(org.getName())).thenReturn(Optional.of(org));

        // when
        Optional<Organization> result = organizationService.getByName(org.getName());

        // then
        assertTrue(result.isPresent());
        assertEquals(org.getName(), result.get().getName());
        verify(organizationRepository).findByName(org.getName());
    }
}
