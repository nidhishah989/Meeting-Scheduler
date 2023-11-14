package org.nidhishah.meetingscheduler.services;

import org.junit.jupiter.api.*;
import org.nidhishah.meetingscheduler.dto.NewOrgMemberDTO;
import org.nidhishah.meetingscheduler.dto.SignUPDTO;
import org.nidhishah.meetingscheduler.entity.Organization;
import org.nidhishah.meetingscheduler.entity.User;
import org.nidhishah.meetingscheduler.repository.OrganizationRepository;
import org.nidhishah.meetingscheduler.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TeamMemberServiceTest {

    @Autowired
    OrganizationRepository organizationRepository;

    @Autowired
    TeamMemberServiceImpl teamMemberService;

    @Autowired
    UserRepository userRepository;

//    @Test
//    @Order(1)
//    void setUpOrg()
//    {
//        // set organization for testing
//        Organization organization = new Organization();
//        organization.setOrgName("testingorg");
//        organizationRepository.save(organization);
//    }

    @Test
    @Order(2)
    void testRegisterNewTeamMember(){
        // set organization for testing
        Organization organization = new Organization();
        organization.setOrgName("testingorg");
        organizationRepository.save(organization);
        // new org team member DTO
        NewOrgMemberDTO newOrgMemberDTO = new NewOrgMemberDTO();
        newOrgMemberDTO.setEmail("teamtest@torg.test");
        newOrgMemberDTO.setFirstName("team1test");
        newOrgMemberDTO.setLastName("lastname");
        newOrgMemberDTO.setRoleName("teammember");

        Assertions.assertDoesNotThrow(() ->
                teamMemberService.registerNewTeamMember(newOrgMemberDTO,"testingorg"));

        Assertions.assertThrows(Exception.class, () ->
                teamMemberService.registerNewTeamMember(newOrgMemberDTO,"12423543"));

    }

    //Sign up dto as front pass to back and complete sign up for teammember
    @Test
    @Order(3)
    void testCompleteUserSignUpProcess(){
        // set organization for testing
        Organization organization = new Organization();
        organization.setOrgName("testingorg");
        organizationRepository.save(organization);
        // new org team member DTO
        NewOrgMemberDTO newOrgMemberDTO = new NewOrgMemberDTO();
        newOrgMemberDTO.setEmail("teamtest@torg.test");
        newOrgMemberDTO.setFirstName("team1test");
        newOrgMemberDTO.setLastName("lastname");
        newOrgMemberDTO.setRoleName("teammember");
        try {
            teamMemberService.registerNewTeamMember(newOrgMemberDTO,"testingorg");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        //get user for temppasscode
        User user =userRepository.findUserByEmailAndOrganization("teamtest@torg.test",
                                                 "testingorg");
        System.out.println(user.getTempPasscode());
        Assertions.assertNotNull(user);
        Assertions.assertFalse(user.getisEnabled());
        String temppasscode = user.getTempPasscode();
        //create signup dto same as front pass
        SignUPDTO signUPDTO = new SignUPDTO();
        signUPDTO.setEmail("teamtest@torg.test");
        signUPDTO.setPassword("12");
        signUPDTO.setOrganization("testingorg");
        signUPDTO.setTempPasccode(temppasscode);
        Assertions.assertEquals("teammember", teamMemberService.completeUserSignUpProcess(signUPDTO));

    }
}
