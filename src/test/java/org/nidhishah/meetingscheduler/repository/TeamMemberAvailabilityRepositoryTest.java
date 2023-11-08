package org.nidhishah.meetingscheduler.repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
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
    public void saveMondayTimeSlotTest(){

        // Create a list of timeslots
        TimeSlot timeSlot1 = new TimeSlot(LocalTime.of(9, 0), LocalTime.of(12, 0));
        TimeSlot timeSlot2 = new TimeSlot(LocalTime.of(1, 0), LocalTime.of(4, 0));

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
}
