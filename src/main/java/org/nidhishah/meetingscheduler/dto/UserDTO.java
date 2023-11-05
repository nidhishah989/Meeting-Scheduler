package org.nidhishah.meetingscheduler.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDTO {

    private Long id;
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private RoleDTO role; // To store the role name from the associated Role entity
    private OrganizationDTO organization; // To store the organization name from the associated Organization entity

    public UserDTO() {
    }
}
