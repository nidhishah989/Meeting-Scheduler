/*******
 * Organization Service:
 * update organization detail
 * find organization by org name
 * By Nidhi Shah
 */
package org.nidhishah.meetingscheduler.services;

import org.nidhishah.meetingscheduler.dto.OrganizationDTO;

public interface OrganizationService {


    public boolean setOrganizationDetail(OrganizationDTO organizationDTO);

    public OrganizationDTO findByOrgName(String organization);
}
