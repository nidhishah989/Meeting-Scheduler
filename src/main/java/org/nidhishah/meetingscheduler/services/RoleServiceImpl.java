/*****
 * Role Service: To collect Role Entity as needed for user
 * By Nidhi Shah
 */
package org.nidhishah.meetingscheduler.services;

import org.nidhishah.meetingscheduler.entity.Role;
import org.nidhishah.meetingscheduler.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleServiceImpl implements RoleService{

    private final RoleRepository roleRepository;

    @Autowired
    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    //find Role entity by rolename
    @Override
    public Role findByRoleName(String roleName) {
        return roleRepository.findByRoleName(roleName);
    }

}
