package org.nidhishah.meetingscheduler.services;

import org.junit.jupiter.api.*;
import org.nidhishah.meetingscheduler.entity.Organization;
import org.nidhishah.meetingscheduler.entity.Role;
import org.nidhishah.meetingscheduler.entity.User;
import org.nidhishah.meetingscheduler.repository.OrganizationRepository;
import org.nidhishah.meetingscheduler.repository.RoleRepository;
import org.nidhishah.meetingscheduler.repository.UserRepository;
import org.nidhishah.meetingscheduler.security.UserPrincipal;
import org.nidhishah.meetingscheduler.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserServiceTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    OrganizationRepository organizationRepository;

    @Autowired
    UserService userService;

    @Autowired
    BCryptPasswordEncoder encoder;

    @Test
    void testFindUserByEmailAndOrganization() {
        //first set user
        Organization organization = new Organization();
        organization.setOrgName("testOrg3");
        organizationRepository.save(organization);
        // Create a user using the UserRepository
        User user = new User();
        user.setUsername("admin");
        user.setEmail("admin@myorg.com");
        user.setOrganization(organization);
        // Set other details
        user.setPassword(encoder.encode("12"));

        // Retrieve role
        Role role = roleRepository.findByRoleName("admin");
        user.setRole(role);

        userRepository.save(user);
        // now let's test the service method
        Assertions.assertTrue(userService.findUserByEmailAndOrganization("admin@myorg.com","testOrg3"));
    }


}
