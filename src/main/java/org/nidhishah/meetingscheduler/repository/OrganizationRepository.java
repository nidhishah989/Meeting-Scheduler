package org.nidhishah.meetingscheduler.repository;

import org.nidhishah.meetingscheduler.entity.Organization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrganizationRepository extends JpaRepository <Organization, Long> {

    public Optional<Organization> findByOrgName(String orgName);

}
