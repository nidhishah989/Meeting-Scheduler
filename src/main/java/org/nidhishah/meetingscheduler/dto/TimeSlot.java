package org.nidhishah.meetingscheduler.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
public class TimeSlot {

    private String startTime;
    private String endTime;

    public TimeSlot(LocalTime startTime, LocalTime endTime) {
        this.startTime = startTime.toString();
        this.endTime = endTime.toString();
    }

}
