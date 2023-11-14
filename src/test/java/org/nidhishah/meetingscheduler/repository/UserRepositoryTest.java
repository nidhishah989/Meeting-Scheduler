package org.nidhishah.meetingscheduler.repository;

import org.junit.jupiter.api.*;
import org.nidhishah.meetingscheduler.dto.TeamMemberDTO;
import org.nidhishah.meetingscheduler.entity.Organization;
import org.nidhishah.meetingscheduler.entity.Role;
import org.nidhishah.meetingscheduler.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.List;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;
import org.nidhishah.meetingscheduler.dto.ClientDTO;

import static org.junit.Assert.assertNotNull;

@SpringBootTest
@Transactional
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    OrganizationRepository organizationRepository;

    // Testing saving methods of userrepository
    @BeforeEach
    public void Setup(){
        //first set organization
        Organization testOrganization = new Organization();
        testOrganization.setOrgName("testOrg2");
        testOrganization.setOrgDescription("Test Description");
        testOrganization.setOrgAddress1("123 Test St");
        testOrganization.setOrgAddress2("Suite 100");
        testOrganization.setOrgCity("Test City");
        testOrganization.setOrgState("Test State");
        testOrganization.setOrgCountry("Test Country");
        testOrganization.setOrgContact("1234567890");
        organizationRepository.save(testOrganization);
        //get all role instances
        Role adminRole = roleRepository.findByRoleName("admin");
        Role teamMemberRole = roleRepository.findByRoleName("teammember");
        Role clientRole = roleRepository.findByRoleName("client");

        //save admin, teammember, client of test organization
        User admin = new User();
        admin.setUsername("admin");
        admin.setPassword("admin");
        admin.setRole(adminRole);
        admin.setFirstName("admin");
        admin.setLastName("lastname");
        admin.setEmail("admin@test.com");
        admin.setEnabled(true);
        admin.setOrganization(testOrganization);
        userRepository.save(admin);

        //teammember 1
        User teamMember1 = new User();
        teamMember1.setUsername("team1");
        teamMember1.setPassword("password");
        teamMember1.setRole(teamMemberRole);
        teamMember1.setFirstName("team1");
        teamMember1.setLastName("test");
        teamMember1.setEmail("team1@test.com");
        teamMember1.setEnabled(true);
        teamMember1.setOrganization(testOrganization);
        userRepository.save(teamMember1);

        //teammember 2
        User teamMember2 = new User();
        teamMember2.setUsername("team2");
        teamMember2.setPassword("password");
        teamMember2.setRole(teamMemberRole);
        teamMember2.setFirstName("team2");
        teamMember2.setLastName("test");
        teamMember2.setEmail("team2@test.com");
        teamMember2.setEnabled(true);
        teamMember2.setOrganization(testOrganization);
        userRepository.save(teamMember2);

        //client 1
        User client1 = new User();
        client1.setUsername("client1");
        client1.setPassword("password");
        client1.setRole(clientRole);
        client1.setFirstName("client1");
        client1.setLastName("test");
        client1.setEmail("client1@test.com");
        client1.setEnabled(true);
        client1.setOrganization(testOrganization);
        userRepository.save(client1);
    }

    /* find user by username and organization name
    *   used in loadUserByUsername to let user looged in by organization
    *   getting admin information
     */
    @Test
    public void findByUsernameAndOrganizationOrgNameTest(){

        User currentUser = userRepository.findByUsernameAndOrganizationOrgName
                            ("admin","testOrg2");
        //check user is there
        Assertions.assertNotNull(currentUser);
        //check email address match
        Assertions.assertEquals("admin@test.com",currentUser.getEmail());
    }

    //Now as we already have client set before.. this will provide me information by email and orgname
    @Test
    public void testfindUserByEmailAndOrganization(){
        User currentUser = userRepository.findUserByEmailAndOrganization
                ("client1@test.com","testOrg2");
        //check user is there
        Assertions.assertNotNull(currentUser);
        //check email address match
        Assertions.assertEquals("client1@test.com",currentUser.getEmail());
    }
    //mostly used to check the teammember or admin is there and for an organization and they are active
    // there should be three entires, match their org name and isEnable true and role within admin, teammember
    @Test
    public void testFindUsersByOrganizationNameAndRole() {
        String organizationName = "testOrg2";
        // Execute the method to fetch users
        List<Object[]> usersByOrganizationNameAndRole = userRepository.findUsersByOrganizationNameAndRole(organizationName);

        // Validate the results
        Assertions.assertEquals(3, usersByOrganizationNameAndRole.size());

        for (Object[] user : usersByOrganizationNameAndRole) {
            String roleName = (String) user[2];
            String orgName = (String) user[3];
            boolean isEnabled = (boolean) user[5];

            Assertions.assertTrue(isEnabled);
            Assertions.assertTrue(roleName.equals("admin") || roleName.equals("teammember"));
            Assertions.assertEquals(organizationName, orgName);
        }
    }

    //check client list fetched in clientDTO
    @Test
    public void testGetUsersByOrgNameAndRole() {

        String organizationName = "testOrg2";

        List<ClientDTO> usersByRole = userRepository.getUsersByOrgNameAndRole(organizationName);

        Assertions.assertEquals(1, usersByRole.size());

        for (ClientDTO user : usersByRole) {
            Assertions.assertEquals("client", user.getRoleName());
            Assertions.assertEquals(organizationName, user.getOrgName());
        }
    }

}
