package org.nidhishah.meetingscheduler.entity;

import jakarta.persistence.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.nidhishah.meetingscheduler.dto.TimeSlot;

import java.io.IOException;
import java.util.List;

@Entity
public class TeamMemberAvailability {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne(targetEntity = User.class,fetch = FetchType.LAZY)
    private User teammember;

    private String mondayTimeSlot;

    public TeamMemberAvailability() {
    }

    public Long getId() {
        return id;
    }

    public User getTeammember() {
        return teammember;
    }

    public void setTeammember(User teammember) {
        this.teammember = teammember;
    }

    public String getMondayTimeSlot() {
        return mondayTimeSlot;
    }

    public void setMondayTimeSlot(String mondayTimeSlot) {
        this.mondayTimeSlot = mondayTimeSlot;
    }

    // Implement serialization and deserialization methods
    public String serializeTimeSlots(List<TimeSlot> timeSlots) throws JsonProcessingException {
        System.out.println("Serializing Timeslots");
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        return objectMapper.writeValueAsString(timeSlots);
    }

    public List<TimeSlot> deserializeTimeSlots(String timeslotStr) throws IOException {
        System.out.println("DeSerializing Timeslots");
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        return objectMapper.readValue(timeslotStr, new TypeReference<List<TimeSlot>>() {});
    }
}
