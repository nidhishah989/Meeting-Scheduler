package org.nidhishah.meetingscheduler.services;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface UserService extends UserDetailsService {


    public UserDetails loadUserByUsername(String username, String organization);

    public boolean findUserByEmailAndOrganization(String email,String organization);

}
