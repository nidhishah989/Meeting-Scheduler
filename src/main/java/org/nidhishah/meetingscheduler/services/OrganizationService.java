package org.nidhishah.meetingscheduler.services;

import org.nidhishah.meetingscheduler.dto.OrganizationDTO;
import org.nidhishah.meetingscheduler.entity.Organization;

import java.util.Optional;

public interface OrganizationService {


    public Organization setOrganization(OrganizationDTO organizationDTO);

    public OrganizationDTO findByOrgName(String organization);
}
