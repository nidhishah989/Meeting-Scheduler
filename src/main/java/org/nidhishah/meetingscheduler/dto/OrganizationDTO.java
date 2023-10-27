package org.nidhishah.meetingscheduler.dto;


import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrganizationDTO {

    @NotNull
    private String orgName;

    private String orgDescription;
    private String baseUrl;
    private String meetingType;
    private String meetingWindow;

    // Constructors, getters, setters, and other fields as needed
}

