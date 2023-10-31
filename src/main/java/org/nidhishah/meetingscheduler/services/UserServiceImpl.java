package org.nidhishah.meetingscheduler.services;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Service
public class UserServiceImpl implements UserService{
    @Override
    public UserDetails loadUserByUsername(String username, String organization) {

        return null;
    }
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("In loaduserbyusername");
        // Retrieve the organization from the request attribute
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        System.out.println("Passed username:"+username);
        String requsername = (String) request.getAttribute("username");
        String organization = (String) request.getAttribute("organization");
        System.out.println("request username:"+requsername+"organization: "+organization);

        return null;
    }
}
