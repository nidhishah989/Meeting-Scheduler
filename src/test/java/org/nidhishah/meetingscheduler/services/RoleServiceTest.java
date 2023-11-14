package org.nidhishah.meetingscheduler.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.nidhishah.meetingscheduler.entity.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class RoleServiceTest {
    @Autowired
    RoleServiceImpl roleService;

    //These three roles available and needed for app.
    @ParameterizedTest
    @ValueSource(strings = {"admin", "teammember", "client"})
    void testFindByRoleName(String roleName) {
        Role role = roleService.findByRoleName(roleName);
        Assertions.assertNotNull(role);
    }
}
