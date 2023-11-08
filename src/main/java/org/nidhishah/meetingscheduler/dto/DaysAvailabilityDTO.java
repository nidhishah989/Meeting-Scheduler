package org.nidhishah.meetingscheduler.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.List;
import java.sql.Time;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DaysAvailabilityDTO {

    List<TimeSlot> MondayTimeSlot;
    List<TimeSlot> TuesdayTimeSlot;
    List<TimeSlot> WednesdayTimeSlot;
    List<TimeSlot> ThursdayTimeSlot;
    List<TimeSlot> FridayTimeSlot;
    List<TimeSlot> SaturdayTimeSlot;
    List<TimeSlot> SundayTimeSlot;

}
