package org.nidhishah.meetingscheduler.repository;

import org.junit.jupiter.api.*;
import org.nidhishah.meetingscheduler.dto.TimeSlot;
import org.nidhishah.meetingscheduler.entity.TeamMemberAvailability;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.nidhishah.meetingscheduler.entity.User;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.time.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TeamMemberAvailabilityRepositoryTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    TeamMemberAvalibilityRepository teamMemberAvalibilityRepository;
    //////////////////////////////////////////////////////////////////////////
    //////// May be need to do before for setting organization teammember or admin before testing /////////////
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////


    // Testing the serializer for timeslot saving and deserializer for timeslot retrieving with testing entity save and retrieve functionality
    ////////////////// Only for Monday ////////////////////////////////////////
    // Note: this will be good if there is not timeslot for monday there. _ initital testing
    @Test
    @Order(1)
    public void saveMondayTimeSlotTest(){

        // Create a list of timeslots
        TimeSlot timeSlot1 = new TimeSlot(LocalTime.of(9, 0), LocalTime.of(12, 0));
        TimeSlot timeSlot2 = new TimeSlot(LocalTime.of(13, 0), LocalTime.of(16, 0));

        List<TimeSlot> timeSlotList = new ArrayList<>();
        timeSlotList.add(timeSlot1);
        timeSlotList.add(timeSlot2);

        //TeamMemberAvailability save
        TeamMemberAvailability teamMemberAvailability = new TeamMemberAvailability();
        try {
            System.out.println("Let's Serialize list first.....");
            String mondayTimeSlotStr = teamMemberAvailability.serializeTimeSlots(timeSlotList);
            System.out.println("In saving teammemberAvailability Test: SerializedMondayTimeSlots string : "+ mondayTimeSlotStr);
            //get teammember
            User teammember = userRepository.findByUsernameAndOrganizationOrgName("nidhishah989","meetsy");
            System.out.println("In saving teammemberAvailability Test: TeamMember UserName: " + teammember.getUsername());
//            System.out.println("In saving teammemberAvailability Test: TeamMember roleName: " + teammember.getRole().getRoleName());
            teamMemberAvailability.setTeammember(teammember);
            teamMemberAvailability.setMondayTimeSlot(mondayTimeSlotStr);
            teamMemberAvalibilityRepository.save(teamMemberAvailability);
        }catch (Exception e){
            e.printStackTrace();
        }

        //Now check is there or not
        // Retrieve the entity from the database
        Optional<TeamMemberAvailability> savedAvailabilityOptional = teamMemberAvalibilityRepository.findById(teamMemberAvailability.getId());
        // get deserailzing string
        TeamMemberAvailability savedAvailability = savedAvailabilityOptional.get();
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


    ///// Previously we are passing Monday time availability //////
    /// so the entry is there ///
    @Test
    @Order(2)
    public void testTeamMemberAvailabiltyAlreadyPresent(){
        // The teammember is already in User table
        //get first that- like is authenticated
        User user = userRepository.findByUsernameAndOrganizationOrgName("nidhishah989","meetsy");

        // now check if the teammember availability entry have been done or not
        TeamMemberAvailability memberAvailability = teamMemberAvalibilityRepository.getByTeammember_Id(user.getId());

        Assertions.assertNotNull(memberAvailability);
    }

    ////////////////// UPDATE MONDAY TIMESLOT BY ADDING NEW - NOT CHECKING CRASHING TIME SLOTS HERE //////
    @Test
    @Order(3)
    public void testTeamMemberAvailabilityUpdate(){
        // The teammember is already in User table
        //get first that- like is authenticated
        User user = userRepository.findByUsernameAndOrganizationOrgName("nidhishah989","meetsy");

        // now check if the teammember availability entry have been done or not
        TeamMemberAvailability memberAvailability = teamMemberAvalibilityRepository.getByTeammember_Id(user.getId());

        Assertions.assertNotNull(memberAvailability);

        //new Monday availability
        TimeSlot timeSlot = new TimeSlot(LocalTime.of(13,17),LocalTime.of(17,0));
        //deserialize the mondaytimeslots
        try {
            List<TimeSlot> savedMondayTimeSlots = memberAvailability.deserializeTimeSlots(memberAvailability.getMondayTimeSlot());
            Assertions.assertNotNull(savedMondayTimeSlots);
            // without checking timeslot crashing .. just updating it
            savedMondayTimeSlots.add(timeSlot);
            //now serialize it back to save
            String mondayTimeSlotStr = memberAvailability.serializeTimeSlots(savedMondayTimeSlots);
            memberAvailability.setMondayTimeSlot(mondayTimeSlotStr);
            teamMemberAvalibilityRepository.save(memberAvailability);
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
        User user = userRepository.findByUsernameAndOrganizationOrgName("nidhishah989","meetsy");

        // now check if the teammember availability entry have been done or not
        TeamMemberAvailability memberAvailability = teamMemberAvalibilityRepository.getByTeammember_Id(user.getId());

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
                TeamMemberAvailability updatedAvailability = teamMemberAvalibilityRepository.getByTeammember_Id(user.getId());
                //serialize Mondayslots and check size
                List<TimeSlot> updatedMondaySlots = updatedAvailability.deserializeTimeSlots(updatedAvailability.getMondayTimeSlot());
                Assertions.assertEquals(previoussize-1,updatedMondaySlots.size());
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
