package org.nidhishah.meetingscheduler.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SignUPDTO {

    private String email;
    private String password;
    private String organization;
    private String tempPasccode;
}
