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

    public List<TeamMemberDTO> getAvailableTeamMembersForMeetingSchedule(String organization);

    //retrieve user info from user and teammemberextrainfo
    public TeamMemberDTO getTeamMemberInfoById(String Id);

    //perticular provider (Team member) availability with window timeslots for meeting schedule
    public DaysAvailabilityDTO getTeamMemberMeetingAvail(String meetingwindow,String id);




}
