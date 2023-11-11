package org.nidhishah.meetingscheduler.services;

import org.nidhishah.meetingscheduler.dto.*;

import java.util.List;

public interface TeamMemberService {

    public void registerAdmin(UserDTO userDTO, OrganizationDTO organizationDTO) throws Exception;

    public List<TeamMemberDTO> getTeamMembersByOrganization(String organization);

    public List<TeamMemberDTO> getTeamMembersByOrgName(String organization);

    public void registerNewTeamMember(NewOrgMemberDTO newOrgMemberDTO, String organization) throws Exception;

    public String completeUserSignUpProcess(SignUPDTO signUPDTO);

    //set teammemberAvailability dto (dayAvailabilityDTO) with defaults timeslots for each day
    public DaysAvailabilityDTO getTeamMemberAvailability(String username,String organization);

    public boolean setTeamMemberAvailability(Long id, TeamMemberDTO teamMemberDTO, DaysAvailabilityDTO daysAvailabilityDTO);
}
