package org.nidhishah.meetingscheduler.repository;

import org.nidhishah.meetingscheduler.dto.ClientDTO;

import org.nidhishah.meetingscheduler.dto.TeamMemberDTO;
import org.nidhishah.meetingscheduler.entity.Organization;
import org.nidhishah.meetingscheduler.entity.Role;
import org.nidhishah.meetingscheduler.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {

    public User getUsersById(Long id);
    public User findByUsernameAndOrganizationOrgName(String username, String orgName);

    @Query("select u from User u where u.email=:email and u.organization.id = " +
            "(select o.id from Organization o where o.orgName = :adminOrganization)")
    public User findUserByEmailAndOrganization(@Param("email") String email,@Param("adminOrganization") String adminOrganization);
//    public List<User> getUsersByOrganizationAndRole(Organization organization, List<Role> roles);

    /// ///// This does not work......
    @Query(value = "SELECT u.firstName, u.lastName, r.roleName, o.orgName " +
            "FROM User u " +
            "JOIN Role r ON u.role.id = r.id " +
            "JOIN Organization o ON u.organization.id = o.id " +
            "WHERE o.orgName = :organizationName " +
            "AND r.roleName IN ('admin', 'teammember')")
    List<Object[]> findUsersByOrganizationNameAndRole(@Param("organizationName") String organizationName);

    ///// perfect mapping and getting everything in TEammemberDTO...
    @Query(value = "SELECT new org.nidhishah.meetingscheduler.dto.TeamMemberDTO(u.firstName, u.lastName, r.roleName, o.orgName) " +
            "FROM User u " +
            "JOIN Role r ON u.role.id = r.id " +
            "JOIN Organization o ON u.organization.id = o.id " +
            "WHERE o.orgName = :organizationName " +
            "AND r.roleName IN ('admin', 'teammember')")
    List<TeamMemberDTO> getUsersByOrganizationNameAndRole(@Param("organizationName") String organizationName);


    //// Get clients list
    @Query(value = "SELECT new org.nidhishah.meetingscheduler.dto.ClientDTO(u.firstName, u.lastName, r.roleName, o.orgName) " +
            "FROM User u " +
            "JOIN Role r ON u.role.id = r.id " +
            "JOIN Organization o ON u.organization.id = o.id " +
            "WHERE o.orgName = :organizationName " +
            "AND r.roleName ='client'")
    List<ClientDTO> getUsersByOrgNameAndRole(@Param("organizationName") String organizationName);
}
