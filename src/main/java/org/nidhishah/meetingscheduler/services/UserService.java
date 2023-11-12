package org.nidhishah.meetingscheduler.services;

import org.nidhishah.meetingscheduler.entity.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Optional;

public interface UserService extends UserDetailsService {


//    public UserDetails loadUserByUsername(String username, String organization);

    public boolean findUserByEmailAndOrganization(String email,String organization);



}
