package org.nidhishah.meetingscheduler.services;

import org.junit.jupiter.api.Test;
import org.nidhishah.meetingscheduler.dto.MeetingDTO;
import org.nidhishah.meetingscheduler.dto.TimeSlot;
import org.nidhishah.meetingscheduler.entity.Organization;
import org.nidhishah.meetingscheduler.entity.Role;
import org.nidhishah.meetingscheduler.entity.User;
import org.nidhishah.meetingscheduler.repository.OrganizationRepository;
import org.nidhishah.meetingscheduler.repository.RoleRepository;
import org.nidhishah.meetingscheduler.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class ClientServiceTest {

    @Autowired
    ClientServiceImpl clientService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    OrganizationRepository organizationRepository;

    @Test
    void saveClientMeetingTest() {
        // Setup organization
        Organization organization = new Organization();
        organization.setOrgName("TestOrg");
        organizationRepository.save(organization);

        // Setup client user
        User client = new User();
        client.setUsername("client");
        client.setPassword("password");
        client.setRole(roleRepository.findByRoleName("client"));
        client.setOrganization(organization);
        userRepository.save(client);

        // Setup team member user
        User teamMember = new User();
        teamMember.setUsername("teamMember");
        teamMember.setPassword("password");
        teamMember.setRole(roleRepository.findByRoleName("teammember"));
        teamMember.setOrganization(organization);
        userRepository.save(teamMember);

        // Prepare MeetingDTO
        MeetingDTO meetingDTO = new MeetingDTO();
        meetingDTO.setClientID(client.getId().toString());
        meetingDTO.setTeamMemberId(teamMember.getId().toString());
        meetingDTO.setOrganization(organization.getOrgName());
        meetingDTO.setMeetingDate(String.valueOf(LocalDateTime.now()));
        meetingDTO.setMeetingType("Onsite");
        meetingDTO.setTimeslot(new TimeSlot(LocalTime.of(9,0), LocalTime.of(9,30)));

        // Call the service method
        boolean result = clientService.saveClientMeeting(meetingDTO);

        // Assert the result
        assertTrue(result);
    }
}
