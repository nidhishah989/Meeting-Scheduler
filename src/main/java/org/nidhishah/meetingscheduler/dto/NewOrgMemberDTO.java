package org.nidhishah.meetingscheduler.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NewOrgMemberDTO {

    private String firstName;
    private String lastName;

    private String roleName;

    private String email;

    public NewOrgMemberDTO() {
    }

}
