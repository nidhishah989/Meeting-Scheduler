package org.nidhishah.meetingscheduler.services;

import org.nidhishah.meetingscheduler.entity.Role;

import java.util.List;


public interface RoleService {
  public Role findByRoleName(String roleName);

}
