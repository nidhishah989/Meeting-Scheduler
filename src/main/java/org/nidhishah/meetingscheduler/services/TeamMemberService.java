package org.nidhishah.meetingscheduler.services;

import org.nidhishah.meetingscheduler.dto.NewOrgMemberDTO;
import org.nidhishah.meetingscheduler.dto.OrganizationDTO;
import org.nidhishah.meetingscheduler.dto.TeamMemberDTO;
import org.nidhishah.meetingscheduler.dto.UserDTO;
import java.util.List;

public interface TeamMemberService {

    public void registerAdmin(UserDTO userDTO, OrganizationDTO organizationDTO) throws Exception;

    public List<TeamMemberDTO> getTeamMembersByOrganization(String organization);

    public List<TeamMemberDTO> getTeamMembersByOrgName(String organization);

    public void registerNewTeamMember(NewOrgMemberDTO newOrgMemberDTO, String organization) throws Exception;
}
