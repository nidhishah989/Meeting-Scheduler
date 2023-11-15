package org.nidhishah.meetingscheduler.dto;


import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Getter
@Setter

public class OrganizationDTO {


    private String orgName;
    private String orgDescription;
    private String baseUrl;
    private String orgAddress1;
    private String orgAddress2;
    private String orgCity;
    private String orgState;
    private String orgCountry;
    private String orgContact;

    // Constructors, getters, setters, and other fields as needed

    public OrganizationDTO() {
    }
}

