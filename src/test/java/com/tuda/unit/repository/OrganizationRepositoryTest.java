package com.tuda.unit.repository;

import com.tuda.data.entity.Organization;
import com.tuda.repository.OrganizationRepository;
import com.tuda.unit.preparer.EntityPreparer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class OrganizationRepositoryTest {

    @Autowired
    private OrganizationRepository organizationRepository;

    @Test
    @DisplayName("findByName должен находить организацию по имени")
    void findByName_shouldReturnOrganization() {
        // given
        Organization org = EntityPreparer.getTestOrganization();
        org.setId(null);
        organizationRepository.save(org);

        // when
        Optional<Organization> result = organizationRepository.findByName("Ventum");

        // then
        assertTrue(result.isPresent());
        assertEquals("+79156745656", result.get().getPhoneNumber());
    }

    @Test
    @DisplayName("findByName должен вернуть пустой Optional, если не найдено")
    void findByName_shouldReturnEmpty_ifNotFound() {
        Optional<Organization> result = organizationRepository.findByName("No Such Org");
        assertTrue(result.isEmpty());
    }
}
