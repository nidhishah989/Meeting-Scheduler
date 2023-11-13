package org.nidhishah.meetingscheduler.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.nidhishah.meetingscheduler.entity.Organization;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MeetingDTO {

    private String meetingType;
    private TimeSlot timeslot;
    private String meetingDate;
    private String meetingDay;
    private String organization;
    private String teamMemberId;
    private String clientID;




}
