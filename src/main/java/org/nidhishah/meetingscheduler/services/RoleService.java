package org.nidhishah.meetingscheduler.services;

import org.nidhishah.meetingscheduler.entity.Role;

public interface RoleService {
  public Role findByRoleName(String roleName);
}
