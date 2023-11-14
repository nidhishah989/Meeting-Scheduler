package org.nidhishah.meetingscheduler.repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.nidhishah.meetingscheduler.entity.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/* ROLE REPOSITORY PARAMETERIZED TESTING
*  checking data.sql inserting the roles or not
* When application start, role entity is already set up.
* */
@SpringBootTest
public class RoleRepositoryTest {

    @Autowired
    RoleRepository roleRepository;

    //These three roles available and needed for app.
    @ParameterizedTest
    @ValueSource(strings = {"admin", "teammember", "client"})
    void testFindByRoleName(String roleName) {
        Role role = roleRepository.findByRoleName(roleName);
        Assertions.assertNotNull(role);
    }

}
