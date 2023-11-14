package org.nidhishah.meetingscheduler.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.nidhishah.meetingscheduler.validation.ValidPassword;

@Getter
@Setter
public class UserDTO {

    private Long id;
    private String username;
    @NotEmpty(message = "Please provide firstName.")
    private String firstName;
    @NotEmpty(message = "Please provide lastName.")
    private String lastName;
    @Email(message = "Email cannot be Empty.")
    private String email;
    @NotEmpty(message = "Password cannot be empty.")
    @ValidPassword(message = "Password must be at least 8 characters long")
    private String password;
    private RoleDTO role; // To store the role name from the associated Role entity
    private OrganizationDTO organization; // To store the organization name from the associated Organization entity

    public UserDTO() {
    }
}
