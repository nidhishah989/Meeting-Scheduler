package org.nidhishah.meetingscheduler.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class TeamMemberDTO {
    private Long id;
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private RoleDTO role; // To store the role name from the associated Role entity
    private OrganizationDTO organization; // To store the organization name from the associated Organization entity

    public TeamMemberDTO() {
        // Default constructor
    }

    // Getters and setters for the fields
    // You can generate these using your IDE or write them manually
}
