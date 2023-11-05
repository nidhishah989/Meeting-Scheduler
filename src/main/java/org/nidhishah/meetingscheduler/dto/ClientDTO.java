package org.nidhishah.meetingscheduler.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClientDTO {

    private String firstName;
    private String lastName;
    private String roleName; // To store the role name from the associated Role entity
    private String orgName; // To store the organization name from the associated Organization entity

    public ClientDTO() {
    }

    public ClientDTO(String firstName, String lastName, String roleName, String orgName) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.roleName = roleName;
        this.orgName = orgName;
    }


}
