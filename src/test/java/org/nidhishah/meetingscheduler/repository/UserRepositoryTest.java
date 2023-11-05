package org.nidhishah.meetingscheduler.repository;

import org.junit.jupiter.api.Test;
import org.nidhishah.meetingscheduler.dto.TeamMemberDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.List;
import org.springframework.test.annotation.DirtiesContext;

import static org.junit.Assert.assertNotNull;

@SpringBootTest
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void testFindUsersByOrganizationNameAndRole() {
        String organizationName = "meet"; // Replace with the organization name you want to test

        List<Object[]> result = userRepository.findUsersByOrganizationNameAndRole(organizationName);

        // Assert that the result is not null and contains data
        assertNotNull(result);

        // Print the retrieved data for verification
        for (Object[] data : result) {
            System.out.println("First Name: " + data[0]);
            System.out.println("Last Name: " + data[1]);
            System.out.println("Role Name: " + data[2]);
            System.out.println("Org Name: " + data[3]);
        }
    }

    @Test
    public void testGetUsersByOrganizationNameAndRole() {
        String organizationName = "meet"; // Replace with the organization name you want to test

        List<TeamMemberDTO> teamMembers = userRepository.getUsersByOrganizationNameAndRole(organizationName);

        // Assert that the result is not null and contains data
        assertNotNull(teamMembers);

        // Print the retrieved data for verification
        for (TeamMemberDTO teamMember : teamMembers) {
            System.out.println("First Name: " + teamMember.getFirstName());
            System.out.println("Last Name: " + teamMember.getLastName());
            System.out.println("Role Name: " + teamMember.getRoleName());
            System.out.println("Org Name: " + teamMember.getOrgName());
        }
    }
}
