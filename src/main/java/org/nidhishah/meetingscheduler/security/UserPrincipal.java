package org.nidhishah.meetingscheduler.security;

import org.nidhishah.meetingscheduler.entity.Role;
import org.nidhishah.meetingscheduler.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class UserPrincipal implements UserDetails {

    private User user;
    private List<Role> roles;

    private String userOrganizationName;

    public UserPrincipal(User user,List<Role> roles, String organization) {
        super();
        this.user = user;
        this.roles = roles;
        this.userOrganizationName = organization;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream().map(role -> new SimpleGrantedAuthority(role.getRoleName())).collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return this.user.getPassword();
    }

    @Override
    public String getUsername() {
        return this.user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {

        return user.getisEnabled();
    }


    public String getOrganizationName(){
        return userOrganizationName;
    }
}
