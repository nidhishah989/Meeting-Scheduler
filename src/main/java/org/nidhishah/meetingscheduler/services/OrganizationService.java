package org.nidhishah.meetingscheduler.services;

import org.nidhishah.meetingscheduler.dto.OrganizationDTO;

public interface OrganizationService {


    public boolean setOrganizationDetail(OrganizationDTO organizationDTO);

    public OrganizationDTO findByOrgName(String organization);
}
