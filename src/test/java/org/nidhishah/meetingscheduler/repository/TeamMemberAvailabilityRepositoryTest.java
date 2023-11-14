package org.nidhishah.meetingscheduler.repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.*;
import org.nidhishah.meetingscheduler.dto.TimeSlot;
import org.nidhishah.meetingscheduler.entity.Organization;
import org.nidhishah.meetingscheduler.entity.Role;
import org.nidhishah.meetingscheduler.entity.TeamMemberAvailability;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.nidhishah.meetingscheduler.entity.User;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.time.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/************
 * Test TImeAvailability by setting one teammember availability
 * Specially test serialization and deserialization
 */
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TeamMemberAvailabilityRepositoryTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    OrganizationRepository organizationRepository;

    @Autowired
    TeamMemberAvalibilityRepository teamMemberAvalibilityRepository;

    @Test
    @Order(1)
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
        //add timeslots for teammember1
        TeamMemberAvailability teamMemberAvailability = new TeamMemberAvailability();
        teamMemberAvailability.setTeammember(teamMember1);
        // Create a list of timeslots
        TimeSlot timeSlot1 = new TimeSlot(LocalTime.of(9, 0), LocalTime.of(12, 0));
        TimeSlot timeSlot2 = new TimeSlot(LocalTime.of(13, 0), LocalTime.of(16, 0));

        List<TimeSlot> timeSlotList = new ArrayList<>();
        timeSlotList.add(timeSlot1);
        timeSlotList.add(timeSlot2);
        //set teammemberavailability
        try {
            teamMemberAvailability.setMondayTimeSlot(teamMemberAvailability.serializeTimeSlots(timeSlotList));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }


    }

    // check admin mondayTimeSlots getting saved and retrieved same
    @Test
    @Order(2)
    public void saveMondayTimeSlotTest(){

        // Create a list of timeslots
        TimeSlot timeSlot1 = new TimeSlot(LocalTime.of(9, 0), LocalTime.of(12, 0));
        TimeSlot timeSlot2 = new TimeSlot(LocalTime.of(13, 0), LocalTime.of(16, 0));

        List<TimeSlot> timeSlotList = new ArrayList<>();
        timeSlotList.add(timeSlot1);
        timeSlotList.add(timeSlot2);
        //get teammember
        User teammember = userRepository.findByUsernameAndOrganizationOrgName("admin","testOrg2");
        System.out.println("In saving teammemberAvailability Test: TeamMember UserName: " + teammember.getUsername());
        //TeamMemberAvailability save
        TeamMemberAvailability teamMemberAvailability = new TeamMemberAvailability();
        try {
            System.out.println("Let's Serialize list first.....");
            String mondayTimeSlotStr = teamMemberAvailability.serializeTimeSlots(timeSlotList);
            System.out.println("In saving teammemberAvailability Test: SerializedMondayTimeSlots string : "+ mondayTimeSlotStr);

//            System.out.println("In saving teammemberAvailability Test: TeamMember roleName: " + teammember.getRole().getRoleName());
            teamMemberAvailability.setTeammember(teammember);
            teamMemberAvailability.setMondayTimeSlot(mondayTimeSlotStr);
            teamMemberAvalibilityRepository.save(teamMemberAvailability);
        }catch (Exception e){
            e.printStackTrace();
        }

        //Now check is there or not
        // Retrieve the entity from the database
        TeamMemberAvailability savedAvailability = teamMemberAvalibilityRepository.getTeamMemberAvailabilityByTeammember_Id(teammember.getId());

        // assertion - check list fetch is similar that we saved
        try {
            List<TimeSlot> savedMondayTimeSlots = savedAvailability.deserializeTimeSlots(savedAvailability.getMondayTimeSlot());
            Assertions.assertNotNull(savedMondayTimeSlots);

            Assertions.assertEquals(timeSlotList.size(), savedMondayTimeSlots.size());
            for (int i=0;i< savedMondayTimeSlots.size();i++){
                Assertions.assertEquals(timeSlotList.get(i).getStartTime(),savedMondayTimeSlots.get(i).getStartTime());
                Assertions.assertEquals(timeSlotList.get(i).getEndTime(),savedMondayTimeSlots.get(i).getEndTime());
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    ////////////////// UPDATE MONDAY TIMESLOT BY ADDING NEW - NOT CHECKING CRASHING TIME SLOTS HERE //////
    // FOR TEAM MEMBER: add new timeslot and check have three timeslots
    @Test
    @Order(3)
    public void testTeamMemberAvailabilityUpdate(){

        //new Monday availability
        TimeSlot timeSlot = new TimeSlot(LocalTime.of(13,17),LocalTime.of(17,0));
        //deserialize the mondaytimeslots
        try {
            // The teammember is already in User table
            //get first that- like is authenticated
            User user = userRepository.findByUsernameAndOrganizationOrgName("admin","testOrg2");
            System.out.println(user.getUsername());
            // now check if the teammember availability entry have been done or not
            TeamMemberAvailability memberAvailability = teamMemberAvalibilityRepository.getTeamMemberAvailabilityByTeammember_Id(user.getId());



            List<TimeSlot> savedMondayTimeSlots = memberAvailability.deserializeTimeSlots(memberAvailability.getMondayTimeSlot());
            Assertions.assertNotNull(savedMondayTimeSlots);
            Integer beforeAvailability = savedMondayTimeSlots.size();
            // without checking timeslot crashing .. just updating it
            savedMondayTimeSlots.add(timeSlot);
            //now serialize it back to save
            String mondayTimeSlotStr = memberAvailability.serializeTimeSlots(savedMondayTimeSlots);
            memberAvailability.setMondayTimeSlot(mondayTimeSlotStr);
            teamMemberAvalibilityRepository.save(memberAvailability);

            //check updated one:
            // now check if the teammember availability entry have been done or not
            TeamMemberAvailability updatedAvailablity = teamMemberAvalibilityRepository.getTeamMemberAvailabilityByTeammember_Id(user.getId());
            List<TimeSlot> updatedMondayTimeSlots = memberAvailability.deserializeTimeSlots(memberAvailability.getMondayTimeSlot());
            Integer afterAvailability = updatedMondayTimeSlots.size();

            Assertions.assertEquals(beforeAvailability +1 ,afterAvailability);

        }catch (Exception e){
            e.printStackTrace();
        }

    }

    //////////////////////////// DELETE MONDAY TIMESLOT and CHECK SIZE IS LESS THAN PREVIOUS ONE //////////////
    @Test
    @Order(4)
    public void testTeamMemberAvailabilityDelete(){
        // The teammember is already in User table
        //get first that- like is authenticated
        User user = userRepository.findByUsernameAndOrganizationOrgName("team1","testOrg2");

        // now check if the teammember availability entry have been done or not
        TeamMemberAvailability memberAvailability = teamMemberAvalibilityRepository.getTeamMemberAvailabilityByTeammember_Id(user.getId());

        if(memberAvailability!= null){
            try {
                List<TimeSlot> savedMondayTimeSlots = memberAvailability.deserializeTimeSlots(memberAvailability.getMondayTimeSlot());
                Integer previoussize = savedMondayTimeSlots.size();
                System.out.println("MondayTimeSlots size before deleting last one: " + previoussize);
                // now we are deleting the last availability
                savedMondayTimeSlots.remove(savedMondayTimeSlots.size()-1);
                // serialize it and save it
                String mondayTimeSlotStr = memberAvailability.serializeTimeSlots(savedMondayTimeSlots);
                memberAvailability.setMondayTimeSlot(mondayTimeSlotStr);
                teamMemberAvalibilityRepository.save(memberAvailability);
                // now retreive again and check size is less than previous one
                TeamMemberAvailability updatedAvailability = teamMemberAvalibilityRepository.getTeamMemberAvailabilityByTeammember_Id(user.getId());
                //serialize Mondayslots and check size
                List<TimeSlot> updatedMondaySlots = updatedAvailability.deserializeTimeSlots(updatedAvailability.getMondayTimeSlot());
                Assertions.assertEquals(previoussize-1,updatedMondaySlots.size());
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
