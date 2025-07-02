package com.tuda.repository;

import com.tuda.data.entity.Guest;
import com.tuda.data.entity.Organization;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrganizationRepository extends JpaRepository<Organization, Long> {

}
