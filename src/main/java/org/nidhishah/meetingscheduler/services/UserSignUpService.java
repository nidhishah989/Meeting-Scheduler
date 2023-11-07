package org.nidhishah.meetingscheduler.services;

import org.nidhishah.meetingscheduler.dto.SignUPDTO;

public interface UserSignUpService {

    public String completeUserSignUpProcess(SignUPDTO signUPDTO);
}
