package org.nidhishah.meetingscheduler.services;

import org.nidhishah.meetingscheduler.entity.Role;
import org.nidhishah.meetingscheduler.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleServiceImpl implements RoleService{

    private final RoleRepository roleRepository;

    @Autowired
    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public Role findByRoleName(String roleName) {
        return roleRepository.findByRoleName(roleName);
    }

    @Override
    public List<Role> getUserRoleById(Long id) {
        return null;
    }
}
