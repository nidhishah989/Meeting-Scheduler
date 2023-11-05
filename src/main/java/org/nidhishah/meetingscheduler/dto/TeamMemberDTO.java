package org.nidhishah.meetingscheduler.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class TeamMemberDTO {
//    private Long id;
//    private String username;
    private String firstName;
    private String lastName;
//    private String email;
//    private String password;
    private String roleName; // To store the role name from the associated Role entity
    private String orgName; // To store the organization name from the associated Organization entity


    public TeamMemberDTO() {
    }

    public TeamMemberDTO(String firstName, String lastName, String role, String organization) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.roleName = role;
        this.orgName = organization;
    }

//    public TeamMemberDTO(String firstName, String lastName, RoleDTO role) {
//        this.firstName = firstName;
//        this.lastName = lastName;
//        this.role = role;
//    }

    // Getters and setters for the fields

    @Override
    public String toString() {
        return "TeamMemberDTO{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", roleName='" + roleName + '\'' +
                ", orgName='" + orgName + '\'' +
                '}';
    }
    // You can generate these using your IDE or write them manually
}
