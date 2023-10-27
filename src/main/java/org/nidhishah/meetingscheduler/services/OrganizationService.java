package org.nidhishah.meetingscheduler.services;

import org.nidhishah.meetingscheduler.dto.OrganizationDTO;
import org.nidhishah.meetingscheduler.entity.Organization;

public interface OrganizationService {


    public Organization setOrganization(String organization);

    public OrganizationDTO findByOrgName(String organization);
}
