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

    @OneToOne(targetEntity = User.class,fetch = FetchType.LAZY)
    private User teammember;

    private String mondayTimeSlot;

    private String tuesdayTimeSlot;

    private String wednesdayTimeSlot;

    private String thursdayTimeSlot;

    private String fridayTimeSlot;

    private String saturdayTimeSlot;

    private String sundayTimeSlot;

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

    public String getTuesdayTimeSlot() {
        return tuesdayTimeSlot;
    }

    public void setTuesdayTimeSlot(String tuesdayTimeSlot) {
        this.tuesdayTimeSlot = tuesdayTimeSlot;
    }

    public String getWednesdayTimeSlot() {
        return wednesdayTimeSlot;
    }

    public void setWednesdayTimeSlot(String wednesdayTimeSlot) {
        this.wednesdayTimeSlot = wednesdayTimeSlot;
    }

    public String getThursdayTimeSlot() {
        return thursdayTimeSlot;
    }

    public void setThursdayTimeSlot(String thursdayTimeSlot) {
        this.thursdayTimeSlot = thursdayTimeSlot;
    }

    public String getFridayTimeSlot() {
        return fridayTimeSlot;
    }

    public void setFridayTimeSlot(String fridayTimeSlot) {
        this.fridayTimeSlot = fridayTimeSlot;
    }

    public String getSaturdayTimeSlot() {
        return saturdayTimeSlot;
    }

    public void setSaturdayTimeSlot(String saturdayTimeSlot) {
        this.saturdayTimeSlot = saturdayTimeSlot;
    }

    public String getSundayTimeSlot() {
        return sundayTimeSlot;
    }

    public void setSundayTimeSlot(String sundayTimeSlot) {
        this.sundayTimeSlot = sundayTimeSlot;
    }

    // Implement serialization and deserialization methods
    public String serializeTimeSlots(List<TimeSlot> timeSlots) throws JsonProcessingException {
        System.out.println("Serializing Timeslots");
        ObjectMapper objectMapper = new ObjectMapper();
//        objectMapper.registerModule(new JavaTimeModule());
        return objectMapper.writeValueAsString(timeSlots);
    }

    public List<TimeSlot> deserializeTimeSlots(String timeslotStr) throws IOException {
        System.out.println("DeSerializing Timeslots");
        ObjectMapper objectMapper = new ObjectMapper();
//        objectMapper.registerModule(new JavaTimeModule());
        return objectMapper.readValue(timeslotStr, new TypeReference<List<TimeSlot>>() {});
    }
}
