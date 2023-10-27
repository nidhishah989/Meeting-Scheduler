package org.nidhishah.meetingscheduler.services;

import org.nidhishah.meetingscheduler.dto.OrganizationDTO;
import org.nidhishah.meetingscheduler.dto.TeamMemberDTO;

public interface TeamMemberService {

    public void registerAdmin(TeamMemberDTO teamMemberDTO, OrganizationDTO organizationDTO) throws Exception;
}
