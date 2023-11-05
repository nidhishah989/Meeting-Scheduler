package org.nidhishah.meetingscheduler.services;

import jakarta.servlet.http.HttpServletRequest;
import org.nidhishah.meetingscheduler.entity.Role;
import org.nidhishah.meetingscheduler.entity.User;
import org.nidhishah.meetingscheduler.repository.RoleRepository;
import org.nidhishah.meetingscheduler.repository.UserRepository;
import org.nidhishah.meetingscheduler.security.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService{

    private UserRepository userRepository;
    private RoleRepository roleRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository,RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

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

        User currentUser = userRepository.findByUsernameAndOrganizationOrgName(username,organization);

        System.out.println("Found User: "+currentUser);
        System.out.println("CurrentUser: "+ currentUser.getUsername());
        if(currentUser == null) {
            throw new UsernameNotFoundException("Invalid login or password");
        }
        else{
             Optional<Role> roleOptional = roleRepository.findById(currentUser.getRole().getId());
             Role role = roleOptional.get();
             List<Role> userRole = new ArrayList<>();
             userRole.add(role);
//            return new org.springframework.security.core.userdetails.User(currentUser.getUsername(),
//                    currentUser.getPassword(),mapStringToAuthorities(role.getRoleName()));
              return new UserPrincipal(currentUser,userRole,organization);
        }
    }


    private Collection<? extends GrantedAuthority> mapStringToAuthorities(String customAuthority) {
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(customAuthority));
        return authorities;

    }
}
